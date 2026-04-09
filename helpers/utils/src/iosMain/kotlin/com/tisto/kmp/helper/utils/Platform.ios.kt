package com.tisto.kmp.helper.utils

import platform.UIKit.UIDevice

class IosPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val type: String = "ios"
    override val platform: String = "ios"
}

actual fun getPlatform(): Platform = IosPlatform()