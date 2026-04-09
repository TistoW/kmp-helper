package com.tisto.kmp.helper.utils.prefs

import kotlinx.browser.localStorage

/**
 * JS (Web) implementation of PlatformPrefs using LocalStorage
 *
 * This implementation is WRITE-ONCE and STABLE.
 * It never changes when new preferences are added.
 * All preference logic lives in common code (AppPrefs + AppPrefsExt).
 *
 * Data persists in browser's local storage.
 */
actual object PlatformPrefs {
    actual fun get(key: String): String? {
        return localStorage.getItem(key)
    }

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
