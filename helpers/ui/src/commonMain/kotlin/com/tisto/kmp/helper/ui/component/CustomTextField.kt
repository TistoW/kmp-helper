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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.icon.myicon.IcSearch
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.utils.ext.def
import com.tisto.kmp.helper.utils.getPlatform
import org.jetbrains.compose.resources.vectorResource
import kotlin.collections.isNotEmpty
import kotlin.let
import kotlin.ranges.coerceAtMost
import kotlin.text.count
import kotlin.text.filter
import kotlin.text.isDigit
import kotlin.text.isEmpty
import kotlin.text.isNotEmpty
import kotlin.text.take

@Composable
fun FormScope(
    maxFields: Int = 50,
    content: @Composable FormScopeImpl.() -> Unit
) {
    val focusRequesters = remember { List(maxFields) { FocusRequester() } }

    // ✅ PERBAIKAN: Buat scope baru yang selalu reset
    val scope = remember(focusRequesters) {
        FormScopeImpl(focusRequesters)
    }

    // ✅ PERBAIKAN: Reset SEBELUM content dipanggil
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
        formatCurrency: Boolean = false,
        maxDecimalDigits: Int = 3,
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
        autoHandlePassword: Boolean = true,
        onEnter: (() -> Unit)? = null,
        onItemSelected: ((Int) -> Unit)? = null,
    ) {
        val myIndex = currentIndex
        // ✅ Increment index
        if (editable) {
            currentIndex++
        }

        // ✅ TAMBAHAN: Reset index setelah composable ini selesai
        DisposableEffect(Unit) {
            onDispose {
                // Tidak perlu reset di sini
            }
        }

        CustomTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            hint = hint,
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
            inputType = inputType,
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
            onEnter = onEnter,
            onItemSelected = onItemSelected
        )
    }

    fun reset() {
        currentIndex = 0
    }
}

