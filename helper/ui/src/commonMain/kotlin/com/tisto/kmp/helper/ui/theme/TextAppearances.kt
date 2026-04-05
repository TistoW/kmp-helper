package com.tisto.kmp.helper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

object TextAppearances {
    // Body
    val body1: TextStyle
        @Composable get() = MaterialTheme.typography.bodyLarge

    val body1Bold: TextStyle
        @Composable get() = body1.copy(fontWeight = FontWeight.Bold)

    val body2: TextStyle
        @Composable get() = MaterialTheme.typography.bodyMedium

    val body2Bold: TextStyle
        @Composable get() = body2.copy(fontWeight = FontWeight.Bold)

    val body3: TextStyle
        @Composable get() = MaterialTheme.typography.bodySmall

    val body3Bold: TextStyle
        @Composable get() = body3.copy(fontWeight = FontWeight.Bold)

    // Label
    val label1: TextStyle
        @Composable get() = MaterialTheme.typography.labelLarge

    val label1Bold: TextStyle
        @Composable get() = label1.copy(fontWeight = FontWeight.Bold)

    val label2: TextStyle
        @Composable get() = MaterialTheme.typography.labelMedium

    val label2Bold: TextStyle
        @Composable get() = label2.copy(fontWeight = FontWeight.Bold)

    val label3: TextStyle
        @Composable get() = MaterialTheme.typography.labelSmall

    val label3Bold: TextStyle
        @Composable get() = label3.copy(fontWeight = FontWeight.Bold)

    // Title

    fun title1() = MaterialTheme.typography.titleLarge

    val title1Bold: TextStyle
        @Composable get() = title1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun title1Bold() = title1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun title2() = MaterialTheme.typography.titleMedium

    @Composable
    fun title2Bold() = title2().copy(fontWeight = FontWeight.Bold)


    val title3: TextStyle
        @Composable get() = MaterialTheme.typography.titleSmall

    val title3Bold: TextStyle
        @Composable get() = title3.copy(fontWeight = FontWeight.Bold)

    // Headline
    val headline1: TextStyle
        @Composable get() = MaterialTheme.typography.headlineSmall

    val headline1Bold: TextStyle
        @Composable get() = headline1.copy(fontWeight = FontWeight.Bold)

    val headline2: TextStyle
        @Composable get() = MaterialTheme.typography.headlineMedium

    val headline2Bold: TextStyle
        @Composable get() = headline2.copy(fontWeight = FontWeight.Bold)

    val headline3: TextStyle
        @Composable get() = MaterialTheme.typography.headlineLarge

    val headline3Bold: TextStyle
        @Composable get() = headline3.copy(fontWeight = FontWeight.Bold)

    // Display
    val display1: TextStyle
        @Composable get() = MaterialTheme.typography.displaySmall

    val display1Bold: TextStyle
        @Composable get() = display1.copy(fontWeight = FontWeight.Bold)

    val display2: TextStyle
        @Composable get() = MaterialTheme.typography.displayMedium

    val display2Bold: TextStyle
        @Composable get() = display2.copy(fontWeight = FontWeight.Bold)

    val display3: TextStyle
        @Composable get() = MaterialTheme.typography.displayLarge

    val display3Bold: TextStyle
        @Composable get() = display3.copy(fontWeight = FontWeight.Bold)
}