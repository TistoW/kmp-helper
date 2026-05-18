package com.tisto.kmp.helper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun HelperTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = Color.Blue,
        onSurface = Color.Black,
        background = Color.White,
        surface = Color.White,
        surfaceVariant = Color.White,
    )

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}