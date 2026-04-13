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
val MyIcon.IcWallet: ImageVector
    get() {
        if (_IcWallet != null) {
            return _IcWallet!!
        }
        _IcWallet = ImageVector.Builder(
            name = "IcWallet",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4.892f, 9.614f)
                curveToRelative(0f, -0.402f, 0.323f, -0.728f, 0.722f, -0.728f)
                horizontalLineTo(9.47f)
                curveToRelative(0.4f, 0f, 0.723f, 0.326f, 0.723f, 0.728f)
                arcToRelative(0.726f, 0.726f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.723f, 0.729f)
                horizontalLineTo(5.614f)
                arcToRelative(0.726f, 0.726f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.722f, -0.729f)
            }

            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(21.188f, 10.004f)
                quadToRelative(-0.094f, -0.005f, -0.2f, -0.004f)
                horizontalLineToRelative(-2.773f)
                curveToRelative(-2.271f, 0f, -4.215f, 1.736f, -4.215f, 4f)
                reflectiveCurveToRelative(1.944f, 4f, 4.215f, 4f)
                horizontalLineToRelative(2.773f)
                quadToRelative(0.106f, 0.001f, 0.2f, -0.004f)
                curveToRelative(0.923f, -0.056f, 1.739f, -0.757f, 1.808f, -1.737f)
                curveToRelative(0.004f, -0.064f, 0.004f, -0.133f, 0.004f, -0.197f)
                verticalLineToRelative(-4.124f)
                curveToRelative(0f, -0.064f, 0f, -0.133f, -0.004f, -0.197f)
                curveToRelative(-0.069f, -0.98f, -0.885f, -1.68f, -1.808f, -1.737f)
                moveTo(17.971f, 15.067f)
                curveToRelative(0.584f, 0f, 1.058f, -0.478f, 1.058f, -1.067f)
                curveToRelative(0f, -0.59f, -0.474f, -1.067f, -1.058f, -1.067f)
                reflectiveCurveToRelative(-1.06f, 0.478f, -1.06f, 1.067f)
                curveToRelative(0f, 0.59f, 0.475f, 1.067f, 1.06f, 1.067f)
            }

            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(21.14f, 10.002f)
                curveToRelative(0f, -1.181f, -0.044f, -2.448f, -0.798f, -3.355f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.233f, -0.256f)
                curveToRelative(-0.749f, -0.748f, -1.698f, -1.08f, -2.87f, -1.238f)
                curveToRelative(-1.141f, -0.153f, -2.596f, -0.153f, -4.434f, -0.153f)
                horizontalLineToRelative(-2.112f)
                curveToRelative(-1.838f, 0f, -3.294f, 0f, -4.434f, 0.153f)
                curveToRelative(-1.172f, 0.158f, -2.121f, 0.49f, -2.87f, 1.238f)
                curveToRelative(-0.748f, 0.749f, -1.08f, 1.698f, -1.238f, 2.87f)
                curveToRelative(-0.153f, 1.14f, -0.153f, 2.595f, -0.153f, 4.433f)
                verticalLineToRelative(0.112f)
                curveToRelative(0f, 1.838f, 0f, 3.294f, 0.153f, 4.433f)
                curveToRelative(0.158f, 1.172f, 0.49f, 2.121f, 1.238f, 2.87f)
                curveToRelative(0.749f, 0.748f, 1.698f, 1.08f, 2.87f, 1.238f)
                curveToRelative(1.14f, 0.153f, 2.595f, 0.153f, 4.433f, 0.153f)
                horizontalLineToRelative(2.112f)
                curveToRelative(1.838f, 0f, 3.294f, 0f, 4.433f, -0.153f)
                curveToRelative(1.172f, -0.158f, 2.121f, -0.49f, 2.87f, -1.238f)
                quadToRelative(0.305f, -0.308f, 0.526f, -0.66f)
                curveToRelative(0.45f, -0.72f, 0.504f, -1.602f, 0.504f, -2.45f)
                lineToRelative(-0.15f, 0.001f)
                horizontalLineToRelative(-2.774f)
                curveToRelative(-2.271f, 0f, -4.215f, -1.736f, -4.215f, -4f)
                reflectiveCurveToRelative(1.944f, -4f, 4.215f, -4f)
                horizontalLineToRelative(2.773f)
                quadToRelative(0.079f, 0f, 0.151f, 0.002f)
            }

            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10.101f, 2.572f)
                lineTo(8f, 3.992f)
                lineToRelative(-1.733f, 1.16f)
                curveToRelative(1.138f, -0.152f, 2.592f, -0.152f, 4.427f, -0.152f)
                horizontalLineToRelative(2.112f)
                curveToRelative(1.838f, 0f, 3.294f, 0f, 4.433f, 0.153f)
                quadToRelative(0.344f, 0.045f, 0.662f, 0.114f)
                lineTo(16f, 4f)
                lineToRelative(-2.113f, -1.428f)
                arcToRelative(3.42f, 3.42f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.786f, 0f)
            }
        }.build()
        return _IcWallet!!
    }

@Suppress("ObjectPropertyName")
private var _IcWallet: ImageVector? = null