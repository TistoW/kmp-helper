package com.tisto.kmp.helper.network.utils

import com.tisto.kmp.helper.network.model.BaseResponse
import com.tisto.kmp.helper.utils.ext.def
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Generic API call wrapper for Compose Multiplatform (KMP)
 *
 * Eliminates repetitive try-catch logic and provides consistent error handling
 * across all repositories. Compatible with Android, iOS, Desktop, and Web.
 *
 * Features:
 * - Automatic BaseResponse<T> to Result<R> conversion
 * - Built-in mapper pattern support
 * - Pagination metadata preservation
 * - Consistent error handling
 * - Flow-based reactive streams
 * - Optional success callbacks
 * - Platform-agnostic (no Android dependencies)
 *
 * @param T The DTO type from API (e.g., UserData)
 * @param R The domain model type (e.g., User)
 * @param apiCall Suspend function that returns BaseResponse<T>
 * @param mapper Transform function from DTO (T) to domain model (R)
 * @param onSuccess Optional callback invoked with mapped data on success
 *
 * @return Flow<Result<R>> Reactive stream emitting Loading, Success, or Error states
 *
 * Example usage in repository:
 * ```kotlin
 * fun login(email: String, password: String) = apiCall(
 *     apiCall = { authApi.login(email, password) },
 *     mapper = { dto ->
 *         User(
 *             id = dto.id,
 *             email = dto.email,
 *             name = dto.name
 *         )
 *     }
 * )
 * ```
 */
fun <T, R> apiCall(
    apiCall: suspend () -> BaseResponse<T>,
    mapper: (T) -> R,
    onSuccess: ((R?) -> Unit)? = null
): Flow<Resource<R>> = flow {
    // Emit loading state
    emit(Resource.Loading)

    // Execute API call
    val response = apiCall()

    if (response.isSuccess) {
        if (response.data != null) {
            // Map DTO to domain model
            val mappedData = mapper(response.data)
            // Invoke optional success callback
            onSuccess?.invoke(mappedData)
            // Emit success with pagination metadata
            emit(
                Resource.Success(
                    data = mappedData,
                    message = response.message,
                    lastPage = response.lastPage.def(1),
                    currentPage = response.currentPage,
                    total = response.total,
                    perPage = response.perPage,
                    lastSync = response.lastSync
                )
            )
        } else {
            emit(
                Resource.Error(
                    exception = Exception("API Error: ${response.message}"),
                    message = response.message,
                    code = response.code
                )
            )
        }
    } else {
        // Backend returned non-success code
        emit(
            Resource.Error(
                exception = Exception("API Error: ${response.message}"),
                message = response.message,
                code = response.code
            )
        )
    }
}.catch { exception ->
    // Network error, serialization error, or other exception
    val errorMessage = exception.mapToUserFriendlyMessage()

    emit(
        Resource.Error(
            exception = exception,
            message = errorMessage
        )
    )
}

/**
 * Map exceptions to user-friendly error messages
 * Platform-agnostic error handling
 */
private fun Throwable.mapToUserFriendlyMessage(): String {
    return when {
        message?.contains("unexpected end of stream", ignoreCase = true) == true ->
            "Internal Server Error"

        message?.contains("timeout", ignoreCase = true) == true ->
            "Request timeout. Please check your connection."

        message?.contains("Unable to resolve host", ignoreCase = true) == true ||
                message?.contains(
                    "No address associated with hostname",
                    ignoreCase = true
                ) == true ->
            "Network error. Please check your internet connection."

        message?.contains("JSON", ignoreCase = true) == true ||
                message?.contains("Serialization", ignoreCase = true) == true ->
            "Data parsing error. Please try again."

        message != null -> message!!

        else -> "An unexpected error occurred. Please try again."
    }
}
