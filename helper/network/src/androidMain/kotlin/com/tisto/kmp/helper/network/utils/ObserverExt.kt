package com.tisto.kmp.helper.network.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.tisto.kmp.helper.network.response.ErrorResponse

fun <T> LiveData<ResourceRetrofit<T>>.observer(
    lifecycleOwner: LifecycleOwner,
    onError: (ErrorResponse) -> Unit = {},
    onErrorAllResponse: ((ResourceRetrofit<T>) -> Unit) = { },
    onLoading: () -> Unit = { },
    onFinished: () -> Unit = { },
    onSuccessAllResponse: (ResourceRetrofit<T>) -> Unit = {},
    onSuccess: (T) -> Unit = {}
) {
    observe(lifecycleOwner) {
        when (it.state) {
            State.SUCCESS -> {
                onSuccessAllResponse(it)
                if (it.body != null) {
                    onSuccess(it.body)
                } else onError(
                    ErrorResponse(
                        message = "Data is null", errorCode = it.errorCode
                    )
                )
                onFinished()
            }

            State.ERROR -> {
                val error = ErrorResponse(
                    message = it.message, errorCode = it.errorCode
                )
                onError(error)
                onErrorAllResponse.invoke(it)
                onFinished()
            }

            State.LOADING -> {
                onLoading()
            }
        }
    }
}

fun <T> LiveData<Resource<T>>.observer(
    lifecycleOwner: LifecycleOwner,
    onError: (ErrorResponse) -> Unit = {},
    onErrorAllResponse: ((Resource<T>) -> Unit) = { },
    onLoading: () -> Unit = { },
    onFinished: () -> Unit = { },
    onSuccessAllResponse: (Resource.Success<T>) -> Unit = {},
    onSuccessNew: (Resource<T>) -> Unit = {},
    onSuccess: (T) -> Unit = {}
) {
    observe(lifecycleOwner) {

        when (it) {
            is Resource.Loading -> {
                onLoading()
            }

            is Resource.Success -> {
                onSuccessAllResponse(it)
                if (it.data != null) {
                    onSuccess(it.data)
                } else onError(
                    ErrorResponse(
                        message = "Data is null", errorCode = it.code
                    )
                )
                onFinished()
            }

            is Resource.Error -> {
                val error = ErrorResponse(
                    message = it.message, errorCode = it.code
                )
                onError(error)
                onErrorAllResponse.invoke(it)
                onFinished()
            }
        }
    }
}


