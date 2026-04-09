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

val MyIcon.IcChecklistSolar: ImageVector
    get() {
        if (_IcChecklistSolar != null) {
            return _IcChecklistSolar!!
        }
        _IcChecklistSolar = ImageVector.Builder(
            name = "IcChecklistSolar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 - Background rounded square (alpha 0.5)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0.5f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(2f, 12f)
                curveToRelative(0f, -4.714f, 0f, -7.071f, 1.464f, -8.536f)
                curveTo(4.93f, 2f, 7.286f, 2f, 12f, 2f)
                reflectiveCurveToRelative(7.071f, 0f, 8.535f, 1.464f)
                curveTo(22f, 4.93f, 22f, 7.286f, 22f, 12f)
                reflectiveCurveToRelative(0f, 7.071f, -1.465f, 8.535f)
                curveTo(19.072f, 22f, 16.714f, 22f, 12f, 22f)
                reflectiveCurveToRelative(-7.071f, 0f, -8.536f, -1.465f)
                curveTo(2f, 19.072f, 2f, 16.714f, 2f, 12f)
            }

            // Path 2 - Checkmarks and text lines (no alpha)
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                // First checkmark
                moveTo(10.543f, 7.517f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = false, -1.086f, -1.034f)
                lineToRelative(-2.314f, 2.43f)
                lineToRelative(-0.6f, -0.63f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = false, -1.086f, 1.034f)
                lineToRelative(1.143f, 1.2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.086f, 0f)
                close()

                // First text line
                moveTo(13f, 8.25f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.5f)
                horizontalLineToRelative(5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1.5f)
                close()

                // Second checkmark
                moveToRelative(-2.457f, 6.267f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = false, -1.086f, -1.034f)
                lineToRelative(-2.314f, 2.43f)
                lineToRelative(-0.6f, -0.63f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = true, isPositiveArc = false, -1.086f, 1.034f)
                lineToRelative(1.143f, 1.2f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.086f, 0f)
                close()

                // Second text line
                moveTo(13f, 15.25f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.5f)
                horizontalLineToRelative(5f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1.5f)
                close()
            }
        }.build()
        return _IcChecklistSolar!!
    }

@Suppress("ObjectPropertyName")
private var _IcChecklistSolar: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIcon.IcChecklistSolar, contentDescription = "Checklist Icon")
    }
}