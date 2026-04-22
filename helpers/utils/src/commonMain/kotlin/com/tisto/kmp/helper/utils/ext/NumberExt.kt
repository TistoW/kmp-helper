package com.tisto.kmp.helper.utils.ext

import kotlin.math.abs
import kotlin.math.round

fun String?.toDoubleSafety(): Double {
    return if (this != null) {
        if (this.isEmpty()) {
            0.0
        } else {
            try {
                this.toDouble()
            } catch (e: NumberFormatException) {
                logs("ErrorNumber:" + e.message)
                0.0
            }
        }
    } else {
        0.0
    }
}

fun Double?.toDoubleSafety(): Double {
    return if (this != null) {
        try {
            this.toDouble()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0.0
        }
    } else {
        0.0
    }
}

fun Int?.toDoubleSafety(): Double {
    return if (this != null) {
        try {
            this.toDouble()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0.0
        }
    } else {
        0.0
    }
}

fun Long?.toDoubleSafety(): Double {
    return if (this != null) {
        try {
            this.toDouble()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0.0
        }
    } else {
        0.0
    }.also { logs("hasil toDoubleSafety : $it") }
}

fun Int?.toFloatSafety(): Float {
    return if (this != null) {
        try {
            this.toFloat()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0f
        }
    } else {
        0f
    }
}

fun String?.toIntSafety(): Int {
    return if (this != null) {
        if (this.isEmpty()) {
            0
        } else {
            try {
                this.toInt()
            } catch (e: NumberFormatException) {
                logs("ErrorNumber:" + e.message)
                0
            }
        }
    } else {
        0
    }
}

fun Boolean?.toBoolSafety(): Boolean {
    return this ?: false
}

fun Double?.toIntSafety(): Int {
    return if (this != null) {
        try {
            this.toInt()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0
        }
    } else {
        0
    }
}

fun Float?.toIntSafety(): Int {
    return if (this != null) {
        try {
            this.toInt()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0
        }
    } else {
        0
    }
}

fun Long?.toIntSafety(): Int {
    return if (this != null) {
        try {
            this.toInt()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0
        }
    } else {
        0
    }
}

fun Int?.toIntSafety(): Int {
    return if (this != null) {
        try {
            this.toInt()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0
        }
    } else {
        0
    }
}

fun Float?.toFloatSafety(): Float {
    return if (this != null) {
        try {
            this.toFloat()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0f
        }
    } else {
        0f
    }
}

fun Byte?.toIntSafety(): Int {
    return if (this != null) {
        try {
            this.toInt()
        } catch (e: NumberFormatException) {
            logs("ErrorNumber:" + e.message)
            0
        }
    } else {
        0
    }
}

fun String?.toStringForceEmpty(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    return this.toString()
}

fun Int?.discount(discount: Int?): Int {
    val value = this.def(0)
    return ((discount.def(0).toDouble() / 100) * value).toInt()
}

fun Double.roundToInt(): Int {
    return round(this).toInt()
}

fun Int.safetyValue(): String {
    return this.toString().ifCondition({ it == "0" }) { "" }
}

fun Double.safetyValue(): String {
    return this.toString().ifCondition({ it == "0" }) { "" }
}

fun Double.safetyPrice(): String {
    return this.reformatDecimal().ifCondition({ it == "0" }) { "" }
}

fun String.safetyOnChangeInt(): Int {
    return this.ifCondition({ v -> v == "" }) { "0" }.toIntSafety()
}

fun String.safetyOnChangeDouble(): Double {
    return this.ifCondition({ v -> v == "" }) { "0" }.toDoubleSafety()
}


fun Double?.reformatDecimal(): String {
    val value = this.def(0.0)
    return value.formatCurrency()
}

fun Number?.formatRupiah(
    showCurrency: Boolean = true,
    maxFractionDigits: Int = 3,
    rounding: KmpRounding = KmpRounding.DOWN
): String {
    return formatCurrency(showCurrency, maxFractionDigits, rounding)
}

// ========== TEST ==========
fun main() {
    println(200.2.formatCurrency()) // "200,2" ✅
    println(200.1.formatCurrency()) // "200,1" ✅
    println(200.199.formatCurrency()) // "200,199" ✅
    println(200.099.formatCurrency()) // "200,099" ✅
    println(200.2.formatCurrency(maxFractionDigits = 1)) // "200,2" ✅
    println(
        200.26.formatCurrency(
            maxFractionDigits = 1,
            rounding = KmpRounding.HALF_UP
        )
    ) // "200,3" ✅
}


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

    // FIX: Gunakan toPlainString() untuk avoid scientific notation
    val valueStr = absValue.toBigDecimal().toPlainString()
    val parts = valueStr.split(".")

    val integerPart = parts[0].toLongOrNull() ?: 0L
    val fractionStr = parts.getOrNull(1) ?: ""

    val fraction = if (fractionStr.isNotEmpty()) {
        processFraction(fractionStr, maxFractionDigits, rounding)
    } else {
        ""
    }

    val formattedInt = formatWithDot(integerPart)
    val formattedFraction = if (fraction.isNotEmpty()) ",$fraction" else ""

    val result = buildString {
        append(formattedInt)
        append(formattedFraction)
    }

    return if (showCurrency) "${if (isNegative) "-" else ""}Rp$result" else result
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
