package com.tisto.kmp.helper.utils.prefs

import kotlinx.serialization.json.Json

inline fun <reified T> PrefManager.setObject(key: String, value: T?) {
    val json = value?.let { Json.encodeToString(it) }
    putString(key, json)
}

inline fun <reified T> PrefManager.getObject(key: String): T? {
    return getString(key)?.let {
        runCatching {
            Json.decodeFromString<T>(it)
        }.getOrNull()
    }
}

//@Deprecated(
//    message = "Use PrefManager.user property instead",
//    replaceWith = ReplaceWith("user = user"),
//    level = DeprecationLevel.WARNING
//)
//fun PrefManager.setUser(user: User?) {
//    setObject(PrefKeys.USER_JSON, user)
//}
//
//@Deprecated(
//    message = "Use PrefManager.user property instead",
//    replaceWith = ReplaceWith("user"),
//    level = DeprecationLevel.WARNING
//)
//fun PrefManager.getUser(): User? {
//    return getObject<User>(PrefKeys.USER_JSON)
//}
