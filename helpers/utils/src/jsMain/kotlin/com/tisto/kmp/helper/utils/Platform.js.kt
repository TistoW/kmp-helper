package com.tisto.kmp.helper.utils

class JsPlatform : Platform {
    override val name: String = "WebJs with Kotlin/JS"
    override val type: String = "webJs"
    override val platform: String = "web"
}

actual fun getPlatform(): Platform = JsPlatform()