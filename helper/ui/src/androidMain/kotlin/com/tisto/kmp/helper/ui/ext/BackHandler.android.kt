package com.tisto.kmp.helper.ui.ext

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
internal actual fun PlatformBackDispatcherHost(
    dispatcher: BackDispatcher,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = dispatcher.canGoBack) {
        dispatcher.handleBack()
    }
    content()
}

@Composable
actual fun BackHandlerExpect(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, onBack)
}
