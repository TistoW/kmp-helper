package com.tisto.kmp.helper.utils.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual fun isMobilePhone() = true

actual class AppExitHandler {
    actual fun exit() {

    }
}

@Composable
actual fun rememberAppExitHandler() = remember { AppExitHandler() }