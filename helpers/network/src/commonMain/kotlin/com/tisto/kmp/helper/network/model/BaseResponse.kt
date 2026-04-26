package com.tisto.kmp.helper.network.model

import kotlinx.serialization.Serializable

/**
 * Dynamic base response wrapper supporting both object and array data
 *
 * Supports two backend response formats:
 *
 * v4 (api/) — code-based:
 * ```json
 * {
 *   "code": "SUCCESS",
 *   "message": "Hallo, Tisto",
 *   "last_page": 0,
 *   "current_page": 1,
 *   "total": 0,
 *   "per_page": 20,
 *   "lastSync": "2025-12-18 22:43:50",
 *   "data": { ... }
 * }
 * ```
 *
 * v5 (api-new/) — success-based:
 * ```json
 * {
 *   "success": true,
 *   "statusCode": 200,
 *   "message": "Success",
 *   "total": 10,
 *   "limit": 20,
 *   "currentPage": 1,
 *   "totalPages": 1,
 *   "data": { ... }
 * }
 * ```
 */
@Serializable
data class BaseResponse<T>(
    // v4 fields
    val code: String? = null,
    val lastPage: Int? = null,
    val perPage: Int? = null,
    val lastSync: String? = null,
    // v5 fields
    val success: Boolean? = null,
    val statusCode: Int? = null,
    val totalPages: Int? = null,
    val limit: Int? = null,
    // shared fields
    val message: String? = null,
    val data: T? = null,
    val currentPage: Int? = null,
    val total: Int? = null,
) {
    /** true if v4 code=SUCCESS or v5 success=true */
    val isSuccess: Boolean
        get() = success == true || code.equals("SUCCESS", ignoreCase = true)

    val isPaginated: Boolean
        get() = totalPages != null || lastPage != null || currentPage != null || total != null

    /** Resolve last page from v5 totalPages or v4 lastPage */
    val resolvedLastPage: Int
        get() = totalPages ?: lastPage ?: 1

    /** Resolve per-page from v5 limit or v4 perPage */
    val resolvedPerPage: Int?
        get() = limit ?: perPage
}

/**
 * Extension function to map BaseResponse data to another type
 */
inline fun <T, R> BaseResponse<T>.mapData(transform: (T?) -> R): BaseResponse<R> {
    return BaseResponse(
        code = code,
        success = success,
        statusCode = statusCode,
        message = message,
        data = transform(data),
        lastPage = lastPage,
        totalPages = totalPages,
        currentPage = currentPage,
        total = total,
        perPage = perPage,
        limit = limit,
        lastSync = lastSync
    )
}
