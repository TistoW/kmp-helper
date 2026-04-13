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
val MyIcon.IcCloseSimple: ImageVector
    get() {
        if (_IcCloseSimple != null) {
            return _IcCloseSimple!!
        }
        _IcCloseSimple = ImageVector.Builder(
            name = "IcCloseSimple",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0.361f, 0.361f, 0.361f)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8.94f, 8f)
                lineTo(13.14f, 3.8067f)
                curveTo(13.2655f, 3.6811f, 13.3361f, 3.5109f, 13.3361f, 3.3333f)
                curveTo(13.3361f, 3.1558f, 13.2655f, 2.9855f, 13.14f, 2.86f)
                curveTo(13.0145f, 2.7345f, 12.8442f, 2.6639f, 12.6667f, 2.6639f)
                curveTo(12.4891f, 2.6639f, 12.3189f, 2.7345f, 12.1933f, 2.86f)
                lineTo(8f, 7.06f)
                lineTo(3.8067f, 2.86f)
                curveTo(3.6811f, 2.7345f, 3.5109f, 2.6639f, 3.3333f, 2.6639f)
                curveTo(3.1558f, 2.6639f, 2.9855f, 2.7345f, 2.86f, 2.86f)
                curveTo(2.7345f, 2.9855f, 2.6639f, 3.1558f, 2.6639f, 3.3333f)
                curveTo(2.6639f, 3.5109f, 2.7345f, 3.6811f, 2.86f, 3.8067f)
                lineTo(7.06f, 8f)
                lineTo(2.86f, 12.1933f)
                curveTo(2.7975f, 12.2553f, 2.7479f, 12.329f, 2.7141f, 12.4103f)
                curveTo(2.6802f, 12.4915f, 2.6628f, 12.5787f, 2.6628f, 12.6667f)
                curveTo(2.6628f, 12.7547f, 2.6802f, 12.8418f, 2.7141f, 12.9231f)
                curveTo(2.7479f, 13.0043f, 2.7975f, 13.078f, 2.86f, 13.14f)
                curveTo(2.922f, 13.2025f, 2.9957f, 13.2521f, 3.0769f, 13.2859f)
                curveTo(3.1582f, 13.3198f, 3.2453f, 13.3372f, 3.3333f, 13.3372f)
                curveTo(3.4213f, 13.3372f, 3.5085f, 13.3198f, 3.5897f, 13.2859f)
                curveTo(3.6709f, 13.2521f, 3.7447f, 13.2025f, 3.8067f, 13.14f)
                lineTo(8f, 8.94f)
                lineTo(12.1933f, 13.14f)
                curveTo(12.2553f, 13.2025f, 12.329f, 13.2521f, 12.4103f, 13.2859f)
                curveTo(12.4915f, 13.3198f, 12.5787f, 13.3372f, 12.6667f, 13.3372f)
                curveTo(12.7547f, 13.3372f, 12.8418f, 13.3198f, 12.923f, 13.2859f)
                curveTo(13.0043f, 13.2521f, 13.078f, 13.2025f, 13.14f, 13.14f)
                curveTo(13.2025f, 13.078f, 13.2521f, 13.0043f, 13.2859f, 12.9231f)
                curveTo(13.3198f, 12.8418f, 13.3372f, 12.7547f, 13.3372f, 12.6667f)
                curveTo(13.3372f, 12.5787f, 13.3198f, 12.4915f, 13.2859f, 12.4103f)
                curveTo(13.2521f, 12.329f, 13.2025f, 12.2553f, 13.14f, 12.1933f)
                lineTo(8.94f, 8f)
                close()
            }
        }.build()
        return _IcCloseSimple!!
    }

@Suppress("ObjectPropertyName")
private var _IcCloseSimple: ImageVector? = null