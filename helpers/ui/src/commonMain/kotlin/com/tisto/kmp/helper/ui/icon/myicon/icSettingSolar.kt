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

val MyIcon.IcSettingSolar: ImageVector
    get() {
        if (_IcSettingSolar != null) {
            return _IcSettingSolar!!
        }
        _IcSettingSolar = ImageVector.Builder(
            name = "IcSettingSolar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Gear/cog outer shape (alpha 0.5, evenOdd)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(14.279f, 2.152f)
                curveTo(13.909f, 2f, 13.439f, 2f, 12.5f, 2f)
                reflectiveCurveToRelative(-1.408f, 0f, -1.779f, 0.152f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.09f, 1.083f)
                curveToRelative(-0.094f, 0.223f, -0.13f, 0.484f, -0.145f, 0.863f)
                arcToRelative(1.62f, 1.62f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.796f, 1.353f)
                arcToRelative(1.64f, 1.64f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.579f, 0.008f)
                curveToRelative(-0.338f, -0.178f, -0.583f, -0.276f, -0.825f, -0.308f)
                arcToRelative(2.03f, 2.03f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.49f, 0.396f)
                curveToRelative(-0.318f, 0.242f, -0.553f, 0.646f, -1.022f, 1.453f)
                curveToRelative(-0.47f, 0.807f, -0.704f, 1.21f, -0.757f, 1.605f)
                curveToRelative(-0.07f, 0.526f, 0.074f, 1.058f, 0.4f, 1.479f)
                curveToRelative(0.148f, 0.192f, 0.357f, 0.353f, 0.68f, 0.555f)
                curveToRelative(0.477f, 0.297f, 0.783f, 0.803f, 0.783f, 1.361f)
                reflectiveCurveToRelative(-0.306f, 1.064f, -0.782f, 1.36f)
                curveToRelative(-0.324f, 0.203f, -0.533f, 0.364f, -0.682f, 0.556f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.399f, 1.479f)
                curveToRelative(0.053f, 0.394f, 0.287f, 0.798f, 0.757f, 1.605f)
                reflectiveCurveToRelative(0.704f, 1.21f, 1.022f, 1.453f)
                curveToRelative(0.424f, 0.323f, 0.96f, 0.465f, 1.49f, 0.396f)
                curveToRelative(0.242f, -0.032f, 0.487f, -0.13f, 0.825f, -0.308f)
                arcToRelative(1.64f, 1.64f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.58f, 0.008f)
                curveToRelative(0.486f, 0.28f, 0.774f, 0.795f, 0.795f, 1.353f)
                curveToRelative(0.015f, 0.38f, 0.051f, 0.64f, 0.145f, 0.863f)
                curveToRelative(0.204f, 0.49f, 0.596f, 0.88f, 1.09f, 1.083f)
                curveToRelative(0.37f, 0.152f, 0.84f, 0.152f, 1.779f, 0.152f)
                reflectiveCurveToRelative(1.409f, 0f, 1.779f, -0.152f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.09f, -1.083f)
                curveToRelative(0.094f, -0.223f, 0.13f, -0.483f, 0.145f, -0.863f)
                curveToRelative(0.02f, -0.558f, 0.309f, -1.074f, 0.796f, -1.353f)
                arcToRelative(1.64f, 1.64f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.579f, -0.008f)
                curveToRelative(0.338f, 0.178f, 0.583f, 0.276f, 0.825f, 0.308f)
                curveToRelative(0.53f, 0.07f, 1.066f, -0.073f, 1.49f, -0.396f)
                curveToRelative(0.318f, -0.242f, 0.553f, -0.646f, 1.022f, -1.453f)
                curveToRelative(0.47f, -0.807f, 0.704f, -1.21f, 0.757f, -1.605f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.4f, -1.479f)
                curveToRelative(-0.148f, -0.192f, -0.357f, -0.353f, -0.68f, -0.555f)
                curveToRelative(-0.477f, -0.297f, -0.783f, -0.803f, -0.783f, -1.361f)
                reflectiveCurveToRelative(0.306f, -1.064f, 0.782f, -1.36f)
                curveToRelative(0.324f, -0.203f, 0.533f, -0.364f, 0.682f, -0.556f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.399f, -1.479f)
                curveToRelative(-0.053f, -0.394f, -0.287f, -0.798f, -0.757f, -1.605f)
                reflectiveCurveToRelative(-0.704f, -1.21f, -1.022f, -1.453f)
                arcToRelative(2.03f, 2.03f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.49f, -0.396f)
                curveToRelative(-0.242f, 0.032f, -0.487f, 0.13f, -0.825f, 0.308f)
                arcToRelative(1.64f, 1.64f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.58f, -0.008f)
                arcToRelative(1.62f, 1.62f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.795f, -1.353f)
                curveToRelative(-0.015f, -0.38f, -0.051f, -0.64f, -0.145f, -0.863f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.09f, -1.083f)
            }

            // Path 2 - Center circle (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(15.523f, 12f)
                curveToRelative(0f, 1.657f, -1.354f, 3f, -3.023f, 3f)
                reflectiveCurveToRelative(-3.023f, -1.343f, -3.023f, -3f)
                reflectiveCurveTo(10.83f, 9f, 12.5f, 9f)
                reflectiveCurveToRelative(3.023f, 1.343f, 3.023f, 3f)
            }
        }.build()
        return _IcSettingSolar!!
    }

@Suppress("ObjectPropertyName")
private var _IcSettingSolar: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcSettingSolar, contentDescription = "Settings Icon")
    }
}