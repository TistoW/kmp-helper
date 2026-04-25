package com.tisto.kmp.helper.ui.icon.myicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon
import kotlin.Unit

public val MyIcon.IcPenBrokenSolar: ImageVector
    get() {
        if (_icPenBrokenSolar != null) {
            return _icPenBrokenSolar!!
        }
        _icPenBrokenSolar = Builder(name = "IcPenBrokenSolar", defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            // stroke-linecap="round" is on the group — all paths use Round
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(2.0f, 12.0f)
                curveToRelative(0.0f, 4.714f, 0.0f, 7.071f, 1.464f, 8.535f)
                curveTo(4.93f, 22.0f, 7.286f, 22.0f, 12.0f, 22.0f)
                reflectiveCurveToRelative(7.071f, 0.0f, 8.535f, -1.465f)
                curveTo(22.0f, 19.072f, 22.0f, 16.714f, 22.0f, 12.0f)
                verticalLineToRelative(-1.5f)
                moveTo(13.5f, 2.0f)
                horizontalLineTo(12.0f)
                curveTo(7.286f, 2.0f, 4.929f, 2.0f, 3.464f, 3.464f)
                curveToRelative(-0.973f, 0.974f, -1.3f, 2.343f, -1.409f, 4.536f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(16.652f, 3.455f)
                lineToRelative(0.649f, -0.649f)
                arcTo(2.753f, 2.753f, 0.0f, false, true, 21.194f, 6.7f)
                lineToRelative(-0.65f, 0.649f)
                moveToRelative(-3.892f, -3.893f)
                reflectiveCurveToRelative(0.081f, 1.379f, 1.298f, 2.595f)
                curveToRelative(1.216f, 1.217f, 2.595f, 1.298f, 2.595f, 1.298f)
                moveToRelative(-3.893f, -3.893f)
                lineTo(10.687f, 9.42f)
                curveToRelative(-0.404f, 0.404f, -0.606f, 0.606f, -0.78f, 0.829f)
                quadToRelative(-0.308f, 0.395f, -0.524f, 0.848f)
                curveToRelative(-0.121f, 0.255f, -0.211f, 0.526f, -0.392f, 1.068f)
                lineTo(8.412f, 13.9f)
                moveToRelative(12.133f, -6.552f)
                lineToRelative(-2.983f, 2.982f)
                moveToRelative(-2.982f, 2.983f)
                curveToRelative(-0.404f, 0.404f, -0.606f, 0.606f, -0.829f, 0.78f)
                arcToRelative(4.6f, 4.6f, 0.0f, false, true, -0.848f, 0.524f)
                curveToRelative(-0.255f, 0.121f, -0.526f, 0.211f, -1.068f, 0.392f)
                lineToRelative(-1.735f, 0.579f)
                moveToRelative(0.0f, 0.0f)
                lineToRelative(-1.123f, 0.374f)
                arcToRelative(0.742f, 0.742f, 0.0f, false, true, -0.939f, -0.94f)
                lineToRelative(0.374f, -1.122f)
                moveToRelative(1.688f, 1.688f)
                lineTo(8.412f, 13.9f)
            }
        }
        .build()
        return _icPenBrokenSolar!!
    }

private var _icPenBrokenSolar: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcPenBrokenSolar, contentDescription = "")
    }
}
