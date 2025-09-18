package com.example.cattleapp.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.cattleapp.viewmodel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * CameraX Compose Preview with ImageCapture.
 * On "Capture" we save a temporary file and call viewModel.scanImageMock(filePath)
 */
@Composable
fun ScanResultScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var isCapturing by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // Preview
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val ic = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build()

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, ic)
                            imageCapture = ic
                        } catch (exc: Exception) {
                            Log.e("ScanScreen", "Camera bind failed", exc)
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isCapturing) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Controls
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                // capture photo
                val ic = imageCapture
                if (ic == null) return@Button

                isCapturing = true
                val file = createFile(context.cacheDir)
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                ic.takePicture(outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        // call viewmodel to simulate ML scan; in production pass the file path or bitmap to your ML pipeline
                        viewModel.scanImageMock(file.absolutePath)
                        isCapturing = false
                        navController.navigate("results")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        isCapturing = false
                        Log.e("ScanScreen", "Photo capture failed: ${exception.message}", exception)
                    }
                })
            }) {
                Text("Capture")
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel")
            }
        }
    }
}

private fun createFile(baseFolder: File): File {
    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    val fileName = "IMG_${sdf.format(Date())}.jpg"
    return File(baseFolder, fileName)
}
