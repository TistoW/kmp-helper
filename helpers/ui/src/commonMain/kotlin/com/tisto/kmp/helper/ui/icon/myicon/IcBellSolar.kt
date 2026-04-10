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
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon
import kotlin.Unit

public val MyIcon.IcBellSolar: ImageVector
    get() {
        if (_icBellSolar != null) {
            return _icBellSolar!!
        }
        _icBellSolar = Builder(name = "IcBellSolar", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), fillAlpha = 0.5f, stroke = null,
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(18.75f, 9.0f)
                verticalLineToRelative(0.704f)
                curveToRelative(0.0f, 0.845f, 0.24f, 1.671f, 0.692f, 2.374f)
                lineToRelative(1.108f, 1.723f)
                curveToRelative(1.011f, 1.574f, 0.239f, 3.713f, -1.52f, 4.21f)
                arcToRelative(25.8f, 25.8f, 0.0f, false, true, -14.06f, 0.0f)
                curveToRelative(-1.759f, -0.497f, -2.531f, -2.636f, -1.52f, -4.21f)
                lineToRelative(1.108f, -1.723f)
                arcToRelative(4.4f, 4.4f, 0.0f, false, false, 0.693f, -2.374f)
                verticalLineTo(9.0f)
                curveToRelative(0.0f, -3.866f, 3.022f, -7.0f, 6.749f, -7.0f)
                reflectiveCurveToRelative(6.75f, 3.134f, 6.75f, 7.0f)
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.243f, 18.545f)
                arcToRelative(5.002f, 5.002f, 0.0f, false, false, 9.513f, 0.0f)
                curveToRelative(-3.145f, 0.59f, -6.367f, 0.59f, -9.513f, 0.0f)
            }
        }
        .build()
        return _icBellSolar!!
    }

private var _icBellSolar: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcBellSolar, contentDescription = "")
    }
}