package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
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
import com.tisto.kmp.helper.utils.ext.logs
import com.tisto.kmp.helper.utils.ext.remove
import kotlin.math.roundToLong

// ─── Raw string format ────────────────────────────────────────────────────────
//
// State stores:  digits + optional decimal separator (comma OR period) + up to 3 decimal digits
//   e.g. "10000,9"  "10000.9"  "75000"  "1500000,25"
//
// Display format (CurrencyVisualTransformation):
//   dot as thousands separator, comma as decimal separator, max 3 decimal digits
//   e.g. "10.000,9"  "75.000"  "1.500.000,25"
//
// The raw text preserves whatever the keyboard sends ('.' or ',') so the IME
// stays in sync. The VisualTransformation always displays comma for decimal.

/**
 * Filters user input to only allow a valid price string:
 * digits, at most one decimal separator (',' or '.'), and at most [maxDecimalDigits]
 * digits after it.  The separator character the user typed is preserved as-is so the
 * IME stays in sync with the raw text (avoids the IME reverting the edit).
 */
fun filterPriceInput(
    it: String,
    maxDecimalDigits: Int = 3,
    maxIntegerDigits: Int = Int.MAX_VALUE,
): String {
    val v = if (',' in it) {
        if ('.' in it) it.remove(",")
        else it.replace(",", ".")
    } else it
    var hasDec = false
    var intCount = 0
    var decCount = 0
    return buildString {
        for (c in v) {
            when {
                c.isDigit() -> {
                    if (!hasDec) {
                        if (intCount < maxIntegerDigits) {
                            append(c); intCount++
                        }
                    } else if (decCount < maxDecimalDigits) {
                        append(c); decCount++
                    }
                }

                (c == ',' || c == '.') && !hasDec && isNotEmpty() -> {
                    hasDec = true
                    append(c) // keep original char so IME doesn't detect a mismatch
                }
            }
        }
    }
}

/** Parses a raw price string to Double. "10000,9" → 10000.9, "10000.9" → 10000.9 */
fun String.toRawDouble(): Double = replace(',', '.').toDoubleOrNull() ?: 0.0

/**
 * Converts a Double back to a raw price string for state storage.
 * 10000.9 → "10000,9"  |  75000.0 → "75000"
 * Decimal part is trimmed of trailing zeros, max 3 digits.
 */
fun Double.toRawPriceString(): String {
    if (this <= 0.0) return "0"
    val rounded = (this * 1000.0).roundToLong().toDouble() / 1000.0
    val longPart = rounded.toLong()
    val decPart = rounded - longPart.toDouble()
    if (decPart < 0.0001) return longPart.toString()
    val decStr = "%.3f".format(decPart).drop(2).trimEnd('0')
    return "${longPart},${decStr}"
}

// ─── VisualTransformation ─────────────────────────────────────────────────────
//
// State stores raw string ("10000,9" or "10000.9"); display shows "10.000,9".
// Raw may contain ',' or '.' as decimal — both are displayed as ','.
// Dots in the formatted output are ONLY thousands separators.
// OffsetMapping treats those inserted dots as zero-width to the cursor.

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = format(raw)

        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var rawSeen = 0
                formatted.forEachIndexed { i, c ->
                    if (rawSeen == offset) return i
                    if (c != '.') rawSeen++
                }
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int =
                formatted.take(offset.coerceAtMost(formatted.length)).count { it != '.' }
        }

        return TransformedText(AnnotatedString(formatted), mapping)
    }

    private fun format(raw: String): String {
        if (raw.isEmpty()) return ""
        val decIdx = raw.indexOfFirst { it == ',' || it == '.' }
        return if (decIdx >= 0) {
            val intPart = raw.substring(0, decIdx).ifEmpty { "0" }
            val decPart = raw.substring(decIdx + 1)
            "${formatIntPart(intPart)},${decPart}"
        } else {
            formatIntPart(raw)
        }
    }

    private fun formatIntPart(digits: String): String {
        val long = digits.toLongOrNull() ?: return digits
        if (long == 0L) return "0"
        return long.toString().reversed().chunked(3).joinToString(".").reversed()
    }
}

// ─── Composable ───────────────────────────────────────────────────────────────

/**
 * A currency-aware [OutlinedTextField] that:
 * - Stores raw input as "10000,9" (comma/period as decimal separator)
 * - Displays formatted output as "10.000,9" (dot as thousands separator)
 * - Prefixes with "Rp "
 * - Only allows valid numeric/decimal input via [filterPriceInput]
 *
 * [value] and [onValueChange] operate on the **raw** string.
 * Use [toRawDouble] / [toRawPriceString] to convert between raw and Double.
 *
 * Uses Material3 [OutlinedTextField] directly (not BasicTextField + DecorationBox)
 * because the latter breaks IME composition for comma/period input on Android.
 */
@Composable
fun CurrencyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    supportingText: String? = null,
    textStyle: TextStyle = TextAppearance.body1(),
    cornerRadius: Dp = Radius.box,
    prefix: String? = "Rp ",
    suffix: String? = null,
    strokeColor: Color = Colors.Gray2,
    strokeWidth: Dp = 0.5.dp,
    focusedStrokeWidth: Dp? = null,
    strokeColorOnFocused: Color = Color.Black,
    maxLength: Int = Int.MAX_VALUE,
) {
    val transformation = remember { CurrencyVisualTransformation() }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    OutlinedTextFields(
        value = value,
        onValueChange = {
            onValueChange(filterPriceInput(it, maxIntegerDigits = maxLength))
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = textStyle,
        visualTransformation = transformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = imeAction,
        ),
        isError = isError,
        label = { Text(label, color = if (isFocused) strokeColorOnFocused else Colors.Gray3) },
        prefix = prefix?.let { { Text(it) } },
        suffix = suffix?.let { { Text(it) } },
        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    color = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        shape = RoundedCornerShape(cornerRadius),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = strokeColorOnFocused,
            unfocusedBorderColor = strokeColor,
        ),
        interactionSource = interactionSource,
        strokeWidth = strokeWidth,
        focusedStrokeWidth = focusedStrokeWidth
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutlinedTextFields(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
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
    strokeWidth: Dp = 0.5.dp,
    focusedStrokeWidth: Dp? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val currentStrokeWidth =
        if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else strokeWidth
    // If color is not provided via the text style, use content color as a default
    val textColor =
        textStyle.color.takeOrElse {
            val focused = interactionSource.collectIsFocusedAsState().value
            colors.textColor(enabled, isError, focused)
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier =
                modifier
                    .then(
                        if (label != null) {
                            Modifier
                                // Merge semantics at the beginning of the modifier chain to ensure
                                // padding is considered part of the text field.
                                .semantics(mergeDescendants = true) {}
                        } else {
                            Modifier
                        }
                    )
                    .defaultMinSize(
                        minWidth = OutlinedTextFieldDefaults.MinWidth,
                        minHeight = OutlinedTextFieldDefaults.MinHeight,
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
            decorationBox =
                @Composable { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        placeholder = placeholder,
                        label = label,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        prefix = prefix,
                        suffix = suffix,
                        supportingText = supportingText,
                        singleLine = singleLine,
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        contentPadding = PaddingValues(
                            top = Spacing.box,
                            bottom = Spacing.box,
                            start = Spacing.box,
                            end = Spacing.box,
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = colors,
                                shape = shape,
                                focusedBorderThickness = if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else currentStrokeWidth,
                                unfocusedBorderThickness = currentStrokeWidth,
                            )
                        },
                    )
                },
        )
    }
}

// ── Example / Preview ────────────────────────────────────────────────────────

@Composable
private fun CurrencyTextFieldExample() {
    var price by remember { mutableStateOf("0") }
    var discount by remember { mutableStateOf("0") }
    var decimal by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.normal),
        verticalArrangement = Arrangement.spacedBy(Spacing.normal),
    ) {
        // 1. Basic price field
        Text("1. Harga produk", style = TextAppearance.body2())
        CurrencyTextField(
            value = price,
            onValueChange = { price = it },
            label = "Harga",
        )
        Text(
            "raw: \"$price\"  →  double: ${price.toRawDouble()}",
            style = TextAppearance.body2(),
        )

        // 2. Discount field (separate state)
        Text("2. Diskon", style = TextAppearance.body2())
        CurrencyTextField(
            value = discount,
            onValueChange = { discount = it },
            label = "Diskon",
        )

        // 3. Field with decimal digits
        Text("3. Harga dengan desimal", style = TextAppearance.body2())
        CurrencyTextField(
            value = decimal,
            onValueChange = { decimal = it },
            label = "Harga (desimal)",
        )
        Text(
            "raw: \"$decimal\"  →  double: ${decimal.toRawDouble()}",
            style = TextAppearance.body2(),
        )
    }
}

@MobilePreview
@Composable
private fun CurrencyTextFieldMobilePreview() {
    HelperTheme { CurrencyTextFieldExample() }
}

@TabletPreview
@Composable
private fun CurrencyTextFieldTabletPreview() {
    HelperTheme { CurrencyTextFieldExample() }
}
