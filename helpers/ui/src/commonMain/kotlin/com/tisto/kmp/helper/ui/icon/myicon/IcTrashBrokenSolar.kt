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

public val MyIcon.IcTrashBrokenSolar: ImageVector
    get() {
        if (_icTrashBrokenSolar != null) {
            return _icTrashBrokenSolar!!
        }
        _icTrashBrokenSolar = Builder(name = "IcTrashBrokenSolar", defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(20.5f, 6.0f)
                horizontalLineToRelative(-17.0f)
                moveToRelative(6.0f, 5.0f)
                lineToRelative(0.5f, 5.0f)
                moveToRelative(4.5f, -5.0f)
                lineToRelative(-0.5f, 5.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(6.5f, 6.0f)
                horizontalLineToRelative(0.11f)
                arcToRelative(2.0f, 2.0f, 0.0f, false, false, 1.83f, -1.32f)
                lineToRelative(0.034f, -0.103f)
                lineToRelative(0.097f, -0.291f)
                curveToRelative(0.083f, -0.249f, 0.125f, -0.373f, 0.18f, -0.479f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, true, 1.094f, -0.788f)
                curveTo(9.962f, 3.0f, 10.093f, 3.0f, 10.355f, 3.0f)
                horizontalLineToRelative(3.29f)
                curveToRelative(0.262f, 0.0f, 0.393f, 0.0f, 0.51f, 0.019f)
                arcToRelative(1.5f, 1.5f, 0.0f, false, true, 1.094f, 0.788f)
                curveToRelative(0.055f, 0.106f, 0.097f, 0.23f, 0.18f, 0.479f)
                lineToRelative(0.097f, 0.291f)
                arcTo(2.0f, 2.0f, 0.0f, false, false, 17.5f, 6.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(18.374f, 15.4f)
                curveToRelative(-0.177f, 2.654f, -0.266f, 3.981f, -1.131f, 4.79f)
                reflectiveCurveToRelative(-2.195f, 0.81f, -4.856f, 0.81f)
                horizontalLineToRelative(-0.774f)
                curveToRelative(-2.66f, 0.0f, -3.99f, 0.0f, -4.856f, -0.81f)
                curveToRelative(-0.865f, -0.809f, -0.953f, -2.136f, -1.13f, -4.79f)
                lineToRelative(-0.46f, -6.9f)
                moveToRelative(13.666f, 0.0f)
                lineToRelative(-0.2f, 3.0f)
            }
        }
        .build()
        return _icTrashBrokenSolar!!
    }

private var _icTrashBrokenSolar: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcTrashBrokenSolar, contentDescription = "")
    }
}
