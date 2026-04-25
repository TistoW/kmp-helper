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

public val MyIcon.IcGoogle: ImageVector
    get() {
        if (_icGoogle != null) {
            return _icGoogle!!
        }
        _icGoogle = Builder(name = "IcGoogle", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 256.0f, viewportHeight = 262.0f).apply {
            path(fill = SolidColor(Color(0xFF4285F4)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(255.878f, 133.451f)
                curveToRelative(0.0f, -10.734f, -0.871f, -18.567f, -2.756f, -26.69f)
                horizontalLineTo(130.55f)
                verticalLineToRelative(48.448f)
                horizontalLineToRelative(71.947f)
                curveToRelative(-1.45f, 12.04f, -9.283f, 30.172f, -26.69f, 42.356f)
                lineToRelative(-0.244f, 1.622f)
                lineToRelative(38.755f, 30.023f)
                lineToRelative(2.685f, 0.268f)
                curveToRelative(24.659f, -22.774f, 38.875f, -56.282f, 38.875f, -96.027f)
            }
            path(fill = SolidColor(Color(0xFF34A853)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(130.55f, 261.1f)
                curveToRelative(35.248f, 0.0f, 64.839f, -11.605f, 86.453f, -31.622f)
                lineToRelative(-41.196f, -31.913f)
                curveToRelative(-11.024f, 7.688f, -25.82f, 13.055f, -45.257f, 13.055f)
                curveToRelative(-34.523f, 0.0f, -63.824f, -22.773f, -74.269f, -54.25f)
                lineToRelative(-1.531f, 0.13f)
                lineToRelative(-40.298f, 31.187f)
                lineToRelative(-0.527f, 1.465f)
                curveTo(35.393f, 231.798f, 79.49f, 261.1f, 130.55f, 261.1f)
            }
            path(fill = SolidColor(Color(0xFFFBBC05)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(56.281f, 156.37f)
                curveToRelative(-2.756f, -8.123f, -4.351f, -16.827f, -4.351f, -25.82f)
                curveToRelative(0.0f, -8.994f, 1.595f, -17.697f, 4.206f, -25.82f)
                lineToRelative(-0.073f, -1.73f)
                lineTo(15.26f, 71.312f)
                lineToRelative(-1.335f, 0.635f)
                curveTo(5.077f, 89.644f, 0.0f, 109.517f, 0.0f, 130.55f)
                reflectiveCurveToRelative(5.077f, 40.905f, 13.925f, 58.602f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEB4335)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(130.55f, 50.479f)
                curveToRelative(24.514f, 0.0f, 41.05f, 10.589f, 50.479f, 19.438f)
                lineToRelative(36.844f, -35.974f)
                curveTo(195.245f, 12.91f, 165.798f, 0.0f, 130.55f, 0.0f)
                curveTo(79.49f, 0.0f, 35.393f, 29.301f, 13.925f, 71.947f)
                lineToRelative(42.211f, 32.783f)
                curveToRelative(10.59f, -31.477f, 39.891f, -54.251f, 74.414f, -54.251f)
            }
        }
        .build()
        return _icGoogle!!
    }

private var _icGoogle: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcGoogle, contentDescription = "")
    }
}
