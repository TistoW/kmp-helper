package com.tisto.kmp.helper.ui.icon.myicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
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

public val MyIcon.IcCameraSolar: ImageVector
    get() {
        if (_icCameraSolar != null) {
            return _icCameraSolar!!
        }
        _icCameraSolar = Builder(name = "IcCameraSolar", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd) {
                moveTo(12.0f, 10.25f)
                arcToRelative(0.75f, 0.75f, 0.0f, false, true, 0.75f, 0.75f)
                verticalLineToRelative(1.25f)
                horizontalLineTo(14.0f)
                arcToRelative(0.75f, 0.75f, 0.0f, false, true, 0.0f, 1.5f)
                horizontalLineToRelative(-1.25f)
                verticalLineTo(15.0f)
                arcToRelative(0.75f, 0.75f, 0.0f, false, true, -1.5f, 0.0f)
                verticalLineToRelative(-1.25f)
                horizontalLineTo(10.0f)
                arcToRelative(0.75f, 0.75f, 0.0f, false, true, 0.0f, -1.5f)
                horizontalLineToRelative(1.25f)
                verticalLineTo(11.0f)
                arcToRelative(0.75f, 0.75f, 0.0f, false, true, 0.75f, -0.75f)
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd) {
                moveTo(9.778f, 21.0f)
                horizontalLineToRelative(4.444f)
                curveToRelative(3.121f, 0.0f, 4.682f, 0.0f, 5.803f, -0.735f)
                arcToRelative(4.4f, 4.4f, 0.0f, false, false, 1.226f, -1.204f)
                curveToRelative(0.749f, -1.1f, 0.749f, -2.633f, 0.749f, -5.697f)
                reflectiveCurveToRelative(0.0f, -4.597f, -0.749f, -5.697f)
                arcToRelative(4.4f, 4.4f, 0.0f, false, false, -1.226f, -1.204f)
                curveToRelative(-0.72f, -0.473f, -1.622f, -0.642f, -3.003f, -0.702f)
                curveToRelative(-0.659f, 0.0f, -1.226f, -0.49f, -1.355f, -1.125f)
                arcTo(2.064f, 2.064f, 0.0f, false, false, 13.634f, 3.0f)
                horizontalLineToRelative(-3.268f)
                curveToRelative(-0.988f, 0.0f, -1.839f, 0.685f, -2.033f, 1.636f)
                curveToRelative(-0.129f, 0.635f, -0.696f, 1.125f, -1.355f, 1.125f)
                curveToRelative(-1.38f, 0.06f, -2.282f, 0.23f, -3.003f, 0.702f)
                arcTo(4.4f, 4.4f, 0.0f, false, false, 2.75f, 7.667f)
                curveTo(2.0f, 8.767f, 2.0f, 10.299f, 2.0f, 13.364f)
                reflectiveCurveToRelative(0.0f, 4.596f, 0.749f, 5.697f)
                curveToRelative(0.324f, 0.476f, 0.74f, 0.885f, 1.226f, 1.204f)
                curveTo(5.096f, 21.0f, 6.657f, 21.0f, 9.778f, 21.0f)
                moveTo(16.0f, 13.0f)
                arcToRelative(4.0f, 4.0f, 0.0f, true, true, -8.0f, 0.0f)
                arcToRelative(4.0f, 4.0f, 0.0f, false, true, 8.0f, 0.0f)
                moveToRelative(2.0f, -3.75f)
                arcToRelative(0.75f, 0.75f, 0.0f, false, false, 0.0f, 1.5f)
                horizontalLineToRelative(1.0f)
                arcToRelative(0.75f, 0.75f, 0.0f, false, false, 0.0f, -1.5f)
                close()
            }
        }
        .build()
        return _icCameraSolar!!
    }

private var _icCameraSolar: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcCameraSolar, contentDescription = "")
    }
}
