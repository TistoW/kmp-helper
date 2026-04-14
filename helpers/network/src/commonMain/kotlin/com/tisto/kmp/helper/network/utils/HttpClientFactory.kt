package com.tisto.kmp.helper.network.utils

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

data class HttpClientConfig(
    val baseUrl: String,
    val enableLogging: Boolean = true,
    val tokenProvider: () -> String?,
    val headersProvider: () -> Map<String, String> = { emptyMap() }
)

/**
 * Factory for creating HttpClient with common configuration
 */
object HttpClientFactory {
    const val TIMEOUT_MILLIS = 30_000L
    const val CONNECT_TIMEOUT_MILLIS = 30_000L

    fun create(
        config: HttpClientConfig
    ): HttpClient {
        return HttpClient(getPlatformEngine()) {
            // Base URL
            defaultRequest {
                url(config.baseUrl)

                config.tokenProvider()?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }

                config.headersProvider().forEach { (key, value) ->
                    header(key, value)
                }
            }

            expectSuccess = false

            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = TIMEOUT_MILLIS
                connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
                socketTimeoutMillis = TIMEOUT_MILLIS
            }

            // ✅ Ktor 3.x syntax
            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { cause, _ ->
                    if (cause.message?.contains("Content-Length", ignoreCase = true) == true) {
                        println("⚠️ Content-Length mismatch (ignored)")
                        return@handleResponseExceptionWithRequest
                    }
                    throw cause
                }
            }

            // Content negotiation for JSON
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true // 🔥 INI KUNCINYA
                    coerceInputValues = true // null untuk non-nullable field → pakai default value
                })
            }

            // Logging
            if (config.enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            message.chunked(4000).forEach { chunk ->
                                println("Ktor ➜ $chunk")
                            }
                        }
                    }
                    level = LogLevel.ALL
                }
            }
        }
    }
}

// ✅ Optional: Retry helper (kalau mau dipake)
suspend fun <T> retryOnContentLengthMismatch(
    maxRetries: Int = 3,
    block: suspend () -> T
): T {
    repeat(maxRetries) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            if (e.message?.contains("Content-Length") == true && attempt < maxRetries - 1) {
                delay(500L * (attempt + 1))
                // Retry
            } else {
                throw e
            }
        }
    }
    throw Exception("Max retries exceeded")
}

