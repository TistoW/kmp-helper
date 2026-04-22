package com.tisto.kmp.helper.utils.ext

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime


const val defaultDateFormat = "yyyy-MM-dd HH:mm:ss"
const val defaultDateFormatMillisecond = "yyyy-MM-dd HH:mm:ss.SSS"
const val dateExampleUTC = "1990-01-01T00:00:00.000000Z"
const val dateExample = "1990-01-01 00:00:00"
const val defaultUTCDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

// ========== MONTH NAMES ==========
private val monthNamesIndonesia = listOf(
    "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
    "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
)

private val monthNamesEnglish = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

private val monthNamesFullIndonesia = listOf(
    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
)

private val monthNamesFullEnglish = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

@OptIn(ExperimentalTime::class)
fun currentTime(): String {
    val now = Clock.System.now()
    val local = now.toLocalDateTime(TimeZone.currentSystemDefault())
    fun Int.twoDigit() = this.toString().padStart(2, '0')
    return "${local.hour.twoDigit()}:${local.minute.twoDigit()}:${local.second.twoDigit()}"
}

/**
 * Get current datetime as string
 */
@OptIn(ExperimentalTime::class)
fun currentDate(format: String = "yyyy-MM-dd"): String {
    return getCurrentDateTime(format)
}

/**
 * Get current datetime as string
 */
@OptIn(ExperimentalTime::class)
fun currentDateTime(format: String = defaultDateFormat): String {
    return getCurrentDateTime(format)
}

/**
 * Get current datetime as string
 */
@OptIn(ExperimentalTime::class)
fun getCurrentDateTime(format: String = defaultDateFormat): String {
    val now = Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    return formatDateTime(localDateTime, format)
}

fun String?.reformatDate(
    toFormat: String = "dd MMM yyyy HH:mm:ss",
    fromFormat: String = defaultDateFormat
): String {
    return this?.convertDate(toFormat, fromFormat).def(dummyResult(toFormat))
}

fun String?.formatDate(
    toFormat: String = "dd MMM yyyy HH:mm:ss",
    fromFormat: String = defaultDateFormat
): String {
    val date  = this?:dateExample
    return date.convertDate(toFormat, fromFormat)
}

/**
 * Convert string date dari satu format ke format lain
 * Otomatis detect UTC (ada "Z")
 */
fun String.convertDate(
    toFormat: String = "dd MMM yyyy HH:mm:ss",
    fromFormat: String = defaultDateFormat
): String {
    // Detect UTC format
    if (this.contains("Z", ignoreCase = true)) {
        return this.convertFromUTC(toFormat = toFormat)
    }

    return try {
        // Parse dari format input
        val localDateTime = parseDateTime(this, fromFormat)

        // Format ke output
        formatDateTime(localDateTime, toFormat)
    } catch (e: Exception) {
        logs("Error Time Format: ${e.message}")
        // Fallback ke dummy date
        dummyResult(toFormat)
    }
}

/**
 * Convert dari UTC ke timezone tertentu (default: Asia/Jakarta)
 */
@OptIn(ExperimentalTime::class)
fun String?.convertFromUTC(
    toFormat: String = defaultDateFormat,
    fromFormat: String = defaultUTCDateFormat,
    timeZone: String = "Asia/Jakarta"
): String {
    if (this == null) return dummyResult(toFormat)

    return try {
        // Parse UTC instant
        val instant = parseUTCInstant(this, fromFormat)

        // Convert ke timezone
        val targetTimeZone = TimeZone.of(timeZone)
        val localDateTime = instant.toLocalDateTime(targetTimeZone)

        // Format output
        val result = formatDateTime(localDateTime, toFormat)

        // Replace " 24" dengan " 00" (edge case midnight)
        result.replace(" 24", " 00")
    } catch (e: Exception) {
        println("Error Time Format: ${e.message}")
        dummyResult(toFormat)
    }
}

/**
 * Parse datetime string ke LocalDateTime
 */
@OptIn(ExperimentalTime::class)
private fun parseDateTime(dateStr: String, format: String): LocalDateTime {
    return when {
        // ISO format standard
        format.contains("'T'") || dateStr.contains("T") -> {
            kotlin.time.Instant.parse(dateStr).toLocalDateTime(TimeZone.currentSystemDefault())
        }

        // Custom format - manual parsing
        else -> {
            val cleaned = dateStr.trim()

            // Format: yyyy-MM-dd HH:mm:ss atau yyyy-MM-dd HH:mm:ss.SSS
            val parts = cleaned.split(" ")
            if (parts.size < 2) throw IllegalArgumentException("Invalid date format")

            val datePart = parts[0].split("-")
            val timePart = parts[1].split(":")

            val year = datePart[0].toInt()
            val month = datePart[1].toInt()
            val day = datePart[2].toInt()

            val hour = timePart[0].toInt()
            val minute = timePart[1].toInt()

            // Handle seconds with milliseconds
            val secondsPart = timePart[2].split(".")
            val second = secondsPart[0].toInt()
            val millisecond = if (secondsPart.size > 1) {
                secondsPart[1].padEnd(3, '0').take(3).toInt()
            } else 0

            LocalDateTime(year, month, day, hour, minute, second, millisecond * 1_000_000)
        }
    }
}

