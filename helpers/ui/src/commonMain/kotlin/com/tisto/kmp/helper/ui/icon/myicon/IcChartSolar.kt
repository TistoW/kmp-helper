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

val MyIcon.IcChartSolar: ImageVector
    get() {
        if (_IcChartBar != null) {
            return _IcChartBar!!
        }
        _IcChartBar = ImageVector.Builder(
            name = "IcChartBar",
            defaultWidth = 32.dp,
            defaultHeight = 32.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Center bar (tallest, no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14f, 20.5f)
                verticalLineTo(4.25f)
                curveToRelative(0f, -0.728f, -0.002f, -1.2f, -0.048f, -1.546f)
                curveToRelative(-0.044f, -0.325f, -0.115f, -0.427f, -0.172f, -0.484f)
                curveToRelative(-0.057f, -0.057f, -0.159f, -0.128f, -0.484f, -0.172f)
                curveTo(12.949f, 2.002f, 12.478f, 2f, 11.75f, 2f)
                curveToRelative(-0.728f, 0f, -1.2f, 0.002f, -1.546f, 0.048f)
                curveToRelative(-0.325f, 0.044f, -0.427f, 0.115f, -0.484f, 0.172f)
                curveToRelative(-0.057f, 0.057f, -0.128f, 0.159f, -0.172f, 0.484f)
                curveToRelative(-0.046f, 0.347f, -0.048f, 0.818f, -0.048f, 1.546f)
                verticalLineTo(20.5f)
                close()
            }

            // Path 2 - Left and right bars (alpha 0.7)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.7f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8f, 8.75f)
                arcTo(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.25f, 8f)
                horizontalLineToRelative(-3f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.75f, 0.75f)
                verticalLineTo(20.5f)
                horizontalLineTo(8f)
                close()
                moveToRelative(12f, 5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.75f, -0.75f)
                horizontalLineToRelative(-3f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.75f, 0.75f)
                verticalLineToRelative(6.75f)
                horizontalLineTo(20f)
                close()
            }

            // Path 3 - Bottom baseline (alpha 0.5)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(1.75f, 20.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.5f)
                horizontalLineToRelative(20f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1.5f)
                horizontalLineTo(2f)
                close()
            }
        }.build()
        return _IcChartBar!!
    }

@Suppress("ObjectPropertyName")
private var _IcChartBar: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcChartSolar, contentDescription = "Chart Bar Icon")
    }
}