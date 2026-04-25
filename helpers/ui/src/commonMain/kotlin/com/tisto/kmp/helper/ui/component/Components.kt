package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.Spacing

@Composable
fun SimpleHorizontalDivider(
    modifier: Modifier = Modifier,
    height: Dp = 0.5.dp,
    color: Color = Color(0xFFEFEFEF)
) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        color = color
    )
}


@Composable
fun IconCloseButton(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    iconSize: Dp = 14.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Close,
            contentDescription = "Remove",
            modifier = Modifier.size(iconSize),
            tint = Color.White,
        )
    }
}

