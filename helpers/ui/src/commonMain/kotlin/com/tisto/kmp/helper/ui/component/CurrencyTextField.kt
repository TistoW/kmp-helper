package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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
@Composable
fun CurrencyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
) {
    val transformation = remember { CurrencyVisualTransformation() }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(filterPriceInput(it)) },
        label = { Text(label) },
        prefix = { Text("Rp ") },
        visualTransformation = transformation,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction,
        ),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
    )
}
