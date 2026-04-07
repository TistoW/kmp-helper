package com.tisto.kmp.helper.network.utils
import com.tisto.kmp.helper.utils.ext.def

data class ResourceRetrofit<out T>(
    val state: State,
    val body: T? = null,
    val message: String = "Server Error",
    val errorCode: String? = null,
    val total: Int = 0,
    val lastPage: Int = 0,
    val lastSync: String? = null
) {

    companion object {

        fun <T> success(
            data: T?,
            message: String = "Server Error",
            lastPage: Int = 1,
            total: Int? = 0,
            lastSync: String? = null
        ): ResourceRetrofit<T> {
            return ResourceRetrofit(
                state = State.SUCCESS,
                body = data,
                message = message,
                lastPage = lastPage,
                total = total.def(),
                lastSync = lastSync
            )
        }

        fun <T> error(msg: String?, errorCode: String?): ResourceRetrofit<T> {
            return ResourceRetrofit(
                state = State.ERROR,
                message = msg ?: "Server Error",
                errorCode = errorCode ?: "ERROR"
            )
        }

        fun <T> loading(): ResourceRetrofit<T> {
            return ResourceRetrofit(State.LOADING, body = null)
        }

    }
}