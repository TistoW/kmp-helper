package com.tisto.kmp.helper.network.utils

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

actual fun getPlatformEngine(): HttpClientEngineFactory<*> = Js