package com.tisto.kmp.helper.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SimpleHorizontalDivider(
    modifier: Modifier = Modifier,
    height: Dp = 0.5.dp,
    color: Color = Color(0xFFEFEFEF)
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth().height(height),
        color = color
    )
}
