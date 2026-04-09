package com.tisto.kmp.helper.utils.prefs


/**
 * WasmJS implementation of PlatformPrefs using LocalStorage
 *
 * This implementation is WRITE-ONCE and STABLE.
 * It never changes when new preferences are added.
 * All preference logic lives in common code (AppPrefs + AppPrefsExt).
 *
 * Data persists in browser's local storage.
 */
@OptIn(ExperimentalWasmJsInterop::class)
private val localStorage: LocalStorage =
    js("window.localStorage")

external interface LocalStorage {
    fun getItem(key: String): String?
    fun setItem(key: String, value: String)
    fun removeItem(key: String)
    fun clear()
}


actual object PlatformPrefs {

    actual fun get(key: String): String? =
        localStorage.getItem(key)

    actual fun put(key: String, value: String) {
        localStorage.setItem(key, value)
    }

    actual fun remove(key: String) {
        localStorage.removeItem(key)
    }

    actual fun clear() {
        localStorage.clear()
    }
}
