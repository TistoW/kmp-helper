package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.utils.PlatformType
import com.tisto.kmp.helper.utils.ext.def

// ══════════════════════════════════════════════════════════════════════════════
// CustomTextField — general-purpose text input.
//
// For specialized inputs use the dedicated components:
//   - Password  → PasswordTextField  (TextFieldPassword.kt)
//   - Currency  → CurrencyTextField   (TextFieldCurrency.kt)
//   - Search    → SearchTextField     (TextFieldSearch.kt)
//
// Legacy: the old version with formatCurrency/autoHandlePassword is preserved
// in TextFieldCustom.old.kt for reference.
// ══════════════════════════════════════════════════════════════════════════════

// ── FormScope (focus-chain helper) ───────────────────────────────────────────

@Composable
fun FormScope(
    maxFields: Int = 50,
    content: @Composable FormScopeImpl.() -> Unit,
) {
    val focusRequesters = remember { List(maxFields) { FocusRequester() } }
    val scope = remember(focusRequesters) { FormScopeImpl(focusRequesters) }
    scope.reset()
    scope.content()
}

class FormScopeImpl(private val focusRequesters: List<FocusRequester>) {
    private var currentIndex = 0

    @Composable
    fun CustomTextField(
        value: String = "",
        onValueChange: (String) -> Unit = {},
        modifier: Modifier = Modifier,
        textStyle: TextStyle = TextAppearance.body1(),
        hint: String = "",
        hintStyle: TextStyle = TextAppearance.body2(),
        placeholder: String = "",
        placeholderStyle: TextStyle = TextAppearance.body1(),
        prefix: String = "",
        prefixStyle: TextStyle = TextAppearance.body1(),
        suffix: String = "",
        suffixStyle: TextStyle = TextAppearance.body1(),
        supportingText: String? = null,
        supportingTextStyle: TextStyle = TextAppearance.body2(),
        editable: Boolean = true,
        endIcon: ImageVector? = null,
        endIconSize: Dp = 20.dp,
        endIconOnClick: () -> Unit = {},
        leadingIcon: ImageVector? = null,
        leadingIconSize: Dp = 20.dp,
        leadingIconOnClick: () -> Unit = {},
        inputType: KeyboardOptions = KeyboardOptions.Default,
        onClick: () -> Unit = {},
        enabled: Boolean = true,
        isError: Boolean = false,
        singleLine: Boolean = false,
        onFocused: () -> Unit = {},
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        minLines: Int = 1,
        maxLength: Int? = null,
        cornerRadius: Dp = Radius.box,
        formatCurrency: Boolean = false,          // legacy — use CurrencyTextField instead
        maxDecimalDigits: Int = 3,                // legacy — use CurrencyTextField instead
        visualTransformation: VisualTransformation? = null,
        itemOptions: List<String> = listOf(),
        floatingLabel: Boolean = true,
        style: TextFieldStyle = TextFieldStyle.OUTLINED,
        backgroundColor: Color = Color.Transparent,
        focusedBackgroundColor: Color? = null,
        strokeWidth: Dp = 1.dp,
        focusedStrokeWidth: Dp? = null,
        strokeColor: Color = Colors.Gray2,
        strokeColorOnFocused: Color = Colors.Gray2,
        autoHandlePassword: Boolean = true,       // legacy — use PasswordTextField instead
        textTransform: TextTransform = TextTransform.NONE,
        onEnter: (() -> Unit)? = null,
        onItemSelected: ((Int) -> Unit)? = null,
    ) {
        val myIndex = currentIndex
        if (editable) currentIndex++

        DisposableEffect(Unit) { onDispose { } }

        CustomTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            label = hint,
            hintStyle = hintStyle,
            placeholder = placeholder,
            placeholderStyle = placeholderStyle,
            prefix = prefix,
            prefixStyle = prefixStyle,
            suffix = suffix,
            suffixStyle = suffixStyle,
            supportingText = supportingText,
            supportingTextStyle = supportingTextStyle,
            editable = editable,
            endIcon = endIcon,
            endIconSize = endIconSize,
            endIconOnClick = endIconOnClick,
            leadingIcon = leadingIcon,
            leadingIconSize = leadingIconSize,
            leadingIconOnClick = leadingIconOnClick,
            keyboardOptions = inputType,
            onClick = onClick,
            enabled = enabled,
            isError = isError,
            singleLine = singleLine,
            onFocused = onFocused,
            maxLines = maxLines,
            minLines = minLines,
            maxLength = maxLength,
            cornerRadius = cornerRadius,
            formatCurrency = formatCurrency,
            maxDecimalDigits = maxDecimalDigits,
            visualTransformation = visualTransformation,
            itemOptions = itemOptions,
            floatingLabel = floatingLabel,
            style = style,
            backgroundColor = backgroundColor,
            focusedBackgroundColor = focusedBackgroundColor,
            strokeWidth = strokeWidth,
            focusedStrokeWidth = focusedStrokeWidth,
            strokeColor = strokeColor,
            strokeColorOnFocused = strokeColorOnFocused,
            focusRequester = if (editable) focusRequesters.getOrNull(myIndex) else null,
            nextFocusRequester = if (editable) focusRequesters.getOrNull(myIndex + 1) else null,
            previousFocusRequester = if (editable) focusRequesters.getOrNull(myIndex - 1) else null,
            autoHandlePassword = autoHandlePassword,
            textTransform = textTransform,
            onEnter = onEnter,
            onItemSelected = onItemSelected,
        )
    }

    fun reset() {
        currentIndex = 0
    }
}

