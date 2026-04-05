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


val MyIcon.IcSearch: ImageVector
    get() {
        if (_IcSearch != null) {
            return _IcSearch!!
        }
        _IcSearch = ImageVector.Builder(
            name = "IcSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(21.71f, 20.29f)
                lineTo(18f, 16.61f)
                arcTo(9f, 9f, 0f, isMoreThanHalf = true, isPositiveArc = false, 16.61f, 18f)
                lineToRelative(3.68f, 3.68f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.42f, 0f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1.39f)
                moveTo(11f, 18f)
                arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, 7f, -7f)
                arcToRelative(7f, 7f, 0f, isMoreThanHalf = false, isPositiveArc = true, -7f, 7f)
            }
        }.build()

        return _IcSearch!!
    }

@Suppress("ObjectPropertyName")
private var _IcSearch: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcSearch, contentDescription = "")
    }
}
