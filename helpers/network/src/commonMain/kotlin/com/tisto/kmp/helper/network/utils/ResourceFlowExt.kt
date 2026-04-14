package com.tisto.kmp.helper.network.utils

import kotlinx.coroutines.flow.Flow

/**
 * Terminal operator that unwraps a Flow<Resource<T>> into success/error callbacks.
 *
 * Collapses the repeating pattern:
 *
 *     try {
 *         flow.collect { r ->
 *             when (r) {
 *                 is Resource.Success -> ...
 *                 is Resource.Error   -> ...
 *                 is Resource.Loading -> Unit
 *             }
 *         }
 *     } catch (t: Throwable) { ... }
 *
 * into a single call:
 *
 *     repo.get(search).onResource(
 *         fallbackError = "Gagal memuat data",
 *         onError = ::showError,
 *         onSuccess = { data -> ... },
 *     )
 *
 * Notes:
 *  - Loading is intentionally swallowed. apiCall() still emits it, but most
 *    callers manage their own isLoading/isRefreshing/isSubmitting flag around
 *    the call site, so they don't need to react to the Loading emission.
 *  - apiCall() already converts network/serialization errors into Resource.Error,
 *    so the outer try/catch here mainly guards programmer errors thrown inside
 *    the onSuccess lambda; it's belt-and-suspenders.
 *  - onError defaults to no-op so callers that only care about success can omit it.
 *  - onSuccess receives `T` (non-null) because Resource.Success.data is non-null.
 */
suspend inline fun <T> Flow<Resource<T>>.onResource(
    fallbackError: String = "Terjadi kesalahan",
    crossinline onError: (String) -> Unit = {},
    crossinline onSuccess: (T) -> Unit,
) {
    try {
        collect { r ->
            when (r) {
                is Resource.Success -> onSuccess(r.data)
                is Resource.Error -> onError(r.message ?: fallbackError)
                is Resource.Loading -> Unit
            }
        }
    } catch (t: Throwable) {
        onError(t.message ?: fallbackError)
    }
}
