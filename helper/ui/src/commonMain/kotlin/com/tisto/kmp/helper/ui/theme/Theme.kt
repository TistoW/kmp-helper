package com.tisto.kmp.helper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun HelperTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = Colors.Green700,
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