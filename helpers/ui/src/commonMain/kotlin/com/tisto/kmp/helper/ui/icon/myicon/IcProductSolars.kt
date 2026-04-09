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

val MyIcon.IcProductSolars: ImageVector
    get() {
        if (_IcProductSolars != null) {
            return _IcProductSolars!!
        }
        _IcProductSolars = ImageVector.Builder(
            name = "IcProductSolars",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Bottom box/package (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6f, 18f)
                curveToRelative(0f, -1.886f, 0f, -2.828f, 0.586f, -3.414f)
                curveTo(7.172f, 14f, 8.114f, 14f, 10f, 14f)
                horizontalLineToRelative(4f)
                curveToRelative(1.886f, 0f, 2.828f, 0f, 3.414f, 0.586f)
                curveTo(18f, 15.172f, 18f, 16.114f, 18f, 18f)
                curveToRelative(0f, 1.886f, 0f, 2.828f, -0.586f, 3.414f)
                curveTo(16.828f, 22f, 15.886f, 22f, 14f, 22f)
                horizontalLineToRelative(-4f)
                curveToRelative(-1.886f, 0f, -2.828f, 0f, -3.414f, -0.586f)
                curveTo(6f, 20.828f, 6f, 19.886f, 6f, 18f)
            }

            // Path 2 - Hanger/hook mechanism (alpha 0.5)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10.286f, 3.91f)
                curveToRelative(0f, -0.568f, 0.538f, -1.16f, 1.374f, -1.16f)
                curveToRelative(0.836f, 0f, 1.374f, 0.592f, 1.374f, 1.16f)
                curveToRelative(0f, 0.311f, -0.112f, 0.581f, -0.294f, 0.78f)
                arcToRelative(11.18f, 11.18f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.38f, 0.385f)
                lineToRelative(-0.08f, 0.08f)
                arcToRelative(8.953f, 8.953f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.529f, 0.558f)
                curveToRelative(-0.265f, 0.312f, -0.553f, 0.723f, -0.658f, 1.23f)
                arcToRelative(4.331f, 4.331f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.774f, 0.722f)
                lineToRelative(-7.095f, 4.992f)
                curveToRelative(-0.927f, 0.652f, -1.166f, 1.702f, -0.828f, 2.582f)
                curveToRelative(0.332f, 0.866f, 1.194f, 1.511f, 2.306f, 1.511f)
                horizontalLineTo(6.01f)
                curveToRelative(0.016f, -0.637f, 0.061f, -1.12f, 0.184f, -1.5f)
                horizontalLineToRelative(-2.49f)
                curveToRelative(-0.503f, 0f, -0.801f, -0.273f, -0.907f, -0.548f)
                curveToRelative(-0.1f, -0.261f, -0.054f, -0.576f, 0.29f, -0.819f)
                lineToRelative(7.096f, -4.99f)
                arcToRelative(2.891f, 2.891f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.628f, -0.513f)
                arcToRelative(2.906f, 2.906f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.655f, 0.482f)
                lineToRelative(7.433f, 5.01f)
                curveToRelative(0.356f, 0.241f, 0.406f, 0.56f, 0.308f, 0.823f)
                curveToRelative(-0.103f, 0.278f, -0.4f, 0.555f, -0.909f, 0.555f)
                horizontalLineToRelative(-2.49f)
                curveToRelative(0.123f, 0.38f, 0.168f, 0.863f, 0.184f, 1.5f)
                horizontalLineToRelative(2.306f)
                curveToRelative(1.125f, 0f, 1.99f, -0.657f, 2.316f, -1.533f)
                curveToRelative(0.33f, -0.891f, 0.073f, -1.948f, -0.877f, -2.588f)
                lineToRelative(-7.433f, -5.01f)
                arcToRelative(4.331f, 4.331f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.614f, -0.66f)
                curveToRelative(0.05f, -0.081f, 0.118f, -0.172f, 0.205f, -0.274f)
                curveToRelative(0.126f, -0.149f, 0.274f, -0.298f, 0.44f, -0.464f)
                lineToRelative(0.075f, -0.072f)
                curveToRelative(0.14f, -0.14f, 0.295f, -0.292f, 0.435f, -0.445f)
                curveToRelative(0.443f, -0.48f, 0.69f, -1.115f, 0.69f, -1.795f)
                curveToRelative(0f, -1.542f, -1.364f, -2.659f, -2.874f, -2.659f)
                reflectiveCurveTo(8.786f, 2.367f, 8.786f, 3.91f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.5f, 0f)
            }
        }.build()
        return _IcProductSolars!!
    }

@Suppress("ObjectPropertyName")
private var _IcProductSolars: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcProductSolars, contentDescription = "Product Icon")
    }
}