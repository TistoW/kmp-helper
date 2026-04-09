package com.tisto.kmp.helper.ui.ext


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.utils.ext.logs
import kotlinx.coroutines.delay

data class BarcodeResult(
    val raw: String,
    val format: String? = null,
)

@Composable
fun BarcodeScannerView(
    modifier: Modifier = Modifier,
    delayMs: Long = 1500L,
    enableSuccessEffect: Boolean = true,
    playBeep: Boolean = true,
    isPreview: Boolean = false,
    guideMode: ScanGuideMode = ScanGuideMode.QR, // ✅ baru
    onResult: (BarcodeResult) -> Unit
) {
    var errorText by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            delay(300)
            showSuccess = false
        }
    }

    Box(
        modifier = modifier
            .background(Colors.Black)
            .clipToBounds()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            } // ✅ penting utk BlendMode.Clear
    ) {

        if (!isPreview) {
            PlatformCameraScanner(
                modifier = Modifier.fillMaxSize(),
                scanCooldownMs = delayMs,
                onResult = onResult,
                onScanEffect = {
                    if (enableSuccessEffect) showSuccess = true
                    if (playBeep) BeepPlayer.play()
                },
                onError = { errorText = it.message ?: "Camera error" }
            )
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Preview", color = Colors.White)
            }
        }

        // ✅ guide overlay (QR / BARCODE)
        ScannerGuideOverlay(
            mode = guideMode,
            modifier = Modifier
                .fillMaxSize(0.8f)
                .align(Alignment.Center)
        )

        // ✅ success overlay
        if (showSuccess) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Colors.Black.copy(alpha = 0.25f))
            )
        }

        // Error
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(Spacing.normal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (errorText != null) {
                Surface(
                    color = Colors.Red,
                    tonalElevation = 2.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = errorText!!,
                        modifier = Modifier.padding(12.dp),
                        color = Colors.White,
                        style = TextAppearance.body2()
                    )
                }
                Spacer(Modifier.height(Spacing.small))
            }
        }
    }
}


enum class ScanGuideMode { QR, BARCODE }

@Composable
fun ScannerGuideOverlay(
    modifier: Modifier = Modifier,
    mode: ScanGuideMode = ScanGuideMode.QR,
    strokeWidth: Dp = 4.dp,
    cornerLength: Dp = 26.dp,
    alpha: Float = 1f,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // frame size
        val frameW = when (mode) {
            ScanGuideMode.QR -> w * 0.62f
            ScanGuideMode.BARCODE -> w * 0.80f
        }
        val frameH = when (mode) {
            ScanGuideMode.QR -> frameW
            ScanGuideMode.BARCODE -> h * 0.22f
        }

        val left = (w - frameW) / 2f
        val top = (h - frameH) / 2f
        val right = left + frameW
        val bottom = top + frameH

        val sw = strokeWidth.toPx()
        val cl = cornerLength.toPx()

        fun line(x1: Float, y1: Float, x2: Float, y2: Float) {
            drawLine(
                color = Colors.White.copy(alpha = alpha),
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = sw,
                cap = StrokeCap.Round
            )
        }

        // top-left
        line(left, top, left + cl, top)
        line(left, top, left, top + cl)

        // top-right
        line(right, top, right - cl, top)
        line(right, top, right, top + cl)

        // bottom-left
        line(left, bottom, left + cl, bottom)
        line(left, bottom, left, bottom - cl)

        // bottom-right
        line(right, bottom, right - cl, bottom)
        line(right, bottom, right, bottom - cl)
    }
}



@TabletPreview
@Composable
fun AttendancePreview() {
    HelperTheme {
        BarcodeScannerView(
            modifier = Modifier.fillMaxSize(),
            isPreview = true,
            onResult = { res ->
                logs("BarcodeScannerView: ${res.raw}")
//                        onScanned(res.raw)
//                onBack()
            }
        )
    }
}

expect object BeepPlayer {
    fun play()
}

@Composable
internal expect fun PlatformCameraScanner(
    modifier: Modifier = Modifier,
    scanCooldownMs: Long = 1000L,
    onResult: (BarcodeResult) -> Unit,
    onScanEffect: (() -> Unit)? = null,
    onError: (Throwable) -> Unit,
)
