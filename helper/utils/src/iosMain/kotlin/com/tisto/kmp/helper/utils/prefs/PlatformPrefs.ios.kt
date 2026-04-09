package com.tisto.kmp.helper.utils.prefs

import platform.Foundation.NSUserDefaults

/**
 * iOS implementation of PlatformPrefs using NSUserDefaults
 *
 * This implementation is WRITE-ONCE and STABLE.
 * It never changes when new preferences are added.
 * All preference logic lives in common code (AppPrefs + AppPrefsExt).
 *
 * Thread-safe through NSUserDefaults built-in synchronization.
 */
actual object PlatformPrefs {
    private val defaults: NSUserDefaults
        get() = NSUserDefaults.standardUserDefaults

    actual fun get(key: String): String? {
        return defaults.stringForKey(key)
    }

    actual fun put(key: String, value: String) {
        defaults.setObject(value, key)
        defaults.synchronize()
    }

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
        defaults.synchronize()
    }

    actual fun clear() {
        // Get all keys in the user defaults domain
        val domain = defaults.persistentDomainForName(
            NSUserDefaults.standardUserDefaults.addSuiteNamed("").toString()
        )
        domain?.keys?.forEach { key ->
            defaults.removeObjectForKey(key.toString())
        }
        defaults.synchronize()
    }
}
