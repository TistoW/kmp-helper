package com.tisto.kmp.helper.network.expect

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val type: String = "android"
}

fun getPlatform(): Platform = AndroidPlatform()