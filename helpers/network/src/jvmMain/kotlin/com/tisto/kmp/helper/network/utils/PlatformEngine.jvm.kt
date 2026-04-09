package com.tisto.kmp.helper.network.utils

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun getPlatformEngine(): HttpClientEngineFactory<*> = CIO