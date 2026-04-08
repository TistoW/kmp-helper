package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandlerExpect(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, onBack)
}