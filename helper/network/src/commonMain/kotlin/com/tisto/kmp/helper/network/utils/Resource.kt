package com.tisto.kmp.helper.network.utils

/**
 * A generic class that holds a value with its loading status.
 * Enhanced with pagination metadata support for list responses.
 * @param <T>
 */
sealed class Resource<out T> {
    data class Success<out T>(
        val data: T,
        val message: String? = null,
        val code: String? = null,
        val lastPage: Int = 1,
        val currentPage: Int? = null,
        val total: Int? = null,
        val perPage: Int? = null,
        val lastSync: String? = null
    ) : Resource<T>() {
        val isPaginated: Boolean get() = currentPage != null || total != null
    }

    data class Error(
        val exception: Throwable,
        val message: String? = exception.message,
        val code: String? = null
    ) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun exceptionOrNull(): Throwable? = when (this) {
        is Error -> exception
        else -> null
    }
}

/**
 * Transform Success data
 */
inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Error -> Resource.Error(exception, message)
        is Resource.Loading -> Resource.Loading
    }
}

/**
 * Handle Success case
 */
inline fun <T> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

/**
 * Handle Error case
 */
inline fun <T> Resource<T>.onError(action: (Throwable, String?) -> Unit): Resource<T> {
    if (this is Resource.Error) action(exception, message)
    return this
}

/**
 * Handle Loading case
 */
inline fun <T> Resource<T>.onLoading(action: () -> Unit): Resource<T> {
    if (this is Resource.Loading) action()
    return this
}
