package com.tisto.kmp.helper.utils

interface Platform {
    val name: String
    val type: String
    val platform: String
}

expect fun getPlatform(): Platform

fun platform(): Platform {
    return getPlatform()
}

object PlatformType {
    val isWasm: Boolean
        get() = platform().type == "wasm"
    val isJs: Boolean
        get() = platform().type == "webJs" || platform().type == "WebJs"
    val isWeb: Boolean
        get() = isWasm || isJs
    val isJvm: Boolean
        get() = platform().type == "windows"
    val isAndroid: Boolean
        get() = platform().type == "android"
    val isIos: Boolean
        get() = platform().type == "ios"
    val platformName: String
        get() = platform().name
}

val isWasm: Boolean
    get() = platform().type == "wasm"
val isJs: Boolean
    get() = platform().type == "webJs"
val isWeb: Boolean
    get() = isWasm || isJs
val isJvm: Boolean
    get() = platform().type == "windows"
val isAndroid: Boolean
    get() = platform().type == "android"
val isIos: Boolean
    get() = platform().type == "ios"
val platformName: String
    get() = platform().name