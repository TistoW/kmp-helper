package com.tisto.kmp

import com.tisto.kmp.helper.network.helperNetwork

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name.helperNetwork()}!"
    }
}