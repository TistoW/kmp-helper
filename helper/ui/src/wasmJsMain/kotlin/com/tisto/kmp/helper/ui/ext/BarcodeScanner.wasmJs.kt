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

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun(
    """
    () => {
      const AC = window.AudioContext || window.webkitAudioContext;
      if (!AC) return;

      if (!window.__zenentaBeepCtx) {
        window.__zenentaBeepCtx = new AC();
      }
      const ctx = window.__zenentaBeepCtx;

      if (ctx.state === 'suspended') {
        ctx.resume().catch(() => {});
      }

      const osc = ctx.createOscillator();
      const gain = ctx.createGain();

      osc.type = 'sine';
      osc.frequency.value = 880;
      gain.gain.value = 0.06;

      osc.connect(gain);
      gain.connect(ctx.destination);

      osc.start();

      setTimeout(() => {
        try { osc.stop(); } catch(e) {}
        try { osc.disconnect(); } catch(e) {}
        try { gain.disconnect(); } catch(e) {}
      }, 80);
    }
    """
)
private external fun jsBeep()

actual object BeepPlayer {
    actual fun play() {
        runCatching { jsBeep() }
    }
}