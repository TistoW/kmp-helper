package com.tisto.kmp.helper.android.network.response

interface BaseResponseRetrofit<T> {
    val data: T?
    val message: String?
    val last_page: Int?
    val total: Int?
    val lastSync: String?
}
