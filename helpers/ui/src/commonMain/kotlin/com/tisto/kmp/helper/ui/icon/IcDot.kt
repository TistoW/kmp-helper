package com.tisto.kmp.helper.ui.icon

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.MobilePreview

val IcDot: ImageVector
    get() = ImageVector.Builder(
        name = "IcDot",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(8f, 4f)
            arcToRelative(4f, 4f, 0f, true, true, 0f, 8f)
            arcToRelative(4f, 4f, 0f, false, true, 0f, -8f)
            close()
        }
    }.build()

@MobilePreview
@Composable
fun IcDotPreview() {
    Image(IcDot, contentDescription = null)
}