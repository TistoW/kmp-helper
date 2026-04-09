package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.awt.GraphicsEnvironment
import java.awt.Toolkit

@Composable
internal actual fun PlatformCameraScanner(
    modifier: Modifier,
    scanCooldownMs: Long,
    onResult: (BarcodeResult) -> Unit,
    onScanEffect: (() -> Unit)?,
    onError: (Throwable) -> Unit
) {
}


actual object BeepPlayer {
    actual fun play() {
        runCatching {
            if (!GraphicsEnvironment.isHeadless()) {
                Toolkit.getDefaultToolkit().beep()
            }
        }
    }
}