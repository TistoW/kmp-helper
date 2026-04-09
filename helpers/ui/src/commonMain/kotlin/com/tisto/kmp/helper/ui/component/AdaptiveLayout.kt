package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    isColumn: Boolean = false,
    content: @Composable () -> Unit
) {
    if (isColumn) {
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    } else {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            content()
        }
    }
}