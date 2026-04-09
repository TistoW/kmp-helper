package com.tisto.kmp.helper.utils.ext

fun formatRupiahDigits(digits: String, includePrefix: Boolean): String {
    val clean = digits.filter { it.isDigit() }
    if (clean.isEmpty()) return ""

    val amount = clean.toLongOrNull() ?: return ""
    val grouped = formatWithDots(amount)

    return if (includePrefix) "Rp$grouped" else grouped
}

// Helper function untuk add separator "."
fun formatWithDots(amount: Long): String {
    if (amount == 0L) return "0"

    val isNegative = amount < 0
    val absoluteAmount = if (isNegative) -amount else amount

    // Convert to string and reverse
    val digits = absoluteAmount.toString()
    val reversed = digits.reversed()

    // Group by 3 and join with "."
    val grouped = reversed.chunked(3).joinToString(".")

    // Reverse back
    val result = grouped.reversed()

    return if (isNegative) "-$result" else result
}

// Hitung berapa digit (0-9) di kiri posisi cursor pada input mentah (sebelum diformat)
fun countDigitsLeft(input: String, cursor: Int): Int =
    input.take(cursor).count { it.isDigit() }

// Dapatkan index di string terformat setelah digit ke-N (N bisa 0)
fun indexAfterNthDigit(formatted: String, n: Int): Int {
    if (n <= 0) return 0
    var seen = 0
    for (i in formatted.indices) {
        if (formatted[i].isDigit()) {
            seen++
            if (seen == n) return i + 1 // tepat setelah digit ke-N
        }
    }
    return formatted.length
}