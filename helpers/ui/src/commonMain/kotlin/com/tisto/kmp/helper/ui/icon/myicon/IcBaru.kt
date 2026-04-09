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

val MyIcon.IcClose: ImageVector
    get() {
        if (_IcClose != null) {
            return _IcClose!!
        }
        _IcClose = ImageVector.Builder(
            name = "IcClose",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 4000f,
            viewportHeight = 4000f
        ).apply {

            path(
                fill = SolidColor(Color(0.204f, 0.251f, 0.490f)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(1744f, 124f)
                curveToRelative(-387f, 404f, -775f, 808f, -1162f, 1212f)
                close()
                moveTo(1806f, 3185f)
                curveToRelative(-5f, 54f, 14f, 109f, 55f, 150f)
                lineToRelative(0f, 0f)
                curveToRelative(22f, 20f, 46f, 35f, 73f, 44f)
                lineToRelative(0f, 0f)
                curveToRelative(58f, 1f, 110f, -26f, 145f, -67f)
                lineToRelative(0f, 0f)
                curveToRelative(-41f, 43f, -109f, 45f, -153f, 4f)
                lineToRelative(0f, -46f)
                curveToRelative(3f, -440f, 8f, -1282f, 11f, -1636f)
                lineToRelative(0f, 0f)
                curveToRelative(-31f, -1f, -61f, 7f, -87f, 20f)
                lineToRelative(0f, 0f)
                curveToRelative(41f, -43f, 109f, -45f, 153f, -5f)
                lineToRelative(0f, 0f)
                curveToRelative(-3f, 556f, -7f, 1112f, -9f, 1668f)
                close()
            }


            path(
                fill = SolidColor(Color(0.996f, 0.996f, 0.996f)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(1806f, 3184f)
                curveToRelative(-5f, 55f, 14f, 110f, 55f, 151f)
                lineToRelative(0f, 0f)
                curveToRelative(22f, 20f, 46f, 35f, 73f, 44f)
                lineToRelative(0f, 0f)
                curveToRelative(58f, 1f, 110f, -26f, 145f, -67f)
                lineToRelative(0f, 0f)
                curveToRelative(-41f, 43f, -109f, 45f, -153f, 4f)
                lineToRelative(0f, -46f)
                curveToRelative(3f, -440f, 8f, -1282f, 11f, -1636f)
                lineToRelative(0f, 0f)
                curveToRelative(-31f, -1f, -61f, 7f, -87f, 20f)
                lineToRelative(0f, 0f)
                curveToRelative(41f, -43f, 109f, -45f, 153f, -5f)
                lineToRelative(0f, 0f)
                curveToRelative(-3f, 556f, -7f, 1112f, -9f, 1667f)
                close()
            }
        }.build()
        return _IcClose!!
    }

@Suppress("ObjectPropertyName")
private var _IcClose: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcClose, contentDescription = "IcClose")
    }
}
