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

val MyIcon.IcUsersSolar: ImageVector
    get() {
        if (_IcUsersGroup != null) {
            return _IcUsersGroup!!
        }
        _IcUsersGroup = ImageVector.Builder(
            name = "IcUsersGroup",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Center person head (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(15.5f, 7.5f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -7f, 0f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7f, 0f)
            }

            // Path 2 - Side persons heads (alpha 0.4)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.4f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19.5f, 7.5f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -5f, 0f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 0f)
                moveToRelative(-15f, 0f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 5f, 0f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5f, 0f)
            }

            // Path 3 - Center person body (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18f, 16.5f)
                curveToRelative(0f, 1.933f, -2.686f, 3.5f, -6f, 3.5f)
                reflectiveCurveToRelative(-6f, -1.567f, -6f, -3.5f)
                reflectiveCurveTo(8.686f, 13f, 12f, 13f)
                reflectiveCurveToRelative(6f, 1.567f, 6f, 3.5f)
            }

            // Path 4 - Side persons bodies (alpha 0.4)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.4f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(22f, 16.5f)
                curveToRelative(0f, 1.38f, -1.79f, 2.5f, -4f, 2.5f)
                reflectiveCurveToRelative(-4f, -1.12f, -4f, -2.5f)
                reflectiveCurveToRelative(1.79f, -2.5f, 4f, -2.5f)
                reflectiveCurveToRelative(4f, 1.12f, 4f, 2.5f)
                moveToRelative(-20f, 0f)
                curveTo(2f, 17.88f, 3.79f, 19f, 6f, 19f)
                reflectiveCurveToRelative(4f, -1.12f, 4f, -2.5f)
                reflectiveCurveTo(8.21f, 14f, 6f, 14f)
                reflectiveCurveToRelative(-4f, 1.12f, -4f, 2.5f)
            }
        }.build()
        return _IcUsersGroup!!
    }

@Suppress("ObjectPropertyName")
private var _IcUsersGroup: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcUsersSolar, contentDescription = "Users Group Icon")
    }
}