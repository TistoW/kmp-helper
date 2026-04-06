package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Radius

@Composable
fun CustomCardBox(
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    cornerRadius: Dp = Radius.normal,
    strokeWidth: Dp = 0.dp,
    strokeColor: Color = Colors.White,
    backgroundColor: Color = Color.White,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    CustomCard(
        modifier = modifier,
        elevation = elevation,
        cornerRadius = cornerRadius,
        strokeWidth = strokeWidth,
        strokeColor = strokeColor,
        backgroundColor = backgroundColor,
        onClick = onClick,
        enabled = enabled,
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                content = content
            )
        }
    )
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    cornerRadius: Dp = Radius.normal,
    strokeWidth: Dp = 0.dp,
    strokeColor: Color = Colors.White,
    backgroundColor: Color = Color.White,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        Card(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(cornerRadius),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            ),
            border = BorderStroke(
                width = strokeWidth,
                color = strokeColor
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(cornerRadius),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            ),
            border = BorderStroke(
                width = strokeWidth,
                color = strokeColor
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            content = content
        )
    }

}