package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.window
import org.w3c.dom.events.EventListener

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

@Composable
internal actual fun PlatformBackDispatcherHost(
    dispatcher: BackDispatcher,
    content: @Composable () -> Unit
) {
    // Listen to browser back/forward
    DisposableEffect(Unit) {
        val listener = EventListener {
            if (dispatcher.canGoBack) {
                dispatcher.handleBack()
            }
        }
        window.addEventListener("popstate", listener)
        onDispose { window.removeEventListener("popstate", listener) }
    }

    // Add a history entry when back stack grows, so browser back triggers popstate.
    // This makes browser back work even in an SPA without routing.
    LaunchedEffect(dispatcher.depth) {
        if (dispatcher.depth > 0) {
            window.history.pushState(data = null, title = "", url = window.location.href)
        }
    }

    content()
}