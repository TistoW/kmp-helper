package com.tisto.kmp.helper.network.expect

interface Platform {
    val name: String
    val type: String
}

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

