package com.tisto.kmp

import com.tisto.kmp.helper.kmpHelper

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name.kmpHelper()}!"
    }
}