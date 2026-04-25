package com.tisto.kmp.helper.ui.icon.myicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon
import kotlin.Unit

public val MyIcon.IcPen2Solar: ImageVector
    get() {
        if (_icPen2Solar != null) {
            return _icPen2Solar!!
        }
        _icPen2Solar = Builder(name = "IcPen2Solar", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeAlpha = 0.5f, strokeLineWidth = 1.5f, strokeLineCap = Round,
                    strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(22.0f, 10.5f)
                verticalLineTo(12.0f)
                curveToRelative(0.0f, 4.714f, 0.0f, 7.071f, -1.465f, 8.535f)
                curveTo(19.072f, 22.0f, 16.714f, 22.0f, 12.0f, 22.0f)
                reflectiveCurveToRelative(-7.071f, 0.0f, -8.536f, -1.465f)
                curveTo(2.0f, 19.072f, 2.0f, 16.714f, 2.0f, 12.0f)
                reflectiveCurveToRelative(0.0f, -7.071f, 1.464f, -8.536f)
                curveTo(4.93f, 2.0f, 7.286f, 2.0f, 12.0f, 2.0f)
                horizontalLineToRelative(1.5f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(17.3f, 2.806f)
                lineToRelative(-0.648f, 0.65f)
                lineToRelative(-5.965f, 5.964f)
                curveToRelative(-0.404f, 0.404f, -0.606f, 0.606f, -0.78f, 0.829f)
                quadToRelative(-0.308f, 0.395f, -0.524f, 0.848f)
                curveToRelative(-0.121f, 0.255f, -0.211f, 0.526f, -0.392f, 1.068f)
                lineTo(8.412f, 13.9f)
                lineToRelative(-0.374f, 1.123f)
                arcToRelative(0.742f, 0.742f, 0.0f, false, false, 0.94f, 0.939f)
                lineToRelative(1.122f, -0.374f)
                lineToRelative(1.735f, -0.579f)
                curveToRelative(0.542f, -0.18f, 0.813f, -0.27f, 1.068f, -0.392f)
                quadToRelative(0.453f, -0.217f, 0.848f, -0.524f)
                curveToRelative(0.223f, -0.174f, 0.425f, -0.376f, 0.83f, -0.78f)
                lineToRelative(5.964f, -5.965f)
                lineToRelative(0.649f, -0.649f)
                arcTo(2.753f, 2.753f, 0.0f, false, false, 17.3f, 2.806f)
                close()
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeAlpha = 0.5f, strokeLineWidth = 1.5f, strokeLineCap = Butt,
                    strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(16.652f, 3.455f)
                reflectiveCurveToRelative(0.081f, 1.379f, 1.298f, 2.595f)
                curveToRelative(1.216f, 1.217f, 2.595f, 1.298f, 2.595f, 1.298f)
                moveTo(10.1f, 15.588f)
                lineTo(8.413f, 13.9f)
            }
        }
        .build()
        return _icPen2Solar!!
    }

private var _icPen2Solar: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcPen2Solar, contentDescription = "")
    }
}
