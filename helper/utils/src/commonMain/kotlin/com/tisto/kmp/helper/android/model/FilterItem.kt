package com.tisto.kmp.helper.android.model

const val defaultOrderType = "ASC"
const val defaultOrderBy = "name"

data class FilterItem(
    var title: String = "Nama: A-Z",
    var value: String = defaultOrderType, // nilai, misal "asc", "desc", "active", dll
    var key: String = defaultOrderBy, // key atau field yang di filter, misal "name", "createdAt", dll
    var type: String = FilterType.SORT   // bisa SORT atau FILTER
)

object FilterType {
    const val FILTER = "filter"
    const val SORT = "sort"
}

data class FilterGroup(
    var title: String = "Urutkan",        // untuk header di UI
    var type: String = FilterType.SORT,   // tipe grup
    var options: List<FilterItem> = listOf(), // daftar pilihan
    var selected: FilterItem? = null      // pilihan yang sedang dipilih
)
