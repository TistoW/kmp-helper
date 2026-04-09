package com.tisto.kmp.helper.network.utils

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun getPlatformEngine(): HttpClientEngineFactory<*> = Darwin