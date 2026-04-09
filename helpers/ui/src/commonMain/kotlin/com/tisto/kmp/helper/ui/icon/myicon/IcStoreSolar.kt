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

val MyIcon.IcStoreSolar: ImageVector
    get() {
        if (_IcStoreSolar != null) {
            return _IcStoreSolar!!
        }
        _IcStoreSolar = ImageVector.Builder(
            name = "IcStoreSolar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Door/entrance (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14.5f, 21.991f)
                verticalLineTo(18.5f)
                curveToRelative(0f, -0.935f, 0f, -1.402f, -0.201f, -1.75f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.549f, -0.549f)
                curveTo(13.402f, 16f, 12.935f, 16f, 12f, 16f)
                reflectiveCurveToRelative(-1.402f, 0f, -1.75f, 0.201f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.549f, 0.549f)
                curveToRelative(-0.201f, 0.348f, -0.201f, 0.815f, -0.201f, 1.75f)
                verticalLineToRelative(3.491f)
                close()
            }

            // Path 2 - Store body/building (alpha 0.5)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5.732f, 12f)
                curveToRelative(-0.89f, 0f, -1.679f, -0.376f, -2.232f, -0.967f)
                verticalLineTo(14f)
                curveToRelative(0f, 3.771f, 0f, 5.657f, 1.172f, 6.828f)
                curveToRelative(0.943f, 0.944f, 2.348f, 1.127f, 4.828f, 1.163f)
                horizontalLineToRelative(5f)
                curveToRelative(2.48f, -0.036f, 3.885f, -0.22f, 4.828f, -1.163f)
                curveTo(20.5f, 19.657f, 20.5f, 17.771f, 20.5f, 14f)
                verticalLineToRelative(-2.966f)
                arcToRelative(3.06f, 3.06f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5.275f, -1.789f)
                lineToRelative(-0.073f, -0.728f)
                arcToRelative(3.167f, 3.167f, 0f, isMoreThanHalf = true, isPositiveArc = true, -6.307f, 0.038f)
                lineToRelative(-0.069f, 0.69f)
                arcTo(3.06f, 3.06f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.732f, 12f)
                moveToRelative(8.768f, 6.5f)
                verticalLineToRelative(3.491f)
                horizontalLineToRelative(-5f)
                verticalLineTo(18.5f)
                curveToRelative(0f, -0.935f, 0f, -1.402f, 0.201f, -1.75f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.549f, -0.549f)
                curveTo(10.598f, 16f, 11.065f, 16f, 12f, 16f)
                reflectiveCurveToRelative(1.402f, 0f, 1.75f, 0.201f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.549f, 0.549f)
                curveToRelative(0.201f, 0.348f, 0.201f, 0.815f, 0.201f, 1.75f)
            }

            // Path 3 - Center awning/roof (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.5f, 2f)
                horizontalLineToRelative(5f)
                lineToRelative(0.652f, 6.517f)
                arcToRelative(3.167f, 3.167f, 0f, isMoreThanHalf = true, isPositiveArc = true, -6.304f, 0f)
                close()
            }

            // Path 4 - Side awnings (alpha 0.7)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.7f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3.33f, 5.351f)
                curveToRelative(0.178f, -0.89f, 0.267f, -1.335f, 0.448f, -1.696f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.889f, -1.548f)
                curveTo(6.057f, 2f, 6.51f, 2f, 7.418f, 2f)
                horizontalLineToRelative(2.083f)
                lineToRelative(-0.725f, 7.245f)
                arcToRelative(3.06f, 3.06f, 0f, isMoreThanHalf = true, isPositiveArc = true, -6.044f, -0.904f)
                close()
                moveToRelative(17.34f, 0f)
                curveToRelative(-0.178f, -0.89f, -0.267f, -1.335f, -0.448f, -1.696f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.888f, -1.548f)
                curveTo(17.944f, 2f, 17.49f, 2f, 16.582f, 2f)
                horizontalLineTo(14.5f)
                lineToRelative(0.725f, 7.245f)
                arcToRelative(3.06f, 3.06f, 0f, isMoreThanHalf = true, isPositiveArc = false, 6.043f, -0.904f)
                close()
            }
        }.build()
        return _IcStoreSolar!!
    }

@Suppress("ObjectPropertyName")
private var _IcStoreSolar: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcStoreSolar, contentDescription = "Store Icon")
    }
}