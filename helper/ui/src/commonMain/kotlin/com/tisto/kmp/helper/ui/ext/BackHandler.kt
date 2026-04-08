package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable

/*****************
 * If you see error from Android only project please ignore it.
 * check if it's really error from KMP project
 */

@Composable
expect fun BackHandlerExpect(enabled: Boolean, onBack: () -> Unit)

@Composable
fun BackHandler( // biar bisa di pakai di android only dan KMP
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    BackHandlerExpect(enabled, onBack)
}
