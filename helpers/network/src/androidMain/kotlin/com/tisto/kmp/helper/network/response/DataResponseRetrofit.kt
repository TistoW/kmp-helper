package com.tisto.kmp.helper.network.response

data class DataResponseRetrofit<T>(
    override var message: String = "",
    override var last_page: Int = 0,
    var code: String = "",
    override var data: T? = null,
    override val total: Int?,
    override val lastSync: String?
) : BaseResponseRetrofit<T>