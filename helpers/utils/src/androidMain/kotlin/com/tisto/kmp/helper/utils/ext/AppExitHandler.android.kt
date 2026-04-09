package com.tisto.kmp.helper.utils.ext

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

actual fun isMobilePhone() = true

actual class AppExitHandler(private val activity: Activity?) {
    actual fun exit() {
        activity?.finishAffinity()
    }
}

@Composable
actual fun rememberAppExitHandler(): AppExitHandler {
    val context = LocalContext.current
    val activity = context as? Activity
    return remember(activity) {
        AppExitHandler(
            activity
        )
    }
}