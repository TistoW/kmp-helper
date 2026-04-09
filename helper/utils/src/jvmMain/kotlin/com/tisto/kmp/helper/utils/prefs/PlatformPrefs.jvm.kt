package com.tisto.kmp.helper.utils.prefs

import java.util.prefs.Preferences

/**
 * JVM (Desktop) implementation of PlatformPrefs using Java Preferences API
 *
 * This implementation is WRITE-ONCE and STABLE.
 * It never changes when new preferences are added.
 * All preference logic lives in common code (AppPrefs + AppPrefsExt).
 *
 * Thread-safe through Preferences built-in synchronization.
 * Data persists in OS-specific location (Windows Registry, macOS plist, Linux file).
 */
actual object PlatformPrefs {
    private val prefs: Preferences by lazy {
        Preferences.userNodeForPackage(PlatformPrefs::class.java)
    }

    actual fun get(key: String): String? {
        return prefs.get(key, null)
    }

    actual fun put(key: String, value: String) {
        prefs.put(key, value)
        prefs.flush()
    }

    actual fun remove(key: String) {
        prefs.remove(key)
        prefs.flush()
    }

    actual fun clear() {
        prefs.clear()
        prefs.flush()
    }
}
