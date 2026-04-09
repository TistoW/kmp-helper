package com.tisto.kmp.helper.utils


class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val type: String = "windows"
    override val platform: String = "windows"
}

actual fun getPlatform(): Platform = JVMPlatform()