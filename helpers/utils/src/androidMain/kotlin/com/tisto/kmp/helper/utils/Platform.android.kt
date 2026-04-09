package com.tisto.kmp.helper.utils

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val type: String = "android"
    override val platform: String = "android"
}

actual fun getPlatform(): Platform = AndroidPlatform()