enum class TextTransform {
    NONE,
    UPPERCASE,
    LOWERCASE,
    CAPITALIZE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String? = "",
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
    inputType: KeyboardOptions = KeyboardOptions.Default, // KeyboardOptions(keyboardType = KeyboardType.Email)
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
    formatCurrency: Boolean = false,
    maxDecimalDigits: Int = 3,
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
    autoHandlePassword: Boolean = true,
    allCaps: Boolean = false,
    textTransform: TextTransform = TextTransform.NONE,
    onEnter: (() -> Unit)? = null,
    onItemSelected: ((Int) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var expanded by remember { mutableStateOf(false) }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val focusManager = LocalFocusManager.current
    val isWeb = remember { getPlatform().platform.contains("web") }
    val transform: TextTransform = if (allCaps) TextTransform.UPPERCASE else textTransform

    // ✅ Password visibility state
    var isPasswordVisible by remember { mutableStateOf(false) }

    // ✅ Detect if this is a password field
    val isPasswordField = remember(inputType) {
        inputType.keyboardType == KeyboardType.Password ||
                inputType.keyboardType == KeyboardType.NumberPassword
    }

    val currentBgColor = if (isFocused && focusedBackgroundColor != null) {
        focusedBackgroundColor
    } else {
        backgroundColor
    }

    val currentStrokeWidth = if (isFocused && focusedStrokeWidth != null) {
        focusedStrokeWidth
    } else {
        strokeWidth
    }

    var tfv by remember {
        mutableStateOf(
            TextFieldValue(
                text = value.def(),
                selection = TextRange(value.def().length)
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

    // ✅ PERBAIKAN: Password icon di luar remember block
    val passwordToggleIcon = if (isPasswordField && autoHandlePassword && endIcon == null) {
        if (isPasswordVisible) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    } else {
        null
    }

    // ✅ Determine final end icon
    val finalEndIcon = endIcon ?: passwordToggleIcon

    // ✅ Determine visual transformation
//    val finalVisualTransformation = when {
//        visualTransformation != null -> visualTransformation
//        isPasswordField && autoHandlePassword && !isPasswordVisible -> PasswordVisualTransformation()
//        else -> VisualTransformation.None
//    }

    val finalVisualTransformation = when {
        visualTransformation != null -> visualTransformation

        isPasswordField && autoHandlePassword && !isPasswordVisible ->
            PasswordVisualTransformation()

        transform == TextTransform.UPPERCASE ->
            VisualTransformation { text ->
                TransformedText(
                    AnnotatedString(text.text.uppercase()),
                    OffsetMapping.Identity
                )
            }

        transform == TextTransform.LOWERCASE ->
            VisualTransformation { text ->
                TransformedText(
                    AnnotatedString(text.text.lowercase()),
                    OffsetMapping.Identity
                )
            }

        else -> VisualTransformation.None
    }


    // ✅ Determine end icon click handler
    val finalEndIconOnClick: () -> Unit = {
        if (isPasswordField && autoHandlePassword && endIcon == null) {
            isPasswordVisible = !isPasswordVisible
        } else {
            endIconOnClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier
            )
            .onPreviewKeyEvent { keyEvent ->
                // ✅ TAMBAH: Skip jika not editable
                if (!editable) {
                    return@onPreviewKeyEvent false
                }

                // ✅ TAMBAH: Handle Enter key
                if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                    onEnter?.let {
                        it.invoke()
                        return@onPreviewKeyEvent true
                    }
                    // Jika onEnter null dan singleLine, pindah ke next field
                    if (singleLine) {
                        if (isWeb) {
                            nextFocusRequester?.requestFocus()
                                ?: focusManager.moveFocus(FocusDirection.Next)
                        } else {
                            nextFocusRequester?.requestFocus()
                                ?: focusManager.moveFocus(FocusDirection.Next)
                        }
                        return@onPreviewKeyEvent true
                    }
                    false
                }
                // Tab handling (existing)
                else if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.isShiftPressed) {
                        if (isWeb) {
                            previousFocusRequester?.requestFocus()
                                ?: focusManager.moveFocus(FocusDirection.Previous)
                        } else {
                            previousFocusRequester?.requestFocus()
                                ?: focusManager.moveFocus(FocusDirection.Previous)
                        }
                    } else {
                        if (isWeb) {
                            nextFocusRequester?.requestFocus()
                                ?: focusManager.moveFocus(FocusDirection.Next)
                        } else {
                            nextFocusRequester?.requestFocus()
                                ?: focusManager.moveFocus(FocusDirection.Next)
                        }
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
                        indication = null
                    ) {
                        onClick()
                        if (itemOptions.isNotEmpty()) expanded = !expanded
                    }
                } else {
                    Modifier
                }
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
                    } else {
                        filtered
                    }

                    val parts = clean.split(",")
                    val integerPart = parts[0].filter { it.isDigit() }
                    val decimalPart =
                        parts.getOrNull(1)?.filter { it.isDigit() }?.take(maxDecimalDigits) ?: ""

                    val rawValue = if (decimalPart.isNotEmpty()) {
                        "$integerPart.$decimalPart"
                    } else if (clean.endsWith(",")) {
                        "$integerPart."
                    } else {
                        integerPart
                    }

                    onValueChange(rawValue)

                    val formatted = formatCurrencyValue(rawValue)
                    tfv = TextFieldValue(formatted, TextRange(formatted.length))
                } else {
                    val composing = newV.composition
                    val over =
                        maxLength != null && newV.text.length > maxLength && composing == null
                    val clippedText = if (over) newV.text.take(maxLength) else newV.text
                    val selStart = minOf(newV.selection.start, clippedText.length)
                    val selEnd = minOf(newV.selection.end, clippedText.length)
                    val next = if (over) {
                        newV.copy(
                            text = clippedText,
                            selection = TextRange(selStart, selEnd),
                            composition = null
                        )
                    } else newV


//                    onValueChange(next.text)
//                    tfv = next

                    val transformedText = when (transform) {
                        TextTransform.UPPERCASE -> next.text.uppercase()
                        TextTransform.LOWERCASE -> next.text.lowercase()
                        TextTransform.CAPITALIZE ->
                            next.text.split(" ")
                                .joinToString(" ") {
                                    it.replaceFirstChar { c ->
                                        if (c.isLowerCase()) c.titlecase() else c.toString()
                                    }
                                }

                        else -> next.text
                    }

                    onValueChange(transformedText)

                    tfv = next.copy(
                        text = transformedText,
                        selection = TextRange(transformedText.length)
                    )
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
                    imeAction = if (onEnter != null) ImeAction.Done else ImeAction.Next
                )
            } else {
                if (singleLine) {
                    inputType.copy(
                        imeAction = if (onEnter != null) ImeAction.Done else ImeAction.Next
                    )
                } else {
                    inputType
                }
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    nextFocusRequester?.requestFocus()
                        ?: focusManager.moveFocus(FocusDirection.Next)
                },
                onDone = {
                    onEnter?.invoke()
                }
            ),
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (it.isFocused) onFocused() }
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = tfv.text,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = finalVisualTransformation,
                label = if (floatingLabel && hint.isNotEmpty()) {
                    {
                        Text(
                            text = hint,
                            style = hintStyle.copy(color = if (isFocused) Color.Black else Color.Gray)
                        )
                    }
                } else null,
                placeholder = {
                    Text(
                        text = if (!floatingLabel && hint.isNotEmpty()) hint else placeholder,
                        style = if (!floatingLabel && hint.isNotEmpty()) hintStyle else placeholderStyle
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
                trailingIcon = finalEndIcon?.let { // ✅ Use computed end icon
                    {
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = Spacing.box)
                                .clickable { finalEndIconOnClick() }, // ✅ Use computed click handler
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = it,
                                contentDescription = if (isPasswordField) {
                                    if (isPasswordVisible) "Hide password" else "Show password"
                                } else null,
                                modifier = Modifier.size(endIconSize)
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
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(leadingIconSize)
                            )
                        }
                    }
                },
                contentPadding = PaddingValues(
                    top = Spacing.box,
                    bottom = Spacing.box,
                    start = if (leadingIcon != null) 4.dp else Spacing.box,
                    end = if (finalEndIcon != null) 4.dp else Spacing.box
                ),
                interactionSource = interactionSource,
                container = {
                    when (style) {
                        TextFieldStyle.OUTLINED -> {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = isError,
                                modifier = Modifier,
                                shape = RoundedCornerShape(cornerRadius),
                                interactionSource = interactionSource,
                                focusedBorderThickness = if (isFocused && focusedStrokeWidth != null)
                                    focusedStrokeWidth else currentStrokeWidth,
                                unfocusedBorderThickness = currentStrokeWidth,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = strokeColorOnFocused,
                                    unfocusedBorderColor = strokeColor,
                                    focusedContainerColor = currentBgColor,
                                    unfocusedContainerColor = currentBgColor,
                                    disabledContainerColor = currentBgColor,
                                    cursorColor = strokeColorOnFocused
                                )
                            )
                        }

                        TextFieldStyle.FILLED -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(currentBgColor, RoundedCornerShape(cornerRadius))
                                    .then(
                                        if (currentStrokeWidth > 0.dp) {
                                            Modifier.border(
                                                width = currentStrokeWidth,
                                                color = if (isFocused) strokeColorOnFocused else strokeColor,
                                                shape = RoundedCornerShape(cornerRadius)
                                            )
                                        } else Modifier
                                    )
                            )
                        }

                        TextFieldStyle.CLEAR -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(currentBgColor, RoundedCornerShape(cornerRadius))
                                    .then(
                                        if (currentStrokeWidth > 0.dp) {
                                            Modifier.border(
                                                width = currentStrokeWidth,
                                                color = if (isFocused) strokeColorOnFocused else strokeColor,
                                                shape = RoundedCornerShape(cornerRadius)
                                            )
                                        } else Modifier
                                    )
                            )
                        }
                    }
                }
            )
        }

        if (!editable) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
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
                        .align(Alignment.CenterEnd)
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
                }
            )
        }
    }
}

