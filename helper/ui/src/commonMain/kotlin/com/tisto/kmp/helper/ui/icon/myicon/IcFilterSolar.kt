package com.tisto.kmp.helper.ui.icon.myicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon

val MyIcon.IcFilterSolar: ImageVector
    get() {
        if (_IcFilter != null) {
            return _IcFilter!!
        }
        _IcFilter = ImageVector.Builder(
            name = "IcFilter",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.058f, 9.723f)
                curveTo(21.006f, 9.189f, 21.481f, 8.922f, 21.74f, 8.491f)
                curveTo(22f, 8.061f, 22f, 7.542f, 22f, 6.504f)
                verticalLineTo(5.814f)
                curveTo(22f, 4.488f, 22f, 3.824f, 21.56f, 3.412f)
                curveTo(21.122f, 3f, 20.415f, 3f, 19f, 3f)
                horizontalLineTo(5f)
                curveTo(3.586f, 3f, 2.879f, 3f, 2.44f, 3.412f)
                curveTo(2f, 3.824f, 2f, 4.488f, 2f, 5.815f)
                verticalLineTo(6.505f)
                curveTo(2f, 7.542f, 2f, 8.061f, 2.26f, 8.491f)
                curveTo(2.52f, 8.921f, 2.993f, 9.189f, 3.942f, 9.723f)
                lineTo(6.855f, 11.363f)
                curveTo(7.491f, 11.721f, 7.81f, 11.9f, 8.038f, 12.098f)
                curveTo(8.512f, 12.509f, 8.804f, 12.993f, 8.936f, 13.588f)
                curveTo(9f, 13.872f, 9f, 14.206f, 9f, 14.873f)
                verticalLineTo(17.543f)
                curveTo(9f, 18.452f, 9f, 18.907f, 9.252f, 19.261f)
                curveTo(9.504f, 19.616f, 9.952f, 19.791f, 10.846f, 20.141f)
                curveTo(12.725f, 20.875f, 13.664f, 21.242f, 14.332f, 20.824f)
                curveTo(15f, 20.407f, 15f, 19.452f, 15f, 17.542f)
                verticalLineTo(14.872f)
                curveTo(15f, 14.206f, 15f, 13.872f, 15.064f, 13.587f)
                curveTo(15.197f, 12.993f, 15.489f, 12.508f, 15.963f, 12.097f)
            }
        }.build()

        return _IcFilter!!
    }

@Suppress("ObjectPropertyName")
private var _IcFilter: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(
            imageVector = MyIcon.IcFilterSolar,
            contentDescription = "Filter Icon"
        )
    }
}