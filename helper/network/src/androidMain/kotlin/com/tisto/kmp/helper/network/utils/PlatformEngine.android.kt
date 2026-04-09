package com.tisto.kmp.helper.network.utils

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual fun getPlatformEngine(): HttpClientEngineFactory<*> = OkHttp