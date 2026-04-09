package com.tisto.kmp.helper.utils.prefs

import kotlinx.serialization.json.Json
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Kotpref-like delegate factories for type-safe preference access
 *
 * These delegates provide a clean, ergonomic API similar to Kotpref:
 * ```
 * object _root_ide_package_.com.tisto.helper.core.helper.utils.prefs.PrefManager {
 *     var isLogin by boolPref("isLogin", false)
 *     var token by stringPref("token", "")
 *     var userId by nullableStringPref("userId")
 *     var themeMode by stringPref("themeMode", "light")
 *     var user by jsonPref<User>("user")
 * }
 * ```
 *
 * Adding a new preference is as simple as adding a new line - NO platform code changes needed!
 */

/**
 * Creates a non-null String preference delegate
 *
 * @param key Preference key
 * @param default Default value when key is not set
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var token by stringPref("token", "")
 * ```
 */
fun stringPref(key: String, default: String): ReadWriteProperty<Any?, String> =
    object : ReadWriteProperty<Any?, String> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return PrefManager.getString(key) ?: default
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            PrefManager.putString(key, value)
        }
    }

/**
 * Creates a nullable String preference delegate
 *
 * @param key Preference key
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var storeSetting by nullableStringPref("storeSetting")
 * ```
 */
fun nullableStringPref(key: String): ReadWriteProperty<Any?, String?> =
    object : ReadWriteProperty<Any?, String?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
            return PrefManager.getString(key)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            PrefManager.putString(key, value)
        }
    }

/**
 * Creates a Boolean preference delegate
 *
 * @param key Preference key
 * @param default Default value when key is not set (defaults to false)
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var isLogin by boolPref("isLogin", false)
 * var isInsideChat by boolPref("isInsideChat") // defaults to false
 * ```
 */
fun boolPref(key: String, default: Boolean = false): ReadWriteProperty<Any?, Boolean> =
    object : ReadWriteProperty<Any?, Boolean> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
            return PrefManager.getBoolean(key, default)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            PrefManager.putBoolean(key, value)
        }
    }

/**
 * Creates a non-null Int preference delegate
 *
 * @param key Preference key
 * @param default Default value when key is not set (defaults to 0)
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var retryCount by intPref("retryCount", 0)
 * var maxItems by intPref("maxItems", 100)
 * ```
 */
fun intPref(key: String, default: Int = 0): ReadWriteProperty<Any?, Int> =
    object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
            return PrefManager.getInt(key, default)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            PrefManager.putInt(key, value)
        }
    }

/**
 * Creates a nullable Int preference delegate
 *
 * @param key Preference key
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var optionalCount by nullableIntPref("optionalCount")
 * ```
 */
fun nullableIntPref(key: String): ReadWriteProperty<Any?, Int?> =
    object : ReadWriteProperty<Any?, Int?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int? {
            val value = PrefManager.getString(key)
            return value?.toIntOrNull()
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int?) {
            if (value == null) {
                PrefManager.remove(key)
            } else {
                PrefManager.putInt(key, value)
            }
        }
    }

/**
 * Creates a Long preference delegate
 *
 * @param key Preference key
 * @param default Default value when key is not set (defaults to 0L)
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var lastSyncTimestamp by longPref("lastSyncTimestamp", 0L)
 * ```
 */
fun longPref(key: String, default: Long = 0L): ReadWriteProperty<Any?, Long> =
    object : ReadWriteProperty<Any?, Long> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Long {
            return PrefManager.getLong(key, default)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
            PrefManager.putLong(key, value)
        }
    }

/**
 * Creates a nullable Long preference delegate
 *
 * @param key Preference key
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var optionalTimestamp by nullableLongPref("optionalTimestamp")
 * ```
 */
fun nullableLongPref(key: String): ReadWriteProperty<Any?, Long?> =
    object : ReadWriteProperty<Any?, Long?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Long? {
            val value = PrefManager.getString(key)
            return value?.toLongOrNull()
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long?) {
            if (value == null) {
                PrefManager.remove(key)
            } else {
                PrefManager.putLong(key, value)
            }
        }
    }

/**
 * Creates a Float preference delegate
 *
 * @param key Preference key
 * @param default Default value when key is not set (defaults to 0f)
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var volume by floatPref("volume", 0.5f)
 * ```
 */
fun floatPref(key: String, default: Float = 0f): ReadWriteProperty<Any?, Float> =
    object : ReadWriteProperty<Any?, Float> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Float {
            return PrefManager.getFloat(key, default)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
            PrefManager.putFloat(key, value)
        }
    }

/**
 * Creates a nullable Float preference delegate
 *
 * @param key Preference key
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * var optionalRating by nullableFloatPref("optionalRating")
 * ```
 */
fun nullableFloatPref(key: String): ReadWriteProperty<Any?, Float?> =
    object : ReadWriteProperty<Any?, Float?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Float? {
            val value = PrefManager.getString(key)
            return value?.toFloatOrNull()
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float?) {
            if (value == null) {
                PrefManager.remove(key)
            } else {
                PrefManager.putFloat(key, value)
            }
        }
    }

/**
 * Creates a JSON object preference delegate using kotlinx.serialization
 *
 * @param T The serializable type
 * @param key Preference key
 * @param default Default value when key is not set (defaults to null)
 * @return ReadWriteProperty delegate
 *
 * Usage:
 * ```
 * @Serializable
 * data class User(val id: String, val name: String)
 *
 * var user by jsonPref<User>("user")
 * var config by jsonPref<AppConfig>("config", AppConfig())
 * ```
 */
inline fun <reified T> jsonPref(
    key: String,
    default: T? = null
): ReadWriteProperty<Any?, T?> =
    object : ReadWriteProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            val json = PrefManager.getString(key) ?: return default
            return runCatching {
                Json.decodeFromString<T>(json)
            }.getOrElse { default }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            if (value == null) {
                PrefManager.remove(key)
            } else {
                val json = Json.encodeToString(value)
                PrefManager.putString(key, json)
            }
        }
    }