// ✅ Helper function untuk format currency
private fun formatCurrencyValue(value: String): String {
    if (value.isEmpty()) return ""

    val parts = value.split(".")
    val integerPart = parts[0]
    val decimalPart = parts.getOrNull(1)

    if (integerPart.isEmpty() && decimalPart == null) return ""

    val formattedInteger = if (integerPart.isEmpty() || integerPart == "0") {
        "0"
    } else {
        integerPart.reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    }

    return if (decimalPart != null) {
        "$formattedInteger,$decimalPart"
    } else if (value.endsWith(".")) {
        "$formattedInteger,"
    } else {
        formattedInteger
    }
}

enum class TextFieldStyle {
    OUTLINED,
    FILLED,
    CLEAR
}

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    hint: String = "Cari",
    style: TextFieldStyle = TextFieldStyle.OUTLINED,
    leadingIcon: ImageVector = MyIcon.IcSearch,
    strokeWidth: Dp = 0.5.dp,
    strokeColor: Color = Colors.Gray2,
    onEnter: () -> Unit = {}
) {
    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        hint = hint,
        style = style,
        leadingIcon = leadingIcon,
        strokeWidth = strokeWidth,
        strokeColor = strokeColor,
        floatingLabel = false,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        onEnter = onEnter
    )
}

