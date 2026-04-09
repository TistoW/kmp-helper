package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.tisto.kmp.helper.utils.PlatformType
import kotlinx.coroutines.DisposableHandle

@Composable
expect fun BackHandlerExpect(enabled: Boolean = true, onBack: () -> Unit)

@Stable
class BackDispatcher {
    private val callbacks = mutableStateListOf<() -> Boolean>()

    val canGoBack: Boolean
        get() = callbacks.isNotEmpty()

    val depth: Int
        get() = callbacks.size

    fun register(enabled: Boolean, callback: () -> Boolean): DisposableHandle {
        if (!enabled) return DisposableHandle {}

        callbacks.add(callback)
        return DisposableHandle {
            callbacks.remove(callback)
        }
    }

    fun handleBack(): Boolean {
        // LIFO: last registered gets first chance
        val cb = callbacks.lastOrNull() ?: return false
        return cb()
    }
}

val LocalBackDispatcher = staticCompositionLocalOf<BackDispatcher> {
    error("BackDispatcher not provided. Wrap your app with ProvideBackDispatcher { ... }")
}

/**
 * Wrap your whole app with this.
 */
@Composable
fun ProvideBackDispatcher(content: @Composable () -> Unit) {
    val dispatcher = remember { BackDispatcher() }

    CompositionLocalProvider(LocalBackDispatcher provides dispatcher) {
        PlatformBackDispatcherHost(dispatcher) {
            content()
        }
    }
}

/**
 * Use this in any screen like Android BackHandler.
 */
@Composable
fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
) {

    if (PlatformType.isAndroid) {
        BackHandlerExpect(enabled, onBack)
    } else {
        val dispatcher = LocalBackDispatcher.current
        DisposableEffect(dispatcher, enabled, onBack) {
            val handle = dispatcher.register(enabled) {
                onBack()
                true
            }
            onDispose { handle.dispose() }
        }
    }
}

/**
 * Platform root hook (implemented per platform).
 */
@Composable
internal expect fun PlatformBackDispatcherHost(
    dispatcher: BackDispatcher,
    content: @Composable () -> Unit
)