/**
 * Parse UTC instant dari string
 */
@OptIn(ExperimentalTime::class)
private fun parseUTCInstant(dateStr: String, format: String): Instant {
    return when {
        // Standard ISO format dengan Z
        dateStr.endsWith("Z") -> {
            try {
                Instant.parse(dateStr)
            } catch (e: Exception) {
                // Handle format dengan microseconds (6 digits)
                // Convert ke milliseconds (3 digits)
                val normalized = dateStr.replace(Regex("\\.\\d{6}Z"), ".000Z")
                Instant.parse(normalized)
            }
        }

        // Custom UTC format
        else -> {
            val localDateTime = parseDateTime(dateStr, format)
            localDateTime.toInstant(TimeZone.UTC)
        }
    }
}

/**
 * Format LocalDateTime ke string sesuai pattern
 * ✅ FIXED: Support MMM (short month) dan MMMM (full month)
 */
private fun formatDateTime(dateTime: LocalDateTime, format: String): String {
    var result = format
    val monthHandled: Boolean

    // Month names (harus sebelum MM!)
    if (result.contains("MMMM")) {
        // Full month name
        val monthName = monthNamesFullEnglish[dateTime.monthNumber - 1]
        result = result.replace("MMMM", monthName)
        monthHandled = true
    } else if (result.contains("MMM")) {
        // Short month name
        val monthName = monthNamesEnglish[dateTime.monthNumber - 1]
        result = result.replace("MMM", monthName)
        monthHandled = true
    } else {
        monthHandled = false
    }

    // Year
    result = result.replace("yyyy", dateTime.year.toString().padStart(4, '0'))

    // Month number (setelah month names!, skip jika sudah pakai MMM/MMMM)
    if (!monthHandled) {
        result = result.replace("MM", dateTime.monthNumber.toString().padStart(2, '0'))
        result = result.replace("M", dateTime.monthNumber.toString())
    }

    // Day
    result = result.replace("dd", dateTime.dayOfMonth.toString().padStart(2, '0'))
    result = result.replace("d", dateTime.dayOfMonth.toString())

    // Hour (24-hour format)
    result = result.replace("HH", dateTime.hour.toString().padStart(2, '0'))
    result = result.replace("kk", dateTime.hour.toString().padStart(2, '0'))
    result = result.replace("H", dateTime.hour.toString())

    // Minute
    result = result.replace("mm", dateTime.minute.toString().padStart(2, '0'))
    result = result.replace("m", dateTime.minute.toString())

    // Second
    result = result.replace("ss", dateTime.second.toString().padStart(2, '0'))
    result = result.replace("s", dateTime.second.toString())

    // Millisecond
    val millis = (dateTime.nanosecond / 1_000_000).toString().padStart(3, '0')
    result = result.replace("SSS", millis)
    result = result.replace("SS", millis.take(2))
    result = result.replace("S", millis.take(1))

    return result
}

/**
 * Generate dummy result sesuai format
 */
private fun dummyResult(format: String): String {
    val dummyDateTime = LocalDateTime(2000, 1, 1, 1, 0, 0)
    return formatDateTime(dummyDateTime, format)
}

// ========== EXTENSION FUNCTIONS ==========

/**
 * Convert Long (timestamp millis) to formatted string
 */
@OptIn(ExperimentalTime::class)
fun Long.toDateString(
    format: String = defaultDateFormat,
    timeZone: String = "Asia/Jakarta"
): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val targetTimeZone = TimeZone.of(timeZone)
    val localDateTime = instant.toLocalDateTime(targetTimeZone)
    return formatDateTime(localDateTime, format)
}

/**
 * Convert formatted string to timestamp millis
 */
@OptIn(ExperimentalTime::class)
fun String.toTimestamp(format: String = defaultDateFormat): Long? {
    return try {
        val localDateTime = parseDateTime(this, format)
        localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    } catch (e: Exception) {
        null
    }
}

fun getTimeDifferenceString(startTime: String? = null): String? {
    if (startTime == null) return null

    val startInstant = try {
        // Parse "yyyy-MM-dd HH:mm:ss" → replace space with T for ISO format
        val isoFormat = startTime.replace(" ", "T")
        LocalDateTime.parse(isoFormat)
            .toInstant(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
        return "Invalid date"
    }

    val currentInstant = Clock.System.now()
    val diffInMillis = currentInstant.toEpochMilliseconds() - startInstant.toEpochMilliseconds()

    val totalSeconds = diffInMillis / 1000
    val totalMinutes = totalSeconds / 60
    val totalHours = totalMinutes / 60
    val totalDays = totalHours / 24

    return when {
        totalMinutes < 1 -> "$totalMinutes menit"
        totalMinutes < 60 -> "$totalMinutes menit"
        totalHours < 24 -> {
            val remainingMinutes = totalMinutes % 60
            "$totalHours jam $remainingMinutes menit"
        }
        else -> "$totalDays hari"
    }
}