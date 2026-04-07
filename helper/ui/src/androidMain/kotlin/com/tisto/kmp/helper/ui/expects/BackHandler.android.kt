package com.tisto.kmp.helper.ui.expects

import androidx.compose.runtime.Composable

// androidMain - actual
@Composable
actual fun BackHandlerExpect(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, onBack)
}