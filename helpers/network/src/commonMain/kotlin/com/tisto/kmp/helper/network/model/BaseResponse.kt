package com.tisto.kmp.helper.network.model

import kotlinx.serialization.Serializable

/**
 * Dynamic base response wrapper supporting both object and array data
 *
 * Supports two backend response variants:
 *
 * 1. Object response:
 * ```json
 * {
 *   "code": "SUCCESS",
 *   "message": "Hallo, Tisto",
 *   "data": { ... }
 * }
 * ```
 *
 * 2. Array response with pagination:
 * ```json
 * {
 *   "code": "SUCCESS",
 *   "message": "Success",
 *   "last_page": 0,
 *   "current_page": 1,
 *   "total": 0,
 *   "per_page": 20,
 *   "lastSync": "2025-12-18 22:43:50",
 *   "data": []
 * }
 * ```
 */
@Serializable
data class BaseResponse<T>(
    val code: String,
    val message: String,
    val success: Boolean? = true,
    val statusCode: Int? = 200,
    val data: T? = null,
    val lastPage: Int? = null,
    val currentPage: Int? = null,
    val total: Int? = null,
    val perPage: Int? = null,
    val lastSync: String? = null
) {
    val isSuccess: Boolean
        get() = code.equals("SUCCESS", ignoreCase = true)

    val isPaginated: Boolean
        get() = lastPage != null || currentPage != null || total != null
}

/**
 * Extension function to map BaseResponse data to another type
 */
inline fun <T, R> BaseResponse<T>.mapData(transform: (T?) -> R): BaseResponse<R> {
    return BaseResponse(
        code = code,
        message = message,
        data = transform(data),
        lastPage = lastPage,
        currentPage = currentPage,
        total = total,
        perPage = perPage,
        lastSync = lastSync
    )
}
