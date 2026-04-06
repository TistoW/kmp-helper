package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun AdaptiveRowColumn(
    modifier: Modifier = Modifier,
    isColumn: Boolean = false,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    if (isColumn) {
        Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
            content()
        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = verticalAlignment,
            horizontalArrangement= horizontalArrangement
        ) {
            content()
        }
    }
}