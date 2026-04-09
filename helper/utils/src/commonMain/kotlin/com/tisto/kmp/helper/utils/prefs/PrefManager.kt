package com.tisto.kmp.helper.utils.prefs

/**
 * Kotpref-like preferences system for Compose Multiplatform
 *
 * This object provides a Kotpref-style preference system using property delegates:
 * - Platform implementations are write-once and stable
 * - Adding new preferences ONLY requires editing this ONE file
 * - NO platform code changes needed when adding preferences
 * - Type-safe delegates (boolPref, stringPref, intPref, jsonPref, etc.)
 * - Works on Android, iOS, Desktop, Web, WASM
 *
 * Usage:
 * ```
 * Prefs.isLogin = true
 * Prefs.token = "abc123"
 * Prefs.user = User(...)
 * val loggedIn = Prefs.isLogin
 * Prefs.clear()
 * ```
 *
 * To add a new preference:
 * 1. Add a property with delegate to this object (see examples below)
 * 2. Done! No platform changes needed.
 *
 * Example:
 * ```
 * var themeMode by stringPref("themeMode", "light")
 * ```
 */
object PrefManager {

    /**
     * Current storage backend
     * Defaults to PlatformStorage (which uses PlatformPrefs)
     * Can be replaced with custom storage for testing or encryption
     */
    internal var storage: AppStorage = PlatformStorage

    /**
     * Initialize Prefs with a custom storage backend (optional)
     *
     * By default, Prefs uses PlatformPrefs - no initialization needed!
     * This method is only for advanced use cases like testing or encryption.
     *
     * Usage:
     * ```
     * // For testing
     * Prefs.init(InMemoryStorage())
     *
     * // For encryption (custom implementation)
     * Prefs.init(EncryptedStorage())
     *
     * // Reset to default
     * Prefs.init(PlatformStorage)
     * ```
     *
     * @param customStorage The storage implementation to use
     */
    fun init(customStorage: AppStorage) {
        storage = customStorage
    }

    // ========== Add your new preferences here! ==========
    // Just add a line like:
    // var themeMode by stringPref("themeMode", "light")
    // var language by stringPref("language", "en")
    // var notificationsEnabled by boolPref("notificationsEnabled", true)
    // var maxRetries by intPref("maxRetries", 3)
    // NO platform changes needed!

    // ========== Low-level API (for advanced use cases) ==========

    /**
     * Get a string value by key
     * @param key The preference key
     * @return The stored value, or null if not found
     */
    fun getString(key: String): String? =
        storage.get(key)

    /**
     * Store a string value
     * @param key The preference key
     * @param value The value to store, or null to remove
     */
    fun putString(key: String, value: String?) {
        if (value == null) {
            storage.remove(key)
        } else {
            storage.set(key, value)
        }
    }

    /**
     * Get a boolean value by key
     * @param key The preference key
     * @param default Default value if key not found
     * @return The stored boolean value
     */
    fun getBoolean(key: String, default: Boolean = false): Boolean =
        storage.get(key)?.toBoolean() ?: default

    /**
     * Store a boolean value
     * @param key The preference key
     * @param value The value to store
     */
    fun putBoolean(key: String, value: Boolean) {
        storage.set(key, value.toString())
    }

    /**
     * Get an integer value by key
     * @param key The preference key
     * @param default Default value if key not found
     * @return The stored integer value
     */
    fun getInt(key: String, default: Int = 0): Int =
        storage.get(key)?.toIntOrNull() ?: default

    /**
     * Store an integer value
     * @param key The preference key
     * @param value The value to store
     */
    fun putInt(key: String, value: Int) {
        storage.set(key, value.toString())
    }

    /**
     * Get a long value by key
     * @param key The preference key
     * @param default Default value if key not found
     * @return The stored long value
     */
    fun getLong(key: String, default: Long = 0L): Long =
        storage.get(key)?.toLongOrNull() ?: default

    /**
     * Store a long value
     * @param key The preference key
     * @param value The value to store
     */
    fun putLong(key: String, value: Long) {
        storage.set(key, value.toString())
    }

    /**
     * Get a float value by key
     * @param key The preference key
     * @param default Default value if key not found
     * @return The stored float value
     */
    fun getFloat(key: String, default: Float = 0f): Float =
        storage.get(key)?.toFloatOrNull() ?: default

    /**
     * Store a float value
     * @param key The preference key
     * @param value The value to store
     */
    fun putFloat(key: String, value: Float) {
        storage.set(key, value.toString())
    }

    /**
     * Remove a preference by key
     * @param key The preference key to remove
     */
    fun remove(key: String) {
        storage.remove(key)
    }

    /**
     * Clear all stored preferences
     */
    fun clear() {
        storage.clearAll()
    }
}