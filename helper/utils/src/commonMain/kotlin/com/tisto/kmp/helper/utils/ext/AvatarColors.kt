package com.tisto.kmp.helper.utils.ext

import androidx.compose.ui.graphics.Color

val AvatarColors = listOf(
    Color(0xFFE8F5E9), // green
    Color(0xFFE3F2FD), // blue
    Color(0xFFE0F2F1), // teal
    Color(0xFFFFEBEE), // red
    Color(0xFFFFF3E0), // orange
    Color(0xFFF3E5F5), // purple
    Color(0xFFE1F5FE), // light blue
    Color(0xFFFCE4EC), // pink
    Color(0xFFF1F8E9), // lime
    Color(0xFFEDE7F6), // deep purple
    Color(0xFFE0F7FA), // cyan
    Color(0xFFFFFDE7), // yellow
)


fun colorFromText(text: String): Color {
    if (text.isEmpty()) return AvatarColors.first()

    val char = text.first().uppercaseChar()
    val index = (char.code - 'A'.code).coerceAtLeast(0)

    return AvatarColors[index % AvatarColors.size]
}