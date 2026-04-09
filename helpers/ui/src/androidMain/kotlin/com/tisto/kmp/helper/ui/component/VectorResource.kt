package com.tisto.kmp.helper.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun vectorResource(@DrawableRes id: Int): ImageVector {
    return ImageVector.vectorResource(id)
}