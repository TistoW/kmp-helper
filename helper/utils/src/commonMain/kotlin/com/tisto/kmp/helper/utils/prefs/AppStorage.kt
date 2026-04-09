package com.tisto.kmp.helper.utils.prefs

/**
 * Pluggable storage abstraction for advanced use cases
 *
 * By default, Prefs uses PlatformPrefs as its storage backend.
 * This interface allows injecting custom storage implementations for:
 * - Unit testing with mock storage
 * - Encrypted storage
 * - In-memory storage
 * - Custom persistence mechanisms
 *
 * Usage (optional):
 * ```
 * // For testing
 * Prefs.init(InMemoryStorage())
 *
 * // For encryption
 * Prefs.init(EncryptedStorage())
 *
 * // Default (uses PlatformPrefs)
 * // No init needed - works out of the box!
 * ```
 */
interface AppStorage {
    /**
     * Get a string value by key
     * @param key The storage key
     * @return The stored value, or null if not found
     */
    fun get(key: String): String?

    /**
     * Store a string value
     * @param key The storage key
     * @param value The value to store
     */
    fun set(key: String, value: String)

    /**
     * Remove a value by key
     * @param key The storage key to remove
     */
    fun remove(key: String)

    /**
     * Clear all stored values
     */
    fun clearAll()
}

/**
 * Default storage implementation backed by PlatformPrefs
 *
 * This is the default storage used by Prefs when no custom storage is provided.
 * It delegates all operations to the platform-specific PlatformPrefs implementation.
 */
object PlatformStorage : AppStorage {
    override fun get(key: String): String? = PlatformPrefs.get(key)

    override fun set(key: String, value: String) = PlatformPrefs.put(key, value)

    override fun remove(key: String) = PlatformPrefs.remove(key)

    override fun clearAll() = PlatformPrefs.clear()
}

/**
 * In-memory storage implementation for testing
 *
 * Usage:
 * ```
 * @Test
 * fun testPreferences() {
 *     Prefs.init(InMemoryStorage())
 *     Prefs.isLogin = true
 *     assertEquals(true, Prefs.isLogin)
 *     Prefs.clear()
 * }
 * ```
 */
class InMemoryStorage : AppStorage {
    private val storage = mutableMapOf<String, String>()

    override fun get(key: String): String? = storage[key]

    override fun set(key: String, value: String) {
        storage[key] = value
    }

    override fun remove(key: String) {
        storage.remove(key)
    }

    override fun clearAll() {
        storage.clear()
    }
}
