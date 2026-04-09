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

val MyIcon.IcServerSquareSolar: ImageVector
    get() {
        if (_IcServerSquareSolar != null) {
            return _IcServerSquareSolar!!
        }
        _IcServerSquareSolar = ImageVector.Builder(
            name = "IcServerSquareSolar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Server boxes background (alpha 0.5)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3.172f, 19.828f)
                curveTo(4.343f, 21f, 6.229f, 21f, 10f, 21f)
                horizontalLineToRelative(3.89f)
                arcToRelative(5.47f, 5.47f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.89f, -3f)
                quadToRelative(0f, -0.452f, 0.07f, -0.885f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.41f, -1.365f)
                arcToRelative(5.52f, 5.52f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.376f, -3f)
                horizontalLineTo(2f)
                verticalLineTo(13f)
                curveToRelative(0f, 3.771f, 0f, 5.657f, 1.172f, 6.828f)
                moveToRelative(0f, -15.656f)
                curveTo(2f, 5.343f, 2f, 7.229f, 2f, 11f)
                verticalLineToRelative(0.25f)
                horizontalLineToRelative(20f)
                verticalLineTo(11f)
                curveToRelative(0f, -3.771f, 0f, -5.657f, -1.172f, -6.828f)
                reflectiveCurveTo(17.771f, 3f, 14f, 3f)
                horizontalLineToRelative(-4f)
                curveTo(6.229f, 3f, 4.343f, 3f, 3.172f, 4.172f)
            }

            // Path 2 - Top sync arrow (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14.929f, 16.753f)
                curveToRelative(0.389f, -1.497f, 1.924f, -2.503f, 3.576f, -2.503f)
                curveToRelative(1.277f, 0f, 2.44f, 0.58f, 3.106f, 1.515f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = true, -1.222f, 0.87f)
                curveToRelative(-0.36f, -0.505f, -1.048f, -0.885f, -1.884f, -0.885f)
                curveToRelative(-0.967f, 0f, -1.687f, 0.482f, -2f, 1.08f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.15f, 1.24f)
                lineToRelative(-0.583f, 0.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.976f, 0f)
                lineToRelative(-0.584f, -0.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.417f, -1.317f)
            }

            // Path 3 - Bottom sync arrow (evenOdd)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(20.928f, 17.43f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.976f, 0f)
                lineToRelative(0.584f, 0.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.417f, 1.317f)
                curveToRelative(-0.389f, 1.497f, -1.924f, 2.503f, -3.576f, 2.503f)
                curveToRelative(-1.277f, 0f, -2.44f, -0.58f, -3.106f, -1.515f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.222f, -0.87f)
                curveToRelative(0.36f, 0.505f, 1.048f, 0.885f, 1.884f, 0.885f)
                curveToRelative(0.967f, 0f, 1.687f, -0.482f, 2f, -1.08f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.15f, -1.24f)
                close()
            }

            // Path 4 - Indicator lights/bars (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5.25f, 17.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.5f, 0f)
                verticalLineToRelative(-2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.5f, 0f)
                close()
                moveToRelative(3f, 0f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.5f, 0f)
                verticalLineToRelative(-2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.5f, 0f)
                close()
                moveToRelative(0f, -9f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.5f, 0f)
                verticalLineToRelative(-2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.5f, 0f)
                close()
                moveTo(6f, 9.25f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.75f, -0.75f)
                verticalLineToRelative(-2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.5f, 0f)
                verticalLineToRelative(2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.75f, 0.75f)
                moveToRelative(7.5f, -2.5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.5f)
                horizontalLineTo(18f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1.5f)
                close()
            }

            // Path 5 - Middle separator line (evenOdd)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(20.144f, 12.75f)
                horizontalLineTo(22f)
                verticalLineToRelative(-1.5f)
                horizontalLineTo(2f)
                verticalLineToRelative(1.5f)
                horizontalLineToRelative(14.856f)
                arcToRelative(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.644f, -0.25f)
                arcToRelative(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.644f, 0.25f)
            }
        }.build()
        return _IcServerSquareSolar!!
    }

@Suppress("ObjectPropertyName")
private var _IcServerSquareSolar: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcServerSquareSolar, contentDescription = "Server Icon")
    }
}