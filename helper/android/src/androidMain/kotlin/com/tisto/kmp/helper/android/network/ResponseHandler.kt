package com.tisto.kmp.helper.android.network

import com.tisto.kmp.helper.android.network.response.BaseResponseRetrofit
import com.tisto.kmp.helper.utils.ext.def
import com.tisto.kmp.helper.utils.ext.getErrorBody
import com.tisto.kmp.helper.utils.ext.logs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import kotlin.let

fun <T, R> apiCallRetrofit(
    apiCall: suspend () -> Response<out BaseResponseRetrofit<T>>,
    onSuccess: ((R?) -> Unit)? = null,
    mapper: (T) -> R
): Flow<ResourceRetrofit<R>> = flow {
    emit(ResourceRetrofit.loading())
    val response = apiCall()
    if (response.isSuccessful) {
        val result = response.body()
        val data = result?.data?.let(mapper)
        onSuccess?.invoke(data)
        emit(
            ResourceRetrofit.success(
                data,
                result?.message ?: "Server Error",
                lastPage = result?.last_page.def(),
                lastSync = result?.lastSync,
                total = result?.total
            )
        )
    } else {
        val errorBody = response.getErrorBody()
        val errorCode = errorBody?.code
        emit(ResourceRetrofit.error(errorBody?.message ?: "Server Error", errorCode))
    }
}.catch { e ->
    logResponse(e.message)
    e.printStackTrace()
    emit(ResourceRetrofit.error(e.message.convertErrorMessage(), null))
}.flowOn(Dispatchers.IO)

fun logResponse(message: String?) {
    logs("executeApi : $message")
}

private fun String?.convertErrorMessage(): String {
    return when (this) {
        "unexpected end of stream" -> "Internal Server Error"
        null -> "Server Error"
        else -> "Server Error"
    }
}