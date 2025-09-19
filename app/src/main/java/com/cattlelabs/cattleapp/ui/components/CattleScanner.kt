@file:OptIn(ExperimentalMaterial3Api::class)

package com.cattlelabs.cattleapp.ui.components

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CattleScanner(
    onBackClick: () -> Unit = {},
    onImageCaptured: (Uri) -> Unit = {},
    onImageSelected: (Uri) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isFlashOn by remember { mutableStateOf(false) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var camera: Camera? by remember { mutableStateOf(null) }
    var isCapturing by remember { mutableStateOf(false) }

    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            hasPermission = true
        } else {
            Toast.makeText(context, "Camera permission is required for Cattle Scanner", Toast.LENGTH_LONG).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onImageSelected(it)
            Toast.makeText(context, "Image selected from gallery", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasPermission) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) { previewView ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                        imageCapture = ImageCapture.Builder()
                            .setFlashMode(
                                if (isFlashOn) ImageCapture.FLASH_MODE_ON
                                else ImageCapture.FLASH_MODE_OFF
                            )
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                            .build()

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        cameraProvider.unbindAll()
                        camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )

                        Log.d("CattleScanner", "Camera initialized successfully")

                    } catch (exc: Exception) {
                        Log.e("CattleScanner", "Camera initialization failed", exc)
                        Toast.makeText(context, "Failed to start camera", Toast.LENGTH_SHORT).show()
                    }
                }, ContextCompat.getMainExecutor(context))
            }

            // Camera overlay with rounded rectangle frame
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Transparent)
                ) {
                    FrameCorners()
                }
            }

            // Instruction text
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 200.dp)
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Position the cattle within the frame",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera Permission",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Camera Access Required",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "We need camera access to scan and identify cattle breeds",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green
                        )
                    ) {
                        Text("Grant Camera Permission")
                    }
                }
            }
        }

        // Top bar with title and controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    cameraExecutor.shutdown()
                    onBackClick()
                },
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            Text(
                text = "Cattle Scanner",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            if (hasPermission) {
                IconButton(
                    onClick = {
                        isFlashOn = !isFlashOn
                        imageCapture?.flashMode = if (isFlashOn) {
                            ImageCapture.FLASH_MODE_ON
                        } else {
                            ImageCapture.FLASH_MODE_OFF
                        }
                        Toast.makeText(
                            context,
                            if (isFlashOn) "Flash On" else "Flash Off",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = if (isFlashOn) "Turn Flash Off" else "Turn Flash On",
                        tint = if (isFlashOn) Color.Yellow else Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // Bottom controls
        if (hasPermission) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        try {
                            galleryLauncher.launch("image/*")
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to open gallery", Toast.LENGTH_SHORT).show()
                            Log.e("CattleScanner", "Gallery launch failed", e)
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Color.Black.copy(alpha = 0.7f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Select from Gallery",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Button(
                    onClick = {
                        if (!isCapturing) {
                            captureImage(
                                imageCapture = imageCapture,
                                context = context,
                                cameraExecutor = cameraExecutor,
                                onImageCaptured = { uri ->
                                    isCapturing = false
                                    onImageCaptured(uri)
                                    Toast.makeText(context, "Photo captured successfully", Toast.LENGTH_SHORT).show()
                                },
                                onError = { exception ->
                                    isCapturing = false
                                    Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
                                    Log.e("CattleScanner", "Image capture failed", exception)
                                }
                            )
                            isCapturing = true
                        }
                    },
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCapturing) Color.Gray else Color.Green,
                        disabledContainerColor = Color.Gray
                    ),
                    contentPadding = PaddingValues(0.dp),
                    enabled = !isCapturing
                ) {
                    if (isCapturing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.Green, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Color.White, CircleShape)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        Toast.makeText(context, "Settings - Coming Soon", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Color.Black.copy(alpha = 0.7f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FrameCorners() {
    val cornerSize = 24.dp
    val cornerThickness = 4.dp
    val cornerColor = Color.White

    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left corner
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(cornerSize)
        ) {
            Box(
                modifier = Modifier
                    .width(cornerSize)
                    .height(cornerThickness)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
            Box(
                modifier = Modifier
                    .width(cornerThickness)
                    .height(cornerSize)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
        }

        // Top-right corner
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(cornerSize)
        ) {
            Box(
                modifier = Modifier
                    .width(cornerSize)
                    .height(cornerThickness)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(cornerThickness)
                    .height(cornerSize)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
        }

        // Bottom-left corner
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(cornerSize)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .width(cornerSize)
                    .height(cornerThickness)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
            Box(
                modifier = Modifier
                    .width(cornerThickness)
                    .height(cornerSize)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
        }

        // Bottom-right corner
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(cornerSize)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(cornerSize)
                    .height(cornerThickness)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(cornerThickness)
                    .height(cornerSize)
                    .background(cornerColor, RoundedCornerShape(2.dp))
            )
        }
    }
}

private fun captureImage(
    imageCapture: ImageCapture?,
    context: Context,
    cameraExecutor: ExecutorService,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val imageCapture = imageCapture ?: run {
        onError(ImageCaptureException(ImageCapture.ERROR_CAPTURE_FAILED, "ImageCapture is null", null))
        return
    }

    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis())

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "cattle_$name")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CattleApp")
        }
    }

    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    try {
        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CattleScanner", "Photo capture failed: ${exception.message}", exception)
                    onError(exception)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.let { uri ->
                        Log.d("CattleScanner", "Photo captured successfully: $uri")
                        onImageCaptured(uri)
                    } ?: run {
                        Log.e("CattleScanner", "Saved URI is null")
                        onError(ImageCaptureException(ImageCapture.ERROR_FILE_IO, "Saved URI is null", null))
                    }
                }
            }
        )
    } catch (e: Exception) {
        Log.e("CattleScanner", "Failed to take picture", e)
        onError(ImageCaptureException(ImageCapture.ERROR_CAPTURE_FAILED, "Failed to take picture", e))
    }
}