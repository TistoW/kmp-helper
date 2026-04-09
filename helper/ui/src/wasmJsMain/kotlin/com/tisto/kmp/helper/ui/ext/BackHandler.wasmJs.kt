package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.window
import org.w3c.dom.events.Event
import kotlin.compareTo

@Composable
actual fun BackHandlerExpect(enabled: Boolean, onBack: () -> Unit) {
    LaunchedEffect(enabled) {
        if (enabled) {
            window.onpopstate = { onBack() }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            window.onpopstate = null
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
internal actual fun PlatformBackDispatcherHost(
    dispatcher: BackDispatcher,
    content: @Composable () -> Unit
) {
    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            if (dispatcher.canGoBack) dispatcher.handleBack()
        }

        window.addEventListener("popstate", listener)

        onDispose {
            window.removeEventListener("popstate", listener)
        }
    }

    LaunchedEffect(dispatcher.depth) {
        if (dispatcher.depth > 0) {
            window.history.pushState(null, "", window.location.href)
        }
    }

    content()
}