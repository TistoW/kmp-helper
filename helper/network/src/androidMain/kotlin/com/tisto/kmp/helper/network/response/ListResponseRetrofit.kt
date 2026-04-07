package com.tisto.kmp.helper.network.response

data class ListResponseRetrofit<T>(
    override var message: String = "",
    var code: String = "",
    val current_page: Int = 0,
    val first_page_url: String? = null,
    val from: Int? = null,
    override var last_page: Int = 0,
    val last_page_url: String? = null,
    val next_page_url: String? = null,
    val path: String? = null,
    val per_page: Int? = null,
    val prev_page_url: String? = null,
    val to: Int? = null,
    override var data: List<T> = arrayListOf(),
    val meta: Meta = Meta(),
    override val total: Int?,
    override val lastSync: String?,
) : BaseResponseRetrofit<List<T>> {
    data class Meta(
        val offset: Int = 0,
        val page: Int = 0,
        val per_page: Int = 0,
        val total: Int = 0,
        val total_page: Int = 0,
    )
}