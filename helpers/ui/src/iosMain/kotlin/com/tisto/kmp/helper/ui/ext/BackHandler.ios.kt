package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandlerExpect(enabled: Boolean, onBack: () -> Unit) {
}

@Composable
internal actual fun PlatformBackDispatcherHost(
    dispatcher: BackDispatcher,
    content: @Composable () -> Unit
) {
    content()
}