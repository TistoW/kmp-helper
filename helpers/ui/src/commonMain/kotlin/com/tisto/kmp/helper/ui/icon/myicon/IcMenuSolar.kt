package com.tisto.kmp.helper.ui.icon.myicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon

val MyIcon.IcMenuSolar: ImageVector
    get() {
        if (_IcMenu != null) {
            return _IcMenu!!
        }
        _IcMenu = ImageVector.Builder(
            name = "IcMenu",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {

            // path 1 (alpha 0.32)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.32f
            ) {
                moveTo(15.7798f, 4.5f)
                lineTo(5.2202f, 4.5f)
                curveTo(4.27169f, 4.5f, 3.5f, 5.06057f, 3.5f, 5.75042f)
                curveTo(3.5f, 6.43943f, 4.27169f, 7f, 5.2202f, 7f)
                lineTo(15.7798f, 7f)
                curveTo(16.7283f, 7f, 17.5f, 6.43943f, 17.5f, 5.75042f)
                curveTo(17.5f, 5.06054f, 16.7283f, 4.5f, 15.7798f, 4.5f)
                close()
            }

            // path 2
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(18.7798f, 10.75f)
                lineTo(8.2202f, 10.75f)
                curveTo(7.27169f, 10.75f, 6.5f, 11.3106f, 6.5f, 12.0004f)
                curveTo(6.5f, 12.6894f, 7.27169f, 13.25f, 8.2202f, 13.25f)
                lineTo(18.7798f, 13.25f)
                curveTo(19.7283f, 13.25f, 20.5f, 12.6894f, 20.5f, 12.0004f)
                curveTo(20.5f, 11.3105f, 19.7283f, 10.75f, 18.7798f, 10.75f)
                close()
            }

            // path 3
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(15.7798f, 17f)
                lineTo(5.2202f, 17f)
                curveTo(4.27169f, 17f, 3.5f, 17.5606f, 3.5f, 18.2504f)
                curveTo(3.5f, 18.9394f, 4.27169f, 19.5f, 5.2202f, 19.5f)
                lineTo(15.7798f, 19.5f)
                curveTo(16.7283f, 19.5f, 17.5f, 18.9394f, 17.5f, 18.2504f)
                curveTo(17.5f, 17.5606f, 16.7283f, 17f, 15.7798f, 17f)
                close()
            }

        }.build()

        return _IcMenu!!
    }

@Suppress("ObjectPropertyName")
private var _IcMenu: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcMenuSolar, contentDescription = "")
    }
}