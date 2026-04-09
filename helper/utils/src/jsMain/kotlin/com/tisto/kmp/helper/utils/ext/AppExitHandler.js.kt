package com.tisto.kmp.helper.utils.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual fun isMobilePhone() = false

// ========== jsMain/AppExit.js.kt ==========
actual class AppExitHandler {
    actual fun exit() {
        kotlinx.browser.window.close()
    }
}

@Composable
actual fun rememberAppExitHandler(): AppExitHandler {
    return remember { AppExitHandler() }
}