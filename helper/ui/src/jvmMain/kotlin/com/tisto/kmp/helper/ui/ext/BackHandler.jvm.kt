package com.tisto.kmp.helper.ui.ext

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type


@Composable
actual fun BackHandlerExpect(enabled: Boolean, onBack: () -> Unit) {
}

@Composable
internal actual fun PlatformBackDispatcherHost(
    dispatcher: BackDispatcher,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPreviewKeyEvent { e ->
                if (!dispatcher.canGoBack) return@onPreviewKeyEvent false
                if (e.type != KeyEventType.KeyUp) return@onPreviewKeyEvent false

                val isBack =
                    e.key == Key.Escape ||
                            e.key == Key.Backspace // NOTE: may conflict with text input

                if (isBack) {
                    dispatcher.handleBack()
                    true
                } else false
            }
    ) {
        content()
    }
}