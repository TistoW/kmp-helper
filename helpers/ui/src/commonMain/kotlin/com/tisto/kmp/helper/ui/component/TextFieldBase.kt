package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance

// =============================================================================
// BaseOutlinedTextField — Material3 OutlinedTextField mirror with custom
// contentPadding and strokeWidth support.
//
// Why a copy instead of using OutlinedTextField directly?
//   1. Material3's OutlinedTextField does NOT expose contentPadding — the
//      DecorationBox uses a hard-coded value, making consistent field height
//      impossible across variants.
//   2. strokeWidth / focusedStrokeWidth are not configurable either.
//
// This base mirrors the Material3 source (textColor resolution, cursor color
// from TextFieldColors, LocalTextSelectionColors, semantics merge, defaultMinSize)
// so every variant built on it gets the same correct behavior.
//
// Specialized wrappers:
//   - CustomTextField      (TextFieldCustom.kt)
//   - CurrencyTextField    (TextFieldCurrency.kt)
//   - PasswordTextField    (TextFieldPassword.kt)
//   - SelectableTextField  (TextFieldSelectable.kt)
//   - SearchTextField      (TextFieldSearch.kt) — own layout, not based on this
// =============================================================================

// -- Enums (shared across all variants) ---------------------------------------

enum class TextFieldStyle { OUTLINED, FILLED, CLEAR }

enum class TextTransform { NONE, UPPERCASE, LOWERCASE, CAPITALIZE }

// -- String-based overload ----------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextAppearance.body1(),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    contentPadding: PaddingValues = PaddingValues(Spacing.box),
    strokeWidth: Dp = 0.5.dp,
    focusedStrokeWidth: Dp? = null,
    onFocused: (() -> Unit)? = null,
    container: (@Composable () -> Unit)? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val currentStrokeWidth =
        if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else strokeWidth

    // Resolve text color from TextFieldColors when not set in textStyle
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled, isError, isFocused)
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier = modifier
                .then(
                    if (label != null) {
                        Modifier.semantics(mergeDescendants = true) {}
                    } else Modifier
                )
                .defaultMinSize(minWidth = OutlinedTextFieldDefaults.MinWidth)
                .then(
                    if (onFocused != null) Modifier.onFocusChanged { if (it.isFocused) onFocused() }
                    else Modifier
                ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError)),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = contentPadding,
                    container = container ?: {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = shape,
                            focusedBorderThickness = focusedStrokeWidth ?: strokeWidth,
                            unfocusedBorderThickness = strokeWidth,
                        )
                    },
                )
            },
        )
    }
}

// -- TextFieldValue-based overload --------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseOutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextAppearance.body1(),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    contentPadding: PaddingValues = PaddingValues(Spacing.box),
    strokeWidth: Dp = 0.5.dp,
    focusedStrokeWidth: Dp? = null,
    onFocused: (() -> Unit)? = null,
    container: (@Composable () -> Unit)? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled, isError, isFocused)
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier = modifier
                .then(
                    if (label != null) {
                        Modifier.semantics(mergeDescendants = true) {}
                    } else Modifier
                )
                .defaultMinSize(minWidth = OutlinedTextFieldDefaults.MinWidth)
                .then(
                    if (onFocused != null) Modifier.onFocusChanged { if (it.isFocused) onFocused() }
                    else Modifier
                ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError)),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value.text,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = contentPadding,
                    container = container ?: {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = shape,
                            focusedBorderThickness = focusedStrokeWidth ?: strokeWidth,
                            unfocusedBorderThickness = strokeWidth,
                        )
                    },
                )
            },
        )
    }
}
