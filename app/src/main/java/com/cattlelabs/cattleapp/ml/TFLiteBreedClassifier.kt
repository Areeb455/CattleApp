package com.cattlelabs.cattleapp.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.cattlelabs.cattleapp.data.BreedData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp

data class OnDeviceBreedPrediction(
    val breedNameEn: String,
    /** 0-100 */
    val confidencePercent: Float
)

/**
 * Runs the bundled ResNet50 breed classifier (cattle_model.tflite) directly on-device,
 * so a scan still works with zero connectivity. It's a plain float32 SavedModel->TFLite
 * export (no quantization, no embedded labels/metadata), so preprocessing must mirror the
 * training pipeline exactly: resize to 224x224, ImageNet mean/std normalization, NHWC RGB.
 *
 * Output class order is NOT embedded in the model. It matches the alphabetically-sorted
 * breed folder names used at training time (Python's `sorted()`), which is assumed to be
 * the same order as [BreedData.allBreeds]. If breeds are ever added/reordered there, this
 * mapping must be re-verified against the training class index (idx_to_class).
 */
@Singleton
class TFLiteBreedClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val MODEL_FILE = "cattle_model.tflite"
        private const val IMAGE_SIZE = 224
        private const val NUM_CLASSES = 41
        private val IMAGENET_MEAN = floatArrayOf(0.485f, 0.456f, 0.406f)
        private val IMAGENET_STD = floatArrayOf(0.229f, 0.224f, 0.225f)
    }

    private val interpreter: Interpreter by lazy { Interpreter(loadModelFile()) }

    /** Loads the image at [imageUri], runs inference, and returns the top [k] breeds. */
    suspend fun classifyTopK(imageUri: Uri, k: Int = 3): List<OnDeviceBreedPrediction> =
        withContext(Dispatchers.Default) {
            val bitmap = loadAndOrientBitmap(imageUri) ?: return@withContext emptyList()

            val input = preprocess(bitmap)
            val output = Array(1) { FloatArray(NUM_CLASSES) }
            interpreter.run(input, output)

            val probabilities = softmax(output[0])

            probabilities.withIndex()
                .sortedByDescending { it.value }
                .take(k)
                .mapNotNull { (index, prob) ->
                    BreedData.allBreeds.getOrNull(index)?.let { breed ->
                        OnDeviceBreedPrediction(
                            breedNameEn = breed.en,
                            confidencePercent = prob * 100f
                        )
                    }
                }
        }

    private fun loadModelFile(): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd(MODEL_FILE)
        FileInputStream(assetFileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            return fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.declaredLength
            )
        }
    }

    private fun preprocess(bitmap: Bitmap): ByteBuffer {
        val resized = Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE, IMAGE_SIZE, true)

        val buffer = ByteBuffer.allocateDirect(4 * IMAGE_SIZE * IMAGE_SIZE * 3)
        buffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        resized.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)

        for (pixel in pixels) {
            val r = ((pixel shr 16) and 0xFF) / 255f
            val g = ((pixel shr 8) and 0xFF) / 255f
            val b = (pixel and 0xFF) / 255f

            buffer.putFloat((r - IMAGENET_MEAN[0]) / IMAGENET_STD[0])
            buffer.putFloat((g - IMAGENET_MEAN[1]) / IMAGENET_STD[1])
            buffer.putFloat((b - IMAGENET_MEAN[2]) / IMAGENET_STD[2])
        }

        buffer.rewind()
        return buffer
    }

    private fun loadAndOrientBitmap(imageUri: Uri): Bitmap? {
        val resolver = context.contentResolver

        val bitmap = resolver.openInputStream(imageUri)?.use { stream ->
            BitmapFactory.decodeStream(stream)
        } ?: return null

        val orientation = resolver.openInputStream(imageUri)?.use { stream ->
            ExifInterface(stream).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        } ?: ExifInterface.ORIENTATION_NORMAL

        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        if (rotationDegrees == 0f) return bitmap

        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val max = logits.maxOrNull() ?: 0f
        val exps = FloatArray(logits.size) { i -> exp((logits[i] - max).toDouble()).toFloat() }
        val sum = exps.sum()
        return FloatArray(exps.size) { i -> exps[i] / sum }
    }
}
