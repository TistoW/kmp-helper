package com.tisto.kmp.helper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

object TextAppearances {

    // Body
    @Composable
    fun body1() = MaterialTheme.typography.bodyLarge
    @Composable
    fun body1Bold() = body1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun body2() = MaterialTheme.typography.bodyMedium
    @Composable
    fun body2Bold() = body2().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun body3() = MaterialTheme.typography.bodySmall
    @Composable
    fun body3Bold() = body3().copy(fontWeight = FontWeight.Bold)

    // Label
    @Composable
    fun label1() = MaterialTheme.typography.labelLarge
    @Composable
    fun label1Bold() = label1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun label2() = MaterialTheme.typography.labelMedium
    @Composable
    fun label2Bold() = label2().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun label3() = MaterialTheme.typography.labelSmall
    @Composable
    fun label3Bold() = label3().copy(fontWeight = FontWeight.Bold)

    // Title
    @Composable
    fun title1() = MaterialTheme.typography.titleLarge
    @Composable
    fun title1Bold() = title1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun title2() = MaterialTheme.typography.titleMedium
    @Composable
    fun title2Bold() = title2().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun title3() = MaterialTheme.typography.titleSmall
    @Composable
    fun title3Bold() = title3().copy(fontWeight = FontWeight.Bold)

    // Headline
    @Composable
    fun headline1() = MaterialTheme.typography.headlineSmall
    @Composable
    fun headline1Bold() = headline1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun headline2() = MaterialTheme.typography.headlineMedium
    @Composable
    fun headline2Bold() = headline2().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun headline3() = MaterialTheme.typography.headlineLarge
    @Composable
    fun headline3Bold() = headline3().copy(fontWeight = FontWeight.Bold)

    // Display
    @Composable
    fun display1() = MaterialTheme.typography.displaySmall
    @Composable
    fun display1Bold() = display1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun display2() = MaterialTheme.typography.displayMedium
    @Composable
    fun display2Bold() = display2().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun display3() = MaterialTheme.typography.displayLarge
    @Composable
    fun display3Bold() = display3().copy(fontWeight = FontWeight.Bold)

}