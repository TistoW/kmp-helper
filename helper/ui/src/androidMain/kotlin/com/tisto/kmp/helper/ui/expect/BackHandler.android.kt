package com.tisto.kmp.helper.ui.expect

import androidx.compose.runtime.Composable

// androidMain - actual
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, onBack)
}