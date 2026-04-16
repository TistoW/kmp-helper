package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import kotlin.math.roundToLong

// ─── Raw string format ────────────────────────────────────────────────────────
//
// State stores:  digits + optional comma + up to 3 decimal digits
//   e.g. "10000,9"  "75000"  "1500000,25"
//
// Display format (CurrencyVisualTransformation):
//   dot as thousands separator, comma as decimal separator, max 3 decimal digits
//   e.g. "10.000,9"  "75.000"  "1.500.000,25"

/**
 * Filters user input to only allow a valid price string:
 * digits, at most one comma, and at most [maxDecimalDigits] digits after it.
 * A comma is only accepted after at least one digit (prevents leading comma).
 */
fun filterPriceInput(v: String, maxDecimalDigits: Int = 3): String {
    var hasComma = false
    var decCount = 0
    return buildString {
        for (c in v) {
            when {
                c.isDigit() -> {
                    if (!hasComma) append(c)
                    else if (decCount < maxDecimalDigits) { append(c); decCount++ }
                }
                c == ',' && !hasComma && isNotEmpty() -> {
                    hasComma = true
                    append(c)
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

// ─── VisualTransformation ─────────────────────────────────────────────────────
//
// State stores raw string ("10000,9"); display shows "10.000,9".
// Only dots are added as thousands separators — comma is passed through as-is.
// OffsetMapping treats inserted dots as zero-width to the cursor.

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
        val commaIdx = raw.indexOf(',')
        return if (commaIdx >= 0) {
            val intPart = raw.substring(0, commaIdx).ifEmpty { "0" }
            val decPart = raw.substring(commaIdx + 1)
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
 * - Stores raw input as "10000,9" (comma as decimal separator)
 * - Displays formatted output as "10.000,9" (dot as thousands separator)
 * - Prefixes with "Rp "
 * - Only allows valid numeric/decimal input via [filterPriceInput]
 *
 * [value] and [onValueChange] operate on the **raw** string.
 * Use [toRawDouble] / [toRawPriceString] to convert between raw and Double.
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
) {
    val transformation = remember { CurrencyVisualTransformation() }
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(filterPriceInput(it)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = textStyle,
            visualTransformation = transformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction,
            ),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = transformation,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = { Text(label) },
                    prefix = prefix?.let { { Text(it) } },
                    contentPadding = PaddingValues(
                        top = Spacing.box,
                        bottom = Spacing.box,
                        start = Spacing.box,
                        end = Spacing.box,
                    ),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = true,
                            isError = isError,
                            interactionSource = interactionSource,
                            shape = RoundedCornerShape(cornerRadius),
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
