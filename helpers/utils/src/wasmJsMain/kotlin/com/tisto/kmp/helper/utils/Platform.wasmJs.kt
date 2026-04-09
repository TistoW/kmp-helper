package com.tisto.kmp.helper.utils

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val type: String = "wasm"
    override val platform: String = "web"
}

actual fun getPlatform(): Platform = WasmPlatform()