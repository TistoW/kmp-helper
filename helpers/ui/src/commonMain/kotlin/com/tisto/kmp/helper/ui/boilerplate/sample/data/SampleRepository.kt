package com.tisto.kmp.helper.ui.boilerplate.sample.data

import com.tisto.kmp.helper.network.model.SearchRequest
import com.tisto.kmp.helper.network.utils.apiCall
import com.tisto.kmp.helper.ui.boilerplate.sample.data.request.SampleRequest

// ══════════════════════════════════════════════════════════════════════════
// REPOSITORY — Concrete class, tanpa interface, tanpa UseCase layer.
// Setiap method: apiCall(apiCall = { ... }, mapper = { it }).
// Return type: Flow<Resource<T>>. Bukan suspend fun T. Bukan LiveData.
// Tanpa try/catch. Tanpa caching. Tanpa Flow orchestration.
// ══════════════════════════════════════════════════════════════════════════

internal class SampleRepository(private val api: SampleApi) {

    fun create(body: SampleRequest?) = apiCall(apiCall = { api.create(body) }, mapper = { it })

    fun get(search: SearchRequest? = null) = apiCall(apiCall = { api.get(search) }, mapper = { it })

    fun getOne(id: String) = apiCall(apiCall = { api.getOne(id) }, mapper = { it })

    fun update(body: SampleRequest?) = apiCall(apiCall = { api.update(body) }, mapper = { it })

    fun delete(id: String) = apiCall(apiCall = { api.delete(id) }, mapper = { it })
}
