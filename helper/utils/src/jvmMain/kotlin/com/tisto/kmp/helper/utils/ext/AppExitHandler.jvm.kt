package com.tisto.kmp.helper.utils.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.system.exitProcess

actual fun isMobilePhone() = false

actual class AppExitHandler {
    actual fun exit() {
        exitProcess(0)
    }
}

@Composable
actual fun rememberAppExitHandler() = remember { AppExitHandler() }