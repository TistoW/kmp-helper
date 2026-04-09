package com.tisto.kmp.helper.utils.prefs

/**
 * Platform-specific preferences storage interface
 *
 * This is the ONLY interface that platform implementations need to provide.
 * Platform implementations are write-once and stable - they never change
 * when new preferences are added.
 *
 * All preference logic lives in common code via AppPrefs.
 */
expect object PlatformPrefs {
    /**
     * Get a string value by key
     * @param key The preference key
     * @return The stored value, or null if not found
     */
    fun get(key: String): String?

    /**
     * Store a string value
     * @param key The preference key
     * @param value The value to store
     */
    fun put(key: String, value: String)

    /**
     * Remove a preference by key
     * @param key The preference key to remove
     */
    fun remove(key: String)

    /**
     * Clear all stored preferences
     */
    fun clear()
}
