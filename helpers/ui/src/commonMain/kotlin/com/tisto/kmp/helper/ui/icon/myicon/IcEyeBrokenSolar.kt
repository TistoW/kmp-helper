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

public val MyIcon.IcEyeBrokenSolar: ImageVector
    get() {
        if (_icEyeBrokenSolar != null) {
            return _icEyeBrokenSolar!!
        }
        _icEyeBrokenSolar = Builder(name = "IcEyeBrokenSolar", defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(9.0f, 4.46f)
                arcTo(9.8f, 9.8f, 0.0f, false, true, 12.0f, 4.0f)
                curveToRelative(4.182f, 0.0f, 7.028f, 2.5f, 8.725f, 4.704f)
                curveTo(21.575f, 9.81f, 22.0f, 10.361f, 22.0f, 12.0f)
                curveToRelative(0.0f, 1.64f, -0.425f, 2.191f, -1.275f, 3.296f)
                curveTo(19.028f, 17.5f, 16.182f, 20.0f, 12.0f, 20.0f)
                reflectiveCurveToRelative(-7.028f, -2.5f, -8.725f, -4.704f)
                curveTo(2.425f, 14.192f, 2.0f, 13.639f, 2.0f, 12.0f)
                curveToRelative(0.0f, -1.64f, 0.425f, -2.191f, 1.275f, -3.296f)
                arcTo(14.5f, 14.5f, 0.0f, false, true, 5.0f, 6.821f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(15.0f, 12.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -6.0f, 0.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, false, true, 6.0f, 0.0f)
                close()
            }
        }
        .build()
        return _icEyeBrokenSolar!!
    }

private var _icEyeBrokenSolar: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcEyeBrokenSolar, contentDescription = "")
    }
}
