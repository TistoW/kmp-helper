package com.tisto.kmp.helper.utils.ext

import kotlin.math.abs

fun Number?.formatCurrency(
    showCurrency: Boolean = false,
    maxFractionDigits: Int = 3,
    rounding: KmpRounding = KmpRounding.DOWN
): String {
//    if (this == null) return if (showCurrency) "Rp0" else "0"
//
//    val locale = Locale("in", "ID")
//    val symbols = DecimalFormatSymbols(locale).apply {
//        groupingSeparator = '.'
//        decimalSeparator = ','
//    }
//
//    val df = DecimalFormat().apply {
//        decimalFormatSymbols = symbols
//        isGroupingUsed = true
//        minimumFractionDigits = 0       // no forced “,000”
//        maximumFractionDigits = maxFractionDigits
//        roundingMode = rounding
//    }
//
//    // format through BigDecimal to reduce Double artifacts
//    val bd = when (this) {
//        is BigDecimal -> this
//        else -> BigDecimal.valueOf(this.toDouble())
//    }
//
//    val numberPart = df.format(bd)
//    return if (showCurrency) "Rp$numberPart" else numberPart

    if (this == null) return if (showCurrency) "Rp0" else "0"

    val value = this.toDouble()
    val isNegative = value < 0
    val absValue = abs(value)

    // ✅ FIX: Convert to string dulu untuk avoid floating point precision
    val valueStr = absValue.toString()
    val parts = valueStr.split(".")

    val integerPart = parts[0].toLongOrNull() ?: 0L
    val fractionStr = parts.getOrNull(1) ?: ""

    // ✅ FIX: Process fraction sebagai string
    val fraction = if (fractionStr.isNotEmpty()) {
        processFraction(fractionStr, maxFractionDigits, rounding)
    } else {
        ""
    }

    val formattedInt = formatWithDot(integerPart)
    val formattedFraction = if (fraction.isNotEmpty()) ",$fraction" else ""

    val result = buildString {
        if (isNegative) append("-")
        append(formattedInt)
        append(formattedFraction)
    }

    return if (showCurrency) "Rp$result" else result
}

// ✅ NEW: Process fraction as string
private fun processFraction(
    fractionStr: String,
    maxDigits: Int,
    rounding: KmpRounding
): String {
    if (maxDigits <= 0) return ""

    // Pad atau truncate ke maxDigits
    val padded = fractionStr.padEnd(maxDigits + 1, '0')
    val current = padded.take(maxDigits)
    val nextDigit = padded.getOrNull(maxDigits)?.digitToIntOrNull() ?: 0

    // Apply rounding
    val shouldRoundUp = when (rounding) {
        KmpRounding.DOWN -> false
        KmpRounding.UP -> nextDigit > 0
        KmpRounding.HALF_UP -> nextDigit >= 5
    }

    val result = if (shouldRoundUp) {
        // Round up last digit
        val asNumber = current.toLongOrNull() ?: return current
        (asNumber + 1).toString().padStart(maxDigits, '0')
    } else {
        current
    }

    // Remove trailing zeros
    return result.trimEnd('0')
}

private fun formatWithDot(amount: Long): String {
    if (amount == 0L) return "0"
    return amount.toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
}

enum class KmpRounding {
    DOWN,
    UP,
    HALF_UP
}

fun String?.formatCurrency(showCurrency: Boolean = false): String {
    return this.toDoubleSafety().formatCurrency(showCurrency)
}

fun Int?.toRupiah(hideCurrency: Boolean = false): String {
    return (this ?: 0).formatCurrency(!hideCurrency)
}

fun Double?.toRupiah(hideCurrency: Boolean = false): String {
    return (this ?: 0.0).formatCurrency(!hideCurrency)
}

fun String?.toRupiah(hideCurrency: Boolean = false): String {
    return (this ?: "0").formatCurrency(!hideCurrency)
}

fun Double?.formatRupiah(hideCurrency: Boolean = false): String {
    return (this ?: 0.0).formatCurrency(!hideCurrency)
}

fun String?.formatRupiah(hideCurrency: Boolean = false): String {
    return (this ?: "0").formatCurrency(!hideCurrency)
}