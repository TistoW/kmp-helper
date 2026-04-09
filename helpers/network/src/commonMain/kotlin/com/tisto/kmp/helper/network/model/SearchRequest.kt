package com.tisto.kmp.helper.network.model

import kotlin.collections.toMutableList
import kotlin.text.isNullOrEmpty
import kotlin.toString


data class SearchRequest(
    var advanceQuery: List<Search>? = null,
    var page: Int = 1,
    var perpage: Int? = null,
    var simpleQuery: List<Search>? = null, // for single query,
    var searchName: String? = null, // easy way searchByName
    val orderBy: String? = null, // createdAt | name |etc | default createdAt
    val orderType: String? = null, // DESC | ASC |default DESC,
    val productType: String? = null //"normal" | "service" | "points" | "package"
) {
    init {
        if (!searchName.isNullOrEmpty()) {
            val list = advanceQuery?.toMutableList() ?: ArrayList()
            list.add(Search("name", value = searchName, condition = "like"))
            advanceQuery = list
        }
        if (orderBy != null) {
            val list = simpleQuery?.toMutableList() ?: ArrayList()
            list.add(Search("orderBy", orderBy))
            simpleQuery = list
        }
        if (orderType != null) {
            val list = simpleQuery?.toMutableList() ?: ArrayList()
            list.add(Search("orderType", orderType))
            simpleQuery = list
        }
    }
}

data class Search(
    val column: String,
    val value: String? = null,
    val condition: String = "=", // like | = | !=
    val type: String? = null, // OR | AND
)

fun SearchRequest?.defaultSearch(): SearchRequest {
    val tempSearch = this ?: SearchRequest()
    val list = tempSearch.advanceQuery ?: ArrayList()
    tempSearch.advanceQuery = list
    return tempSearch
}

fun SearchRequest?.convertToQuery(): HashMap<String, String> {
    val tempSearch = this ?: SearchRequest(page = 1)
    tempSearch.advanceQuery = tempSearch.advanceQuery ?: ArrayList()
    tempSearch.simpleQuery = tempSearch.simpleQuery ?: ArrayList()
    val options: HashMap<String, String> = HashMap()
    tempSearch.let { sr ->
        sr.simpleQuery?.let { s ->
            s.forEach {
                if (it.column.isNotEmpty() && !it.value.isNullOrEmpty()) {
                    options[it.column] = it.value
                }
            }
        }
        var index = 0
        options["page"] = sr.page.toString()
        if (sr.perpage != null) {
            options["perpage"] = sr.perpage.toString()
        }

        sr.advanceQuery?.let { s ->
            s.forEach {
                if (it.column.isNotEmpty()) options["search[$index][column]"] = it.column
                if (it.condition.isNotEmpty()) options["search[$index][condition]"] = it.condition
                if (!it.value.isNullOrEmpty()) options["search[$index][value]"] = it.value
                if (!it.type.isNullOrEmpty()) options["search[$index][type]"] = it.type
                index += 1
            }
        }
    }
    return options
}
