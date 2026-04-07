package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing

@Composable
fun ButtonNormal(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextAppearance.body2Bold(),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    horizontalContentPadding: Dp = Spacing.box,
    backgroundColor: Color = Colors.ColorPrimary,
    contentColor: Color = Color.White,
    disabledBackgroundColor: Color = Colors.Gray4,
    strokeWidth: Dp = 0.dp, // ✅ 0.dp = filled, >0.dp = outlined
    elevation: Dp = 0.dp, // ✅ 0.dp = filled, >0.dp = outlined
    cornerRadius: Dp = Radius.normal,
    strokeColor: Color = Colors.ColorPrimary,
    textColor: Color = Colors.White,
    imageVector: ImageVector? = null,
    imageTint: Color = Colors.White,
    onClick: () -> Unit
) {
    val isOutlined = strokeWidth > 0.dp

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isOutlined) Color.Transparent else {
                if (enabled && !isLoading) backgroundColor else disabledBackgroundColor
            },
            contentColor = if (isOutlined) strokeColor else contentColor,
            // ✅ Disable hover/pressed colors
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = Colors.Gray3
        ),
        border = if (isOutlined) {
            BorderStroke(strokeWidth, if (enabled) strokeColor else Colors.Gray4)
        } else null,
        enabled = enabled && !isLoading,
        contentPadding = PaddingValues(horizontal = horizontalContentPadding, vertical = 0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 0.dp,  // ✅ No elevation change
            hoveredElevation = 0.dp,  // ✅ No elevation on hover
            focusedElevation = 0.dp   // ✅ No elevation on focus
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = if (isOutlined) strokeColor else contentColor,
                strokeWidth = 2.dp
            )
        } else {

            if (imageVector != null) {
                Icon(imageVector, null, tint = imageTint)
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                style = textStyle,
                textAlign = TextAlign.Center,
                color = if (enabled) textColor else Colors.Gray3
            )
        }
    }
}