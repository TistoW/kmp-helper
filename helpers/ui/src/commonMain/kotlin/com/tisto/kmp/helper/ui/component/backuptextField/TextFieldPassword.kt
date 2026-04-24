package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance

// ══════════════════════════════════════════════════════════════════════════════
// PasswordTextField — standalone password input with visibility toggle.
//
// Usage:
//   PasswordTextField(
//       value = state.password,
//       onValueChange = { onEvent(Event.PasswordChanged(it)) },
//       hint = "Password",
//   )
//
// Does NOT replace the old CustomTextField password handling — that stays for
// backward compat. New features should use this instead.
// ══════════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    hintStyle: TextStyle = TextAppearance.body2(),
    textStyle: TextStyle = TextAppearance.body1(),
    supportingText: String? = null,
    supportingTextStyle: TextStyle = TextAppearance.body2(),
    isError: Boolean = false,
    enabled: Boolean = true,
    style: TextFieldStyle = TextFieldStyle.OUTLINED,
    strokeWidth: Dp = 0.5.dp,
    focusedStrokeWidth: Dp? = null,
    strokeColor: Color = Colors.Gray2,
    strokeColorOnFocused: Color = Color.Black,
    backgroundColor: Color = Color.Transparent,
    cornerRadius: Dp = Radius.box,
    imeAction: ImeAction = ImeAction.Next,
    onDone: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by remember { mutableStateOf(false) }

    val currentStrokeWidth = if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else strokeWidth

    val visualTransformation = if (isPasswordVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    val toggleIcon = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        textStyle = textStyle,
        singleLine = true,
        cursorBrush = SolidColor(strokeColorOnFocused),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = if (onDone != null) ImeAction.Done else imeAction,
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
            onDone = { onDone?.invoke() },
        ),
        interactionSource = interactionSource,
        modifier = modifier.fillMaxWidth(),
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = enabled,
            singleLine = true,
            visualTransformation = visualTransformation,
            label = if (label.isNotEmpty()) {
                {
                    Text(
                        text = label,
                        style = hintStyle.copy(color = if (isFocused) Color.Black else Color.Gray),
                    )
                }
            } else null,
            supportingText = supportingText?.let { msg ->
                { Text(text = msg, style = supportingTextStyle) }
            },
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(end = Spacing.box)
                        .clickable { isPasswordVisible = !isPasswordVisible },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = toggleIcon,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(20.dp),
                    )
                }
            },
            contentPadding = PaddingValues(
                top = Spacing.box,
                bottom = Spacing.box,
                start = Spacing.box,
                end = 4.dp,
            ),
            interactionSource = interactionSource,
            container = {
                OutlinedTextFieldDefaults.Container(
                    enabled = enabled,
                    isError = isError,
                    shape = RoundedCornerShape(cornerRadius),
                    interactionSource = interactionSource,
                    focusedBorderThickness = if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else currentStrokeWidth,
                    unfocusedBorderThickness = currentStrokeWidth,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = strokeColorOnFocused,
                        unfocusedBorderColor = strokeColor,
                        focusedContainerColor = backgroundColor,
                        unfocusedContainerColor = backgroundColor,
                        disabledContainerColor = backgroundColor,
                        cursorColor = strokeColorOnFocused,
                    ),
                )
            },
        )
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Composable
private fun PasswordTextFieldExample() {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.normal),
        verticalArrangement = Arrangement.spacedBy(Spacing.normal),
    ) {
        Text("1. Default", style = TextAppearance.body2())
        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            strokeWidth = 1.dp,
        )

        Text("2. With error", style = TextAppearance.body2())
        PasswordTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Konfirmasi Password",
            strokeWidth = 1.dp,
            isError = true,
            supportingText = "Password tidak sama",
        )
    }
}

@MobilePreview
@Composable
private fun PasswordTextFieldMobilePreview() {
    HelperTheme { PasswordTextFieldExample() }
}

@TabletPreview
@Composable
private fun PasswordTextFieldTabletPreview() {
    HelperTheme { PasswordTextFieldExample() }
}
