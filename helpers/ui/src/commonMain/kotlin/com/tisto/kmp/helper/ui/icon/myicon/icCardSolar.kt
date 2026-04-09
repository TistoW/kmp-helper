package com.tisto.kmp.helper.ui.icon.myicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon

val MyIcon.IcCardSolar: ImageVector
    get() {
        if (_IcCardSolar != null) {
            return _IcCardSolar!!
        }
        _IcCardSolar = ImageVector.Builder(
            name = "IcCardSolar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Card background (alpha 0.5)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(22f, 12.818f)
                lineToRelative(-0.409f, -0.409f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.182f, 0f)
                lineToRelative(-0.801f, 0.801f)
                arcToRelative(2.251f, 2.251f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4.358f, 0.79f)
                verticalLineToRelative(1.764f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.341f, 3.827f)
                lineToRelative(0.409f, 0.409f)
                horizontalLineTo(10f)
                curveToRelative(-3.771f, 0f, -5.657f, 0f, -6.828f, -1.172f)
                curveTo(2f, 17.657f, 2f, 15.771f, 2f, 12f)
                curveToRelative(0f, -0.442f, 0.002f, -1.608f, 0.004f, -2f)
                horizontalLineTo(22f)
                curveToRelative(0.002f, 0.392f, 0f, 1.558f, 0f, 2f)
                close()
            }

            // Path 2 - Card top section and upward arrow (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                // Text placeholder bar
                moveTo(5.25f, 16f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.75f, -0.75f)
                horizontalLineToRelative(4f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 1.5f)
                horizontalLineTo(6f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.75f, -0.75f)

                // Card header section
                moveTo(9.995f, 4f)
                horizontalLineToRelative(4.01f)
                curveToRelative(3.781f, 0f, 5.672f, 0f, 6.846f, 1.116f)
                curveToRelative(0.846f, 0.803f, 1.083f, 1.96f, 1.149f, 3.884f)
                verticalLineToRelative(1f)
                horizontalLineTo(2f)
                verticalLineTo(9f)
                curveToRelative(0.066f, -1.925f, 0.303f, -3.08f, 1.149f, -3.884f)
                curveTo(4.323f, 4f, 6.214f, 4f, 9.995f, 4f)

                // Upward arrow
                moveToRelative(9.475f, 9.47f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.06f, 0f)
                lineToRelative(2f, 2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = true, -1.06f, 1.06f)
                lineToRelative(-0.72f, -0.72f)
                verticalLineTo(20f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.5f, 0f)
                verticalLineToRelative(-4.19f)
                lineToRelative(-0.72f, 0.72f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = true, -1.06f, -1.06f)
                close()
            }

            // Path 3 - Downward arrow (evenOdd fill type)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(15.5f, 13.25f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.75f, 0.75f)
                verticalLineToRelative(4.19f)
                lineToRelative(0.72f, -0.72f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.06f, 1.06f)
                lineToRelative(-2f, 2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.06f, 0f)
                lineToRelative(-2f, -2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.06f, -1.06f)
                lineToRelative(0.72f, 0.72f)
                verticalLineTo(14f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.75f, -0.75f)
            }
        }.build()
        return _IcCardSolar!!
    }

@Suppress("ObjectPropertyName")
private var _IcCardSolar: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcCardSolar, contentDescription = "Card Transfer Icon")
    }
}