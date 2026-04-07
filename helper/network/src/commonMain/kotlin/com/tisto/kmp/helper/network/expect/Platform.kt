package com.tisto.kmp.helper.network.expect

expect fun PlatformTypeExpect(): String

interface Platform {
    val name: String
    val type: String
}

class AndroidPlatform : Platform {
    override val name: String = "Android"
    override val type: String = "android"
}

fun getPlatform(): Platform = AndroidPlatform()

object PlatformType {
    val isWasm: Boolean
        get() = getPlatform().type == "wasm"
    val isJs: Boolean
        get() = getPlatform().type == "webJs" || getPlatform().type == "WebJs"
    val isWeb: Boolean
        get() = isWasm || isJs
    val isJvm: Boolean
        get() = getPlatform().type == "windows"
    val isAndroid: Boolean
        get() = getPlatform().type == "android"
    val isIos: Boolean
        get() = getPlatform().type == "ios"
    val platformName: String
        get() = getPlatform().name
}

val isWasm: Boolean
    get() = getPlatform().type == "wasm"
val isJs: Boolean
    get() = getPlatform().type == "webJs"
val isJvm: Boolean
    get() = getPlatform().type == "windows"
val isAndroid: Boolean
    get() = getPlatform().type == "android"
val isIos: Boolean
    get() = getPlatform().type == "ios"
val platformName: String
    get() = getPlatform().name

