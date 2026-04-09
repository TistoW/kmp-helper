package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformCameraScanner(
    modifier: Modifier,
    scanCooldownMs: Long,
    onResult: (BarcodeResult) -> Unit,
    onScanEffect: (() -> Unit)?,
    onError: (Throwable) -> Unit,
) {
}

@Suppress("UnsafeCastFromDynamic", "UNCHECKED_CAST")
actual object BeepPlayer {

    private var audioCtx: dynamic = null

    actual fun play() {
        runCatching {
            val ctx = audioCtx ?: run {
                val c = js("new (window.AudioContext || window.webkitAudioContext)()")
                audioCtx = c
                c
            }

            // Some browsers suspend audio until user gesture; resume safely
            runCatching { ctx.resume() }

            val osc = ctx.createOscillator()
            val gain = ctx.createGain()

            osc.type = "sine"
            osc.frequency.value = 880 // Hz
            gain.gain.value = 0.05    // volume

            osc.connect(gain)
            gain.connect(ctx.destination)

            osc.start()

            // stop after 80ms
            js("setTimeout")( {
                runCatching { osc.stop() }
                runCatching { osc.disconnect() }
                runCatching { gain.disconnect() }
            }, 80)
        }
    }
}
