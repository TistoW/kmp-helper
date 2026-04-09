package com.tisto.kmp.helper.network.utils

import io.ktor.client.engine.HttpClientEngineFactory

/**
 * Get platform-specific HTTP engine
 * This is implemented in each platform's source set
 */
expect fun getPlatformEngine(): HttpClientEngineFactory<*>