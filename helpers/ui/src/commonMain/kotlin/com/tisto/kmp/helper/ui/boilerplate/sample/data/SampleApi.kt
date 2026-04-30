package com.tisto.kmp.helper.ui.boilerplate.sample.data

import com.tisto.kmp.helper.network.model.BaseResponse
import com.tisto.kmp.helper.network.model.SearchRequest
import com.tisto.kmp.helper.network.model.convertToQuery
import com.tisto.kmp.helper.network.utils.deleteMethod
import com.tisto.kmp.helper.network.utils.getMethod
import com.tisto.kmp.helper.network.utils.postMethod
import com.tisto.kmp.helper.network.utils.putMethod
import com.tisto.kmp.helper.ui.boilerplate.sample.data.model.Sample
import com.tisto.kmp.helper.ui.boilerplate.sample.data.request.SampleRequest
import io.ktor.client.HttpClient

// ══════════════════════════════════════════════════════════════════════════
// API SERVICE — Ktor HttpClient, bukan Retrofit.
// Gunakan helper-network extensions: postMethod, getMethod, putMethod, deleteMethod.
// Semua method return BaseResponse<T> mentah — TIDAK unwrap di layer Api.
// URL prefix: $v2 dari Constants.
// ══════════════════════════════════════════════════════════════════════════

internal class SampleApi(private val client: HttpClient) {

    // Ganti "/v2/sample" dengan URL endpoint yang sesuai, misal "$v2/sample"
    // $v2 berasal dari com.app_package.core.utils.constants.Constants

    suspend fun create(body: SampleRequest?): BaseResponse<Sample> = client.postMethod(
        url = "/v2/sample",
        body = body,
        pickedImage = body?.pickedImage,
    )

    suspend fun get(search: SearchRequest? = null): BaseResponse<List<Sample>> = client.getMethod(
        url = "/v2/sample",
        query = search?.convertToQuery(),
    )

    suspend fun getOne(id: String): BaseResponse<Sample> = client.getMethod(
        url = "/v2/sample/$id",
    )

    suspend fun update(body: SampleRequest?): BaseResponse<Sample> = client.putMethod(
        url = "/v2/sample/${body?.id}",
        body = body,
        pickedImage = body?.pickedImage,
    )

    suspend fun delete(id: String): BaseResponse<Sample> = client.deleteMethod(
        url = "/v2/sample/$id",
    )
}
