package com.tisto.kmp.helper.utils.prefs

import android.content.Context
import android.content.SharedPreferences


/**
 * Android implementation of PlatformPrefs using SharedPreferences
 *
 * This implementation is WRITE-ONCE and STABLE.
 * It never changes when new preferences are added.
 * All preference logic lives in common code (AppPrefs + AppPrefsExt).
 *
 * Thread-safe through synchronized access.
 */
actual object PlatformPrefs {
    private const val PREFS_NAME = "app_preferences"

    private var preferences: SharedPreferences? = null

    /**
     * Initialize preferences with application context
     * Must be called before any other operations
     *
     * Call this in your Application class:
     * ```
     * class MyApplication : Application() {
     *     override fun onCreate() {
     *         super.onCreate()
     *         PlatformPrefs.init(this)
     *     }
     * }
     * ```
     */
    fun init(context: Context) {
        if (preferences == null) {
            preferences = context.applicationContext.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
        }
    }

    private fun getPrefs(): SharedPreferences {
        return preferences ?: error(
            "PlatformPrefs not initialized. Call PlatformPrefs.init(context) first."
        )
    }

    actual fun get(key: String): String? {
        return getPrefs().getString(key, null)
    }

    actual fun put(key: String, value: String) {
        getPrefs().edit().putString(key, value).apply()
    }

    actual fun remove(key: String) {
        getPrefs().edit().remove(key).apply()
    }

    actual fun clear() {
        getPrefs().edit().clear().apply()
    }
}
