package com.tisto.kmp.helper.utils.ext

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ========== SessionManager.kt ==========
class SessionManager {
    private val _sessionKey = MutableStateFlow(0)
    val sessionKey: StateFlow<Int> = _sessionKey.asStateFlow()

    fun resetSession() {
        _sessionKey.value++
    }
}