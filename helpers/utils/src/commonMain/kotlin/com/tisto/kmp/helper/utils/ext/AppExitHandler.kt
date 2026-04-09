package com.tisto.kmp.helper.utils.ext

import androidx.compose.runtime.Composable
import com.tisto.kmp.helper.utils.ext.AppExitHandler

expect fun isMobilePhone(): Boolean // for real device android

expect class AppExitHandler {
    fun exit()
}

@Composable
expect fun rememberAppExitHandler(): AppExitHandler