// ── Enums ────────────────────────────────────────────────────────────────────

enum class TextTransform { NONE, UPPERCASE, LOWERCASE, CAPITALIZE }

enum class TextFieldStyle { OUTLINED, FILLED, CLEAR }

// ── CustomTextField ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String? = "",
    textStyle: TextStyle = TextAppearance.body1(),
    label: String = "",
    hintStyle: TextStyle = TextAppearance.body2(),
    placeholder: String = "",
    placeholderStyle: TextStyle = TextAppearance.body1(),
    prefix: String = "",
    prefixStyle: TextStyle = TextAppearance.body1(),
    suffix: String = "",
    suffixStyle: TextStyle = TextAppearance.body1(),
    supportingText: String? = null,
    supportingTextStyle: TextStyle = TextAppearance.body2(),
    editable: Boolean = true,
    endIcon: ImageVector? = null,
    endIconSize: Dp = 20.dp,
    endIconOnClick: () -> Unit = {},
    leadingIcon: ImageVector? = null,
    leadingIconSize: Dp = 20.dp,
    leadingIconOnClick: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = false,
    onFocused: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    maxLength: Int? = null,
    cornerRadius: Dp = Radius.box,
    formatCurrency: Boolean = false,          // legacy — use CurrencyTextField instead
    maxDecimalDigits: Int = 3,                // legacy — use CurrencyTextField instead
    visualTransformation: VisualTransformation? = null,
    itemOptions: List<String> = listOf(),
    floatingLabel: Boolean = true,
    style: TextFieldStyle = TextFieldStyle.OUTLINED,
    backgroundColor: Color = Color.Transparent,
    focusedBackgroundColor: Color? = null,
    strokeWidth: Dp = 0.5.dp,
    focusedStrokeWidth: Dp? = null,
    strokeColor: Color = Colors.Gray2,
    strokeColorOnFocused: Color = Color.Black,
    focusRequester: FocusRequester? = null,
    nextFocusRequester: FocusRequester? = null,
    previousFocusRequester: FocusRequester? = null,
    autoHandlePassword: Boolean = true,       // legacy — use PasswordTextField instead
    allCaps: Boolean = false,
    textTransform: TextTransform = TextTransform.NONE,
    onEnter: (() -> Unit)? = null,
    onItemSelected: ((Int) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var expanded by remember { mutableStateOf(false) }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val focusManager = LocalFocusManager.current
    val isWeb = remember { PlatformType.isWeb }
    val transform: TextTransform = if (allCaps) TextTransform.UPPERCASE else textTransform

    val currentBgColor = if (isFocused && focusedBackgroundColor != null) focusedBackgroundColor else backgroundColor
    val currentStrokeWidth = if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else strokeWidth

    // Legacy password handling
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isPasswordField = remember(keyboardOptions) {
        keyboardOptions.keyboardType == KeyboardType.Password ||
                keyboardOptions.keyboardType == KeyboardType.NumberPassword
    }

    val passwordToggleIcon = if (isPasswordField && autoHandlePassword && endIcon == null) {
        if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    } else null

    val finalEndIcon = endIcon ?: passwordToggleIcon

    val finalEndIconOnClick: () -> Unit = {
        if (isPasswordField && autoHandlePassword && endIcon == null) {
            isPasswordVisible = !isPasswordVisible
        } else {
            endIconOnClick()
        }
    }

    val finalVisualTransformation = when {
        visualTransformation != null -> visualTransformation
        isPasswordField && autoHandlePassword && !isPasswordVisible -> PasswordVisualTransformation()
        transform == TextTransform.UPPERCASE ->
            VisualTransformation { text ->
                TransformedText(AnnotatedString(text.text.uppercase()), OffsetMapping.Identity)
            }
        transform == TextTransform.LOWERCASE ->
            VisualTransformation { text ->
                TransformedText(AnnotatedString(text.text.lowercase()), OffsetMapping.Identity)
            }
        else -> VisualTransformation.None
    }

    var tfv by remember {
        mutableStateOf(
            TextFieldValue(
                text = value.def(),
                selection = TextRange(value.def().length),
            )
        )
    }

    LaunchedEffect(value, formatCurrency) {
        if (formatCurrency) {
            val formatted = formatCurrencyValue(value.def())
            if (tfv.text != formatted) {
                tfv = TextFieldValue(formatted, TextRange(formatted.length))
            }
        } else {
            if (tfv.text != value) {
                val cur = tfv.selection.start.coerceAtMost(value.def().length)
                tfv = tfv.copy(text = value.def(), selection = TextRange(cur))
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
            .onPreviewKeyEvent { keyEvent ->
                if (!editable) return@onPreviewKeyEvent false

                if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                    onEnter?.let {
                        it.invoke()
                        return@onPreviewKeyEvent true
                    }
                    if (singleLine) {
                        nextFocusRequester?.requestFocus()
                            ?: focusManager.moveFocus(FocusDirection.Next)
                        return@onPreviewKeyEvent true
                    }
                    false
                } else if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.isShiftPressed) {
                        previousFocusRequester?.requestFocus()
                            ?: focusManager.moveFocus(FocusDirection.Previous)
                    } else {
                        nextFocusRequester?.requestFocus()
                            ?: focusManager.moveFocus(FocusDirection.Next)
                    }
                    true
                } else {
                    false
                }
            }
            .then(
                if (!editable) {
                    Modifier.clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = null,
                    ) {
                        onClick()
                        if (itemOptions.isNotEmpty()) expanded = !expanded
                    }
                } else Modifier
            )
    ) {
        BasicTextField(
            value = tfv,
            onValueChange = { newV ->
                if (formatCurrency) {
                    val input = newV.text.removePrefix(prefix)
                    val filtered = input.filter { it.isDigit() || it == ',' }
                    val commaCount = filtered.count { it == ',' }
                    val clean = if (commaCount > 1) {
                        filtered.replaceFirst(",", "").replace(",", "")
                    } else filtered

                    val parts = clean.split(",")
                    val integerPart = parts[0].filter { it.isDigit() }
                    val decimalPart = parts.getOrNull(1)?.filter { it.isDigit() }?.take(maxDecimalDigits) ?: ""

                    val rawValue = if (decimalPart.isNotEmpty()) "$integerPart.$decimalPart"
                    else if (clean.endsWith(",")) "$integerPart."
                    else integerPart

                    onValueChange(rawValue)
                    val formatted = formatCurrencyValue(rawValue)
                    tfv = TextFieldValue(formatted, TextRange(formatted.length))
                } else {
                    val composing = newV.composition
                    val over = maxLength != null && newV.text.length > maxLength && composing == null
                    val clippedText = if (over) newV.text.take(maxLength) else newV.text
                    val selStart = minOf(newV.selection.start, clippedText.length)
                    val selEnd = minOf(newV.selection.end, clippedText.length)
                    val next = if (over) {
                        newV.copy(
                            text = clippedText,
                            selection = TextRange(selStart, selEnd),
                            composition = null,
                        )
                    } else newV

                    val needsTransform = transform != TextTransform.NONE
                    val transformedText = when (transform) {
                        TextTransform.UPPERCASE -> next.text.uppercase()
                        TextTransform.LOWERCASE -> next.text.lowercase()
                        TextTransform.CAPITALIZE -> next.text.split(" ")
                            .joinToString(" ") {
                                it.replaceFirstChar { c ->
                                    if (c.isLowerCase()) c.titlecase() else c.toString()
                                }
                            }
                        else -> next.text
                    }

                    onValueChange(transformedText)

                    tfv = if (needsTransform) {
                        next.copy(text = transformedText, selection = TextRange(transformedText.length))
                    } else {
                        next.copy(text = transformedText)
                    }
                }
            },
            enabled = enabled,
            readOnly = !editable,
            textStyle = textStyle,
            cursorBrush = SolidColor(strokeColorOnFocused),
            visualTransformation = finalVisualTransformation,
            keyboardOptions = if (formatCurrency) {
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (onEnter != null) ImeAction.Done else ImeAction.Next,
                )
            } else if (singleLine) {
                keyboardOptions.copy(imeAction = if (onEnter != null) ImeAction.Done else ImeAction.Next)
            } else {
                keyboardOptions
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    nextFocusRequester?.requestFocus()
                        ?: focusManager.moveFocus(FocusDirection.Next)
                },
                onDone = { onEnter?.invoke() },
            ),
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (it.isFocused) onFocused() },
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = tfv.text,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = finalVisualTransformation,
                label = if (floatingLabel && label.isNotEmpty()) {
                    {
                        Text(
                            text = label,
                            style = hintStyle.copy(color = if (isFocused) Color.Black else Color.Gray),
                        )
                    }
                } else null,
                placeholder = {
                    Text(
                        text = if (!floatingLabel && label.isNotEmpty()) label else placeholder,
                        style = if (!floatingLabel && label.isNotEmpty()) hintStyle else placeholderStyle,
                    )
                },
                prefix = {
                    if (prefix.isNotEmpty() && (tfv.text.isNotEmpty() || isFocused)) {
                        Text(prefix, style = prefixStyle)
                    }
                },
                suffix = {
                    if (suffix.isNotEmpty() && (tfv.text.isNotEmpty() || isFocused)) {
                        Text(suffix, style = suffixStyle)
                    }
                },
                supportingText = supportingText?.let { msg ->
                    { Text(text = msg, style = supportingTextStyle) }
                },
                trailingIcon = finalEndIcon?.let {
                    {
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = Spacing.box)
                                .clickable { finalEndIconOnClick() },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = it,
                                contentDescription = if (isPasswordField) {
                                    if (isPasswordVisible) "Hide password" else "Show password"
                                } else null,
                                modifier = Modifier.size(endIconSize),
                            )
                        }
                    }
                },
                leadingIcon = leadingIcon?.let {
                    {
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(start = Spacing.box)
                                .clickable { leadingIconOnClick() },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(leadingIconSize),
                            )
                        }
                    }
                },
                contentPadding = PaddingValues(
                    top = Spacing.box,
                    bottom = Spacing.box,
                    start = if (leadingIcon != null) 4.dp else Spacing.box,
                    end = if (finalEndIcon != null) 4.dp else Spacing.box,
                ),
                interactionSource = interactionSource,
                container = {
                    when (style) {
                        TextFieldStyle.OUTLINED -> {
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
                                    focusedContainerColor = currentBgColor,
                                    unfocusedContainerColor = currentBgColor,
                                    disabledContainerColor = currentBgColor,
                                    cursorColor = strokeColorOnFocused,
                                ),
                            )
                        }
                        TextFieldStyle.FILLED, TextFieldStyle.CLEAR -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(currentBgColor, RoundedCornerShape(cornerRadius))
                                    .then(
                                        if (currentStrokeWidth > 0.dp) {
                                            Modifier.border(
                                                width = currentStrokeWidth,
                                                color = if (isFocused) strokeColorOnFocused else strokeColor,
                                                shape = RoundedCornerShape(cornerRadius),
                                            )
                                        } else Modifier
                                    )
                            )
                        }
                    }
                },
            )
        }

        if (!editable) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onClick()
                        if (itemOptions.isNotEmpty()) expanded = !expanded
                    }
            ) {
                IconButton(
                    onClick = { endIconOnClick() },
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 4.dp)
                        .width(50.dp)
                        .padding(7.dp)
                        .align(Alignment.CenterEnd),
                ) {}
            }
        }

        if (itemOptions.isNotEmpty()) {
            PopupMenu(
                expanded = expanded,
                onDismiss = { expanded = false },
                items = itemOptions,
                onSelected = {
                    onValueChange(it)
                    onItemSelected?.invoke(itemOptions.indexOf(it))
                },
            )
        }
    }
}

// ── Legacy currency formatter ────────────────────────────────────────────────

private fun formatCurrencyValue(value: String): String {
    if (value.isEmpty()) return ""
    val parts = value.split(".")
    val integerPart = parts[0]
    val decimalPart = parts.getOrNull(1)
    if (integerPart.isEmpty() && decimalPart == null) return ""
    val formattedInteger = if (integerPart.isEmpty() || integerPart == "0") "0"
    else integerPart.reversed().chunked(3).joinToString(".").reversed()
    return if (decimalPart != null) "$formattedInteger,$decimalPart"
    else if (value.endsWith(".")) "$formattedInteger,"
    else formattedInteger
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Composable
private fun CustomTextFieldExamples() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("CustomTextField Examples", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        item {
            Text("1. Outlined Standard", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nama",
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Text("2. Filled tanpa Border", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                style = TextFieldStyle.FILLED,
                backgroundColor = Color(0xFFF5F7FA),
                strokeWidth = 0.dp,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Text("3. Clear/Transparent", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = note,
                onValueChange = { note = it },
                label = "Catatan",
                style = TextFieldStyle.CLEAR,
                strokeWidth = 0.dp,
                floatingLabel = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@TabletPreview
@Composable
private fun TabletPreviews() {
    HelperTheme { CustomTextFieldExamples() }
}

@MobilePreview
@Composable
private fun MobilePreviews() {
    HelperTheme { CustomTextFieldExamples() }
}
