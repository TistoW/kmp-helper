package com.tisto.kmp.helper.ui.icon.myicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon

val MyIcon.IcChatMoney: ImageVector
    get() {
        if (_IcChatMoney != null) {
            return _IcChatMoney!!
        }
        _IcChatMoney = ImageVector.Builder(
            name = "IcChatMoney",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 22f)
                curveToRelative(5.523f, 0f, 10f, -4.477f, 10f, -10f)
                reflectiveCurveTo(17.523f, 2f, 12f, 2f)
                reflectiveCurveTo(2f, 6.477f, 2f, 12f)
                curveToRelative(0f, 1.6f, 0.376f, 3.112f, 1.043f, 4.453f)
                curveToRelative(0.178f, 0.356f, 0.237f, 0.763f, 0.134f, 1.148f)
                lineToRelative(-0.595f, 2.226f)
                arcToRelative(1.3f, 1.3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.591f, 1.592f)
                lineToRelative(2.226f, -0.596f)
                arcToRelative(1.63f, 1.63f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.149f, 0.133f)
                arcTo(9.96f, 9.96f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 22f)
            }

            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12.75f, 8f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.5f, 0f)
                verticalLineToRelative(0.01f)
                curveToRelative(-1.089f, 0.275f, -2f, 1.133f, -2f, 2.323f)
                curveToRelative(0f, 1.457f, 1.365f, 2.417f, 2.75f, 2.417f)
                curveToRelative(0.824f, 0f, 1.25f, 0.533f, 1.25f, 0.917f)
                reflectiveCurveToRelative(-0.426f, 0.916f, -1.25f, 0.916f)
                reflectiveCurveToRelative(-1.25f, -0.532f, -1.25f, -0.916f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.5f, 0f)
                curveToRelative(0f, 1.19f, 0.911f, 2.049f, 2f, 2.323f)
                verticalLineTo(16f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.5f, 0f)
                verticalLineToRelative(-0.01f)
                curveToRelative(1.089f, -0.274f, 2f, -1.133f, 2f, -2.323f)
                curveToRelative(0f, -1.457f, -1.365f, -2.417f, -2.75f, -2.417f)
                curveToRelative(-0.824f, 0f, -1.25f, -0.533f, -1.25f, -0.917f)
                reflectiveCurveToRelative(0.426f, -0.916f, 1.25f, -0.916f)
                reflectiveCurveToRelative(1.25f, 0.532f, 1.25f, 0.916f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.5f, 0f)
                curveToRelative(0f, -1.19f, -0.911f, -2.048f, -2f, -2.323f)
                close()
            }
        }.build()
        return _IcChatMoney!!
    }

@Suppress("ObjectPropertyName")
private var _IcChatMoney: ImageVector? = null