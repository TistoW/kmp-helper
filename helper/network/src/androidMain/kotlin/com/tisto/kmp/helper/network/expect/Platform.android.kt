package com.tisto.kmp.helper.network.expect

import android.os.Build

class AndroidPlatforms : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val type: String = "android"
}

actual fun PlatformTypeExpect(): String {
    return "Test Saja"
}