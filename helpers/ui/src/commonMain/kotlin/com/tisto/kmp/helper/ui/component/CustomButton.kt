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
import androidx.compose.material3.MaterialTheme
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

/**
 * Visual style variants for [ButtonNormal].
 *
 * Use [style] instead of manually wiring color params when possible.
 * For special cases (e.g. `backgroundColor = Color.Black`) keep using
 * the individual override params with the default [Primary] style.
 */
enum class ButtonStyle {
    /** Filled with the app's primary color. Default. */
    Primary,

    /** Outlined border in the primary color, transparent background. */
    Outlined,

    /** Filled red — for destructive/delete actions. */
    Destructive,

    /** Outlined red border — for less prominent destructive actions. */
    OutlinedDestructive,

    /** No background, no border. Text colored in primary. Like a TextButton. */
    Ghost,

    /** Filled with the secondary/accent color (orange). */
    Secondary,

    /** Filled green — for success/confirm actions. */
    Success,
}

/**
 * Universal button component.
 *
 * Quick usage:
 * ```
 * ButtonNormal(text = "Simpan") { ... }
 * ButtonNormal(text = "Hapus", style = ButtonStyle.Destructive) { ... }
 * ButtonNormal(text = "Batal", style = ButtonStyle.Outlined) { ... }
 * ```
 *
 * The individual color/stroke params are legacy overrides and only take effect
 * when [style] == [ButtonStyle.Primary]. Use [style] for all new code.
 */

@Composable
fun NormalButton(
    text: String,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Primary,
    textStyle: TextStyle = TextAppearance.body2Bold(),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    horizontalContentPadding: Dp = Spacing.box,
    verticalContentPadding: Dp = 0.dp,
    imageVector: ImageVector? = null,
    // ── Legacy override params ───────────────────────────────────────────
    // Only applied when style == Primary. Use `style` for all new callers.
    backgroundColor: Color? = null,                    // null = ambil dari theme
    contentColor: Color = Color.White,
    disabledBackgroundColor: Color = Colors.Gray4,
    strokeWidth: Dp = 0.dp,
    elevation: Dp = 0.dp,
    cornerRadius: Dp = Radius.normal,
    strokeColor: Color? = null,                        // null = ambil dari theme
    textColor: Color = Colors.White,
    imageTint: Color = Colors.White,
    contentPadding: PaddingValues? = null,
    onClick: () -> Unit,
) {
    ButtonNormal(
        text = text,
        modifier = modifier,
        style = style,
        textStyle = textStyle,
        enabled = enabled,
        isLoading = isLoading,
        horizontalContentPadding = horizontalContentPadding,
        verticalContentPadding = verticalContentPadding,
        imageVector = imageVector,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        strokeWidth = strokeWidth,
        elevation = elevation,
        cornerRadius = cornerRadius,
        strokeColor = strokeColor,
        textColor = textColor,
        imageTint = imageTint,
        contentPadding = contentPadding,
        onClick = onClick,
    )
}

@Composable
fun ButtonNormal(
    text: String,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Primary,
    textStyle: TextStyle = TextAppearance.body2Bold(),
    enabled: Boolean = true,
    isLoading: Boolean = false,
    horizontalContentPadding: Dp = Spacing.box,
    verticalContentPadding: Dp = 0.dp,
    imageVector: ImageVector? = null,
    // ── Legacy override params ───────────────────────────────────────────
    // Only applied when style == Primary. Use `style` for all new callers.
    backgroundColor: Color? = null,                    // null = ambil dari theme
    contentColor: Color = Color.White,
    disabledBackgroundColor: Color = Colors.Gray4,
    strokeWidth: Dp = 0.dp,
    elevation: Dp = 0.dp,
    cornerRadius: Dp = Radius.normal,
    strokeColor: Color? = null,                        // null = ambil dari theme
    textColor: Color = Colors.White,
    imageTint: Color = Colors.White,
    contentPadding: PaddingValues? = null,
    onClick: () -> Unit,
) {
    val primaryColor = MaterialTheme.colorScheme.primary  // ambil dari theme

    val resolvedBg: Color
    val resolvedText: Color
    val resolvedStrokeWidth: Dp
    val resolvedStroke: Color
    val resolvedContent: Color

    when (style) {
        ButtonStyle.Primary -> {
            resolvedBg = backgroundColor ?: primaryColor       // fallback ke theme
            resolvedText = textColor
            resolvedStrokeWidth = strokeWidth
            resolvedStroke = strokeColor ?: primaryColor       // fallback ke theme
            resolvedContent = contentColor
        }

        ButtonStyle.Outlined -> {
            resolvedBg = Color.Transparent
            resolvedText = strokeColor ?: primaryColor
            resolvedStrokeWidth = 1.dp
            resolvedStroke = strokeColor ?: primaryColor
            resolvedContent = strokeColor ?: primaryColor
        }

        ButtonStyle.Destructive -> {
            resolvedBg = Colors.Delete
            resolvedText = Colors.White
            resolvedStrokeWidth = 0.dp
            resolvedStroke = Colors.Delete
            resolvedContent = Colors.White
        }

        ButtonStyle.OutlinedDestructive -> {
            resolvedBg = Color.Transparent
            resolvedText = Colors.Delete
            resolvedStrokeWidth = 1.dp
            resolvedStroke = Colors.Delete
            resolvedContent = Colors.Delete
        }

        ButtonStyle.Ghost -> {
            resolvedBg = Color.Transparent
            resolvedText = Colors.ColorPrimary
            resolvedStrokeWidth = 0.dp
            resolvedStroke = Color.Transparent
            resolvedContent = Colors.ColorPrimary
        }

        ButtonStyle.Secondary -> {
            resolvedBg = Colors.Secondary
            resolvedText = Colors.White
            resolvedStrokeWidth = 0.dp
            resolvedStroke = Colors.Secondary
            resolvedContent = Colors.White
        }

        ButtonStyle.Success -> {
            resolvedBg = Colors.Success
            resolvedText = Colors.White
            resolvedStrokeWidth = 0.dp
            resolvedStroke = Colors.Success
            resolvedContent = Colors.White
        }
    }

    val isOutlined = resolvedStrokeWidth > 0.dp

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isOutlined) Color.Transparent
            else if (enabled && !isLoading) resolvedBg else disabledBackgroundColor,
            contentColor = if (isOutlined) resolvedStroke else resolvedContent,
            disabledContainerColor = if (isOutlined) Color.Transparent else disabledBackgroundColor,
            disabledContentColor = Colors.Gray3,
        ),
        border = if (isOutlined) {
            BorderStroke(resolvedStrokeWidth, if (enabled) resolvedStroke else Colors.Gray4)
        } else null,
        enabled = enabled && !isLoading,
        contentPadding = contentPadding ?: PaddingValues(
            horizontal = horizontalContentPadding,
            vertical = verticalContentPadding
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = if (isOutlined) resolvedStroke else resolvedContent,
                strokeWidth = 2.dp,
            )
        } else {
            if (imageVector != null) {
                Icon(
                    imageVector,
                    contentDescription = null,
                    tint = if (style == ButtonStyle.Primary) imageTint else resolvedContent,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyle,
                textAlign = TextAlign.Center,
                color = if (enabled) resolvedText else Colors.Gray3,
            )
        }
    }
}