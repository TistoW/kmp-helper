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

val MyIcon.IcLogo: ImageVector
    get() {
        if (_IcComplex != null) {
            return _IcComplex!!
        }
        _IcComplex = ImageVector.Builder(
            name = "IcComplex",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 4000f,
            viewportHeight = 4000f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF34407D)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(1744f, 124f)
                curveToRelative(-387f, 404f, -775f, 808f, -1162f, 1212f)
                curveToRelative(-373f, 390f, -360f, 1014f, 30f, 1388f)
                curveToRelative(404f, 387f, 808f, 774f, 1212f, 1161f)
                curveToRelative(122f, 117f, 316f, 112f, 432f, -9f)
                curveToRelative(387f, -404f, 775f, -808f, 1162f, -1212f)
                curveToRelative(373f, -390f, 360f, -1014f, -30f, -1388f)
                curveToRelative(-404f, -387f, -808f, -774f, -1212f, -1161f)
                curveToRelative(-122f, -117f, -316f, -112f, -432f, 9f)
                close()
                moveTo(1806f, 3185f)
                curveToRelative(-5f, 54f, 14f, 109f, 55f, 150f)
                lineToRelative(0f, 0f)
                curveToRelative(22f, 20f, 46f, 35f, 73f, 44f)
                curveToRelative(18f, 6f, 38f, 9f, 58f, 9f)
                lineToRelative(0f, 0f)
                curveToRelative(58f, 1f, 110f, -26f, 145f, -67f)
                curveToRelative(273f, -280f, 737f, -757f, 1005f, -1033f)
                curveToRelative(144f, -148f, 141f, -387f, -7f, -531f)
                curveToRelative(-212f, 218f, -494f, 508f, -764f, 785f)
                lineToRelative(0f, 0f)
                curveToRelative(-41f, 43f, -109f, 45f, -153f, 4f)
                curveToRelative(-18f, -17f, -30f, -39f, -33f, -63f)
                lineToRelative(0f, -46f)
                curveToRelative(3f, -440f, 8f, -1282f, 11f, -1636f)
                curveToRelative(0f, -104f, -84f, -189f, -187f, -189f)
                lineToRelative(0f, 0f)
                curveToRelative(-31f, -1f, -61f, 7f, -87f, 20f)
                curveToRelative(-17f, 9f, -34f, 21f, -49f, 37f)
                curveToRelative(-271f, 278f, -744f, 764f, -1015f, 1043f)
                curveToRelative(-144f, 148f, -141f, 387f, 7f, 531f)
                curveToRelative(212f, -218f, 494f, -508f, 764f, -785f)
                lineToRelative(0f, 0f)
                curveToRelative(41f, -43f, 109f, -45f, 153f, -5f)
                curveToRelative(18f, 18f, 30f, 40f, 33f, 64f)
                lineToRelative(0f, 0f)
                curveToRelative(-3f, 556f, -7f, 1112f, -9f, 1668f)
                close()
            }

            path(
                fill = SolidColor(Color(0xFFFEFEFE)),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(1806f, 3184f)
                curveToRelative(-5f, 55f, 14f, 110f, 55f, 151f)
                lineToRelative(0f, 0f)
                curveToRelative(22f, 20f, 46f, 35f, 73f, 44f)
                curveToRelative(18f, 6f, 38f, 9f, 58f, 9f)
                lineToRelative(0f, 0f)
                curveToRelative(58f, 1f, 110f, -26f, 145f, -67f)
                curveToRelative(273f, -280f, 737f, -758f, 1005f, -1033f)
                curveToRelative(144f, -148f, 141f, -387f, -7f, -531f)
                curveToRelative(-212f, 218f, -494f, 508f, -764f, 785f)
                lineToRelative(0f, 0f)
                curveToRelative(-41f, 43f, -109f, 45f, -153f, 4f)
                curveToRelative(-18f, -17f, -30f, -39f, -33f, -63f)
                lineToRelative(0f, -46f)
                curveToRelative(3f, -440f, 8f, -1282f, 11f, -1636f)
                curveToRelative(0f, -104f, -84f, -189f, -187f, -189f)
                lineToRelative(0f, 0f)
                curveToRelative(-31f, -1f, -61f, 7f, -87f, 20f)
                curveToRelative(-17f, 9f, -34f, 21f, -49f, 37f)
                curveToRelative(-271f, 278f, -744f, 764f, -1015f, 1043f)
                curveToRelative(-144f, 148f, -141f, 387f, 7f, 531f)
                curveToRelative(212f, -218f, 494f, -508f, 764f, -785f)
                lineToRelative(0f, 0f)
                curveToRelative(41f, -43f, 109f, -45f, 153f, -5f)
                curveToRelative(18f, 18f, 30f, 40f, 33f, 64f)
                lineToRelative(0f, 0f)
                curveToRelative(-3f, 556f, -7f, 1112f, -9f, 1667f)
                close()
            }
        }.build()
        return _IcComplex!!
    }

@Suppress("ObjectPropertyName")
private var _IcComplex: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcLogo, contentDescription = "")
    }
}
