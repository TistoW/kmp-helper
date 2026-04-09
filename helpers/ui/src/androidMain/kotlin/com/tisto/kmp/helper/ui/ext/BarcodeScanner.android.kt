package com.tisto.kmp.helper.ui.ext

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.common.sdkinternal.MlKitContext
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

actual object BeepPlayer {
    private val tone by lazy { ToneGenerator(AudioManager.STREAM_MUSIC, 90) }

    actual fun play() {
        runCatching { tone.startTone(ToneGenerator.TONE_PROP_BEEP, 80) }
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalGetImage::class)
@Composable
internal actual fun PlatformCameraScanner(
    modifier: Modifier,
    scanCooldownMs: Long,
    onResult: (BarcodeResult) -> Unit,
    onScanEffect: (() -> Unit)?,
    onError: (Throwable) -> Unit,
) {
    if (LocalInspectionMode.current) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Camera disabled in Preview")
        }
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { ok -> granted = ok }

    LaunchedEffect(Unit) {
        if (!granted) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (!granted) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Izin kamera dibutuhkan")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Izinkan Kamera")
                }
            }
        }
        return
    }

    val previewView = remember(context) {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // ✅ cooldown gate (shared across analyzer calls)
    val cooldownMsState by rememberUpdatedState(scanCooldownMs.coerceAtLeast(0L))
    val onResultState by rememberUpdatedState(onResult)
    val onErrorState by rememberUpdatedState(onError)
    val onScanEffectState by rememberUpdatedState(onScanEffect)

    DisposableEffect(previewView, lifecycleOwner) {
        val executor = Executors.newSingleThreadExecutor()
        val mainExecutor = ContextCompat.getMainExecutor(context)

        var cameraProvider: ProcessCameraProvider? = null
        var scanner: BarcodeScanner? = null
        var analysis: ImageAnalysis? = null

        // timestamp terakhir emit (thread-safe enough utk single analyzer thread)
        var nextAllowedAt = 0L

//        val future = ProcessCameraProvider.getInstance(context)
//        future.addListener({
//            try {
//                cameraProvider = future.get()
//
//                // ✅ jangan pakai MlKitContext internal. Hapus ensureMlKitInitialized.
//                scanner = BarcodeScanning.getClient()
//
//                val preview = Preview.Builder().build().also {
//                    it.surfaceProvider = previewView.surfaceProvider
//                }
//
//                analysis = ImageAnalysis.Builder()
//                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                    .build()
//
//                analysis.setAnalyzer(executor) { imageProxy ->
//                    val media = imageProxy.image
//                    if (media == null) {
//                        imageProxy.close()
//                        return@setAnalyzer
//                    }
//
//                    // ✅ cooldown: jangan proses kalau belum waktunya
//                    val now = System.currentTimeMillis()
//                    if (now < nextAllowedAt) {
//                        imageProxy.close()
//                        return@setAnalyzer
//                    }
//
//                    val input = InputImage.fromMediaImage(
//                        media,
//                        imageProxy.imageInfo.rotationDegrees
//                    )
//
//                    scanner.process(input)
//                        .addOnSuccessListener { list ->
//                            val first = list.firstOrNull()
//                            val raw = first?.rawValue
//
//                            if (!raw.isNullOrEmpty()) {
//                                // ✅ set next allowed time
//                                val cooldown = cooldownMsState
//                                nextAllowedAt = System.currentTimeMillis() + cooldown
//
//                                // ✅ trigger effect + callback on main thread
//                                onScanEffectState?.invoke()
//                                onResultState(
//                                    BarcodeResult(
//                                        raw = raw,
//                                        format = first.format.toString()
//                                    )
//                                )
//                            }
//                        }
//                        .addOnFailureListener { onErrorState(it) }
//                        .addOnCompleteListener { imageProxy.close() }
//                }
//
//                cameraProvider?.unbindAll()
//                cameraProvider?.bindToLifecycle(
//                    lifecycleOwner,
//                    CameraSelector.DEFAULT_BACK_CAMERA,
//                    preview,
//                    analysis
//                )
//            } catch (t: Throwable) {
//                onErrorState(t)
//            }
//        }, mainExecutor)

        onDispose {
            try {
                analysis?.clearAnalyzer()
            } catch (_: Throwable) {
            }
            try {
                cameraProvider?.unbindAll()
            } catch (_: Throwable) {
            }
            try {
                scanner?.close()
            } catch (_: Throwable) {
            }
            try {
                executor.shutdown()
            } catch (_: Throwable) {
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { previewView }
    )
}

private fun ensureMlKitInitialized(context: Context) {
    val app = context.applicationContext
    runCatching { MlKitContext.getInstance() }
}
