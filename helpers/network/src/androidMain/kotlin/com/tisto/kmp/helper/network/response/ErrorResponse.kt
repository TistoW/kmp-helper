package com.tisto.kmp.helper.network.response

data class ErrorResponse(
    val code: String? = null,
    val message: String? = null,
    val param: String? = null,
    val error: Error? = null,
    val errorCode: String? = null,
) {
    data class Error(
//        val error: NestedError? = null,
        val message: String? = null
    )

    data class NestedError(
        val error: String? = null,
        val message: String? = null,
        val error_code: Int? = null,
    )
}