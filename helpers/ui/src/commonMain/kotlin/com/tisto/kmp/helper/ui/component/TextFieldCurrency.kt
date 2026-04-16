package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
import kotlin.math.roundToLong

// ─── Raw string format ────────────────────────────────────────────────────────
//
// State stores:  digits + optional decimal separator (comma) + up to 3 decimal digits
//   e.g. "10000,9"  "75000"  "1500000,25"
//
// Display format (formatCurrency):
//   dot as thousands separator, comma as decimal separator, max 3 decimal digits
//   e.g. "10.000,9"  "75.000"  "1.500.000,25"
//
// BasicTextField receives the already-formatted display string directly —
// NO VisualTransformation is used. This avoids the IME composition bug where
// VisualTransformation that changes text length breaks comma/period input.

/**
 * Filters user input to only allow a valid price string:
 * digits, at most one decimal separator (','), and at most [maxDecimalDigits]
 * digits after it.
 */
fun filterPriceInput(
    v: String,
    maxDecimalDigits: Int = 3,
    maxIntegerDigits: Int = Int.MAX_VALUE,
): String {
    var hasDec = false
    var intCount = 0
    var decCount = 0
    return buildString {
        for (c in v) {
            when {
                c.isDigit() -> {
                    if (!hasDec) {
                        if (intCount < maxIntegerDigits) { append(c); intCount++ }
                    } else if (decCount < maxDecimalDigits) {
                        append(c); decCount++
                    }
                }

                (c == ',') && !hasDec && isNotEmpty() -> {
                    hasDec = true
                    append(',')
                }
            }
        }
    }
}

/** Parses a raw price string to Double. "10000,9" → 10000.9 */
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

// ─── Formatting helpers ──────────────────────────────────────────────────────
//
// These convert between raw ("10000,9") and display ("10.000,9") formats.
// The BasicTextField shows the display format directly — no VisualTransformation.

/** "10000,9" → "10.000,9" */
private fun formatCurrency(raw: String): String {
    if (raw.isEmpty()) return ""
    val decIdx = raw.indexOf(',')
    return if (decIdx >= 0) {
        val intPart = raw.substring(0, decIdx).ifEmpty { "0" }
        val decPart = raw.substring(decIdx + 1)
        "${formatIntPart(intPart)},${decPart}"
    } else {
        formatIntPart(raw)
    }
}

/** "10.000,9" → "10000,9" */
private fun stripThousands(display: String): String = buildString {
    for (c in display) {
        if (c != '.') append(c)
    }
}

private fun formatIntPart(digits: String): String {
    val long = digits.toLongOrNull() ?: return digits
    if (long == 0L) return "0"
    return long.toString().reversed().chunked(3).joinToString(".").reversed()
}

/** Map a cursor position in raw string to a position in formatted string. */
private fun rawCursorToFormatted(rawPos: Int, formatted: String): Int {
    var rawSeen = 0
    formatted.forEachIndexed { i, c ->
        if (rawSeen == rawPos) return i
        if (c != '.') rawSeen++
    }
    return formatted.length
}

/** Map a cursor position in formatted string to a position in raw string. */
private fun formattedCursorToRaw(fmtPos: Int, formatted: String): Int =
    formatted.take(fmtPos.coerceAtMost(formatted.length)).count { it != '.' }

// ─── Composable ───────────────────────────────────────────────────────────────

/**
 * A currency-aware outlined text field that:
 * - Stores raw input as "10000,9" (comma as decimal separator)
 * - Displays formatted output as "10.000,9" (dot as thousands separator)
 * - Prefixes with "Rp "
 * - Only allows valid numeric/decimal input via [filterPriceInput]
 *
 * [value] and [onValueChange] operate on the **raw** string.
 * Use [toRawDouble] / [toRawPriceString] to convert between raw and Double.
 *
 * Uses BasicTextField + DecorationBox so [contentPadding] can be controlled,
 * keeping height consistent with PasswordTextField / SearchTextField / CustomTextField.
 *
 * The field manages formatting internally via [TextFieldValue] — no
 * VisualTransformation is used, which avoids the IME composition bug where
 * length-changing transformations break comma/period input on Android.
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    // The formatted display string derived from the raw value.
    val formatted = remember(value) { formatCurrency(value) }
    // TextFieldValue that the BasicTextField actually sees (formatted text + cursor).
    var tfv by remember(value) {
        mutableStateOf(TextFieldValue(formatted, TextRange(formatted.length)))
    }
    // Keep tfv in sync when external value changes (e.g. reset).
    if (tfv.text != formatted && stripThousands(tfv.text) != value) {
        tfv = TextFieldValue(formatted, TextRange(formatted.length))
    }

    Column(modifier = modifier) {
        BasicTextField(
            value = tfv,
            onValueChange = { newTfv ->
                // 1. Strip thousands dots to get raw-like text
                val typed = stripThousands(newTfv.text)
                // 2. Convert any typed '.' to ',' (period key → decimal comma)
                val normalized = typed.replace('.', ',')
                // 3. Filter to valid price input
                val newRaw = filterPriceInput(normalized, maxIntegerDigits = maxLength)
                // 4. Format for display
                val newFormatted = formatCurrency(newRaw)
                // 5. Map cursor from the user's typing position to the new formatted string
                val rawCursor = formattedCursorToRaw(newTfv.selection.start, newTfv.text)
                    .coerceAtMost(newRaw.length)
                val fmtCursor = rawCursorToFormatted(rawCursor, newFormatted)
                tfv = TextFieldValue(newFormatted, TextRange(fmtCursor))
                // 6. Notify parent with the raw value
                if (newRaw != value) onValueChange(newRaw)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction,
            ),
            cursorBrush = SolidColor(strokeColorOnFocused),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = tfv.text,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = { Text(label) },
                    prefix = prefix?.let { { Text(it) } },
                    suffix = suffix?.let { { Text(it) } },
                    contentPadding = PaddingValues(
                        top = Spacing.box,
                        bottom = Spacing.box,
                        start = Spacing.box,
                        end = Spacing.box,
                    ),
                    container = {
                        val currentStrokeWidth = if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else strokeWidth
                        OutlinedTextFieldDefaults.Container(
                            enabled = true,
                            isError = isError,
                            interactionSource = interactionSource,
                            shape = RoundedCornerShape(cornerRadius),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = strokeColorOnFocused,
                                unfocusedBorderColor = strokeColor,
                            ),
                            focusedBorderThickness = if (isFocused && focusedStrokeWidth != null) focusedStrokeWidth else currentStrokeWidth,
                            unfocusedBorderThickness = currentStrokeWidth,
                        )
                    },
                )
            },
        )
        if (supportingText != null) {
            Text(
                text = supportingText,
                color = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = Spacing.box, top = 4.dp),
            )
        }
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
