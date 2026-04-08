package com.tisto.kmp.helper.ui.ext

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun Modifier.longClickable(
    onClick: () -> Unit = {},
    onLongClick: () -> Unit
): Modifier = this.then(
    Modifier.combinedClickable(
        onClick = onClick,
        onLongClick = onLongClick
    )
)

@Composable
fun Modifier.insertTopBarPadding() = this.windowInsetsPadding(
    WindowInsets.statusBars.only(WindowInsetsSides.Top)
)