@Composable
fun EditTextCustomExamples() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "EditTextCustom Examples",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 1. Outlined dengan Border Normal (1dp)
        item {
            Text("1. Outlined Standard", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = name,
                onValueChange = { name = it },
                hint = "Nama",
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 1.dp,
                strokeColor = Color(0xFFE0E0E0),
                strokeColorOnFocused = Color(0xFF0ABAB5),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 2. Tanpa Border (strokeWidth = 0.dp)
        item {
            Text("2. Filled tanpa Border", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                hint = "Email",
                style = TextFieldStyle.FILLED,
                backgroundColor = Color(0xFFF5F7FA),
                strokeWidth = 0.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 3. Border Tipis ke Tebal saat Focus
        item {
            Text("3. Border Dynamic (Tipis → Tebal)", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 1.dp,
                focusedStrokeWidth = 2.dp,
                strokeColor = Color(0xFFE0E0E0),
                strokeColorOnFocused = Color(0xFF0ABAB5),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 4. Border Tebal
        item {
            Text("4. Border Tebal", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = phone,
                onValueChange = { phone = it },
                hint = "Phone",
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 2.dp,
                strokeColor = Color(0xFF0ABAB5),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 5. Bottom Border Only (Material Style)
        item {
            Text("5. Bottom Border Only", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = username,
                onValueChange = { username = it },
                hint = "Username",
                style = TextFieldStyle.FILLED,
                backgroundColor = Color.Transparent,
                strokeWidth = 0.dp,
                focusedStrokeWidth = 2.dp,
                cornerRadius = 0.dp,
                strokeColorOnFocused = Color(0xFF0ABAB5),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 6. Search Bar tanpa Border
        item {
            Text("6. Search Bar", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search...",
                style = TextFieldStyle.FILLED,
                backgroundColor = Color(0xFFF5F7FA),
                strokeWidth = 0.dp,
                cornerRadius = 24.dp,
                floatingLabel = false,
                onClick = {

                },
                editable = false,
                leadingIcon = Icons.Default.Search,
                endIcon = Icons.Default.Close,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 7. Clear/Transparent tanpa Border
        item {
            Text("7. Clear/Transparent", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = note,
                onValueChange = { note = it },
                hint = "Catatan",
                style = TextFieldStyle.CLEAR,
                backgroundColor = Color.Transparent,
                strokeWidth = 0.dp,
                floatingLabel = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 8. Custom: Background + Border Tipis
        item {
            Text("8. Background Cyan + Border", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = price,
                onValueChange = { price = it },
                hint = "Harga",
                prefix = "Rp",
                style = TextFieldStyle.FILLED,
                backgroundColor = Color(0xFFE6F9F8),
                focusedBackgroundColor = Color(0xFFCCF3F1),
                strokeWidth = 0.5.dp,
                strokeColor = Color(0xFF0ABAB5),
                strokeColorOnFocused = Color(0xFF0ABAB5),
                cornerRadius = 8.dp,
                formatCurrency = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 9. Border Tebal untuk Emphasis
        item {
            Text("9. Border Tebal (Emphasis)", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = code,
                onValueChange = { code = it },
                hint = "Kode Verifikasi",
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 3.dp,
                strokeColor = Color(0xFF9E9E9E),
                strokeColorOnFocused = Color(0xFF0ABAB5),
                cornerRadius = 8.dp,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            var price by remember { mutableStateOf("") }
            Text("10. Example Standalone - Price Input:", fontWeight = FontWeight.Medium)
            CustomTextField(
                value = price,
                onValueChange = { price = it },
                hint = "Harga",
                prefix = "Rp",
                style = TextFieldStyle.OUTLINED,
                cornerRadius = 8.dp,
                formatCurrency = true,
                inputType = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = name,
                onValueChange = { name = it },
                hint = "Nama",
                style = TextFieldStyle.OUTLINED,
                formatCurrency = true,
                strokeWidth = 0.5.dp,
                strokeColor = Colors.Gray4,
                strokeColorOnFocused = Color(0xFF0ABAB5),
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            )
        }
    }
}

@TabletPreview
@Composable
fun TabletPreviews() {
    HelperTheme {
        EditTextCustomExamples()
    }

}

@MobilePreview
@Composable
fun MobilePreviews() {
    HelperTheme {
        EditTextCustomExamples()
    }
}