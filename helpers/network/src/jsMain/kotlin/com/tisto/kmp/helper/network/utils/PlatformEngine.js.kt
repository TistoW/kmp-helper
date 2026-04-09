package com.tisto.kmp.helper.network.utils

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

actual fun getPlatformEngine(): HttpClientEngineFactory<*> = object : HttpClientEngineFactory<HttpClientEngineConfig> {
    override fun create(block: HttpClientEngineConfig.() -> Unit): HttpClientEngine {
        return Js.create {
            // ✅ Empty config - use defaults (more lenient)
        }
    }
}
