package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DateFilterDialog(
    initialStartDate: String,
    initialEndDate: String,
    activePreset: String?,
    onApply: (startDate: String, endDate: String) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit,
) {
    val tz = TimeZone.currentSystemDefault()
    val today = Clock.System.now().toLocalDateTime(tz).date

    var selectedPreset by remember { mutableStateOf(activePreset) }
    var dialogStart by remember {
        mutableStateOf(parseLocalDate(initialStartDate) ?: today)
    }
    var dialogEnd by remember {
        mutableStateOf(parseLocalDate(initialEndDate) ?: today)
    }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val presets = listOf(
        "today" to DateStrings.presetToday,
        "yesterday" to DateStrings.presetYesterday,
        "7days" to DateStrings.preset7Days,
        "30days" to DateStrings.preset30Days,
        "thisWeek" to DateStrings.presetThisWeek,
        "lastWeek" to DateStrings.presetLastWeek,
        "thisMonth" to DateStrings.presetThisMonth,
        "lastMonth" to DateStrings.presetLastMonth,
        "thisYear" to DateStrings.presetThisYear,
    )

    fun applyPreset(preset: String) {
        selectedPreset = preset
        val (s, e) = computePresetRange(preset, today)
        dialogStart = s
        dialogEnd = e
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(Spacing.large),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = DateStrings.filterDate,
                    style = TextAppearance.body1Bold(),
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }

            Spacer(Modifier.height(Spacing.normal))

            // Preset chips grid
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                verticalArrangement = Arrangement.spacedBy(Spacing.small),
            ) {
                presets.forEach { (key, label) ->
                    val isSelected = selectedPreset == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { applyPreset(key) },
                        label = { Text(label, style = TextAppearance.body2()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Black,
                            selectedLabelColor = Color.White,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            selectedBorderColor = Color.Black,
                            borderColor = Colors.Gray3,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(Spacing.normal))

            // From date field
            OutlinedTextField(
                value = dialogStart.toDisplayDateLong(),
                onValueChange = {},
                readOnly = true,
                label = { Text(DateStrings.fromDate) },
                trailingIcon = {
                    IconButton(onClick = { showStartPicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showStartPicker = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = Colors.Gray3,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )

            Spacer(Modifier.height(Spacing.small))

            // To date field
            OutlinedTextField(
                value = dialogEnd.toDisplayDateLong(),
                onValueChange = {},
                readOnly = true,
                label = { Text(DateStrings.toDate) },
                trailingIcon = {
                    IconButton(onClick = { showEndPicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEndPicker = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = Colors.Gray3,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )

            Spacer(Modifier.height(Spacing.large))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.box),
            ) {
                Button(
                    onClick = onReset,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = {
                        onApply(dialogStart.toApiString(), dialogEnd.toApiString())
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                ) {
                    Text(DateStrings.btnSave)
                }
            }
        }
    }

    // Start date picker
    if (showStartPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = dialogStart.toUtcMillis(),
        )
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let {
                        dialogStart = it.toLocalDate()
                        if (dialogStart > dialogEnd) dialogEnd = dialogStart
                        selectedPreset = null
                    }
                    showStartPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Batal") }
            },
        ) { DatePicker(state = pickerState) }
    }

    // End date picker
    if (showEndPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = dialogEnd.toUtcMillis(),
        )
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let {
                        dialogEnd = it.toLocalDate()
                        if (dialogEnd < dialogStart) dialogStart = dialogEnd
                        selectedPreset = null
                    }
                    showEndPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Batal") }
            },
        ) { DatePicker(state = pickerState) }
    }
}

val MONTHS_ID_LONG = listOf(
    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
    "Juli", "Agustus", "September", "Oktober", "November", "Desember",
)

@Suppress("DEPRECATION")
fun LocalDate.toDisplayDateLong(): String =
    "$dayOfMonth ${MONTHS_ID_LONG[monthNumber - 1]} $year"

@Suppress("DEPRECATION")
fun LocalDate.toApiString(): String =
    "${year}-${monthNumber.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"

fun LocalDate.toUtcMillis(): Long =
    atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

fun Long.toLocalDate(): LocalDate =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC).date

fun parseLocalDate(date: String): LocalDate? = try {
    val parts = date.split("-")
    if (parts.size == 3) LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt()) else null
} catch (_: Exception) {
    null
}

fun computePresetRange(preset: String, today: LocalDate): Pair<LocalDate, LocalDate> {
    return when (preset) {
        "yesterday" -> {
            val y = today.minus(1, DateTimeUnit.DAY)
            y to y
        }
        "7days" -> today.minus(7, DateTimeUnit.DAY) to today
        "30days" -> today.minus(30, DateTimeUnit.DAY) to today
        "thisWeek" -> {
            // ISO week: Monday = 1, Sunday = 7
            val start = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
            start to start.plus(6, DateTimeUnit.DAY)
        }
        "lastWeek" -> {
            val thisWeekStart = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
            val start = thisWeekStart.minus(7, DateTimeUnit.DAY)
            start to thisWeekStart.minus(1, DateTimeUnit.DAY)
        }
        "thisMonth" -> {
            val start = LocalDate(today.year, today.month, 1)
            val end = start.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
            start to end
        }
        "lastMonth" -> {
            val thisMonthStart = LocalDate(today.year, today.month, 1)
            val start = thisMonthStart.minus(1, DateTimeUnit.MONTH)
            val end = thisMonthStart.minus(1, DateTimeUnit.DAY)
            start to end
        }
        "thisYear" -> LocalDate(today.year, 1, 1) to LocalDate(today.year, 12, 31)
        else -> today to today  // "today"
    }
}

internal object DateStrings {

    const val columnDate = "Tanggal"
    const val loadFailed = "Gagal memuat data"

    // Date filter bar
    const val filterDate = "Filter Tanggal"
    const val fromDate = "Dari Tanggal"
    const val toDate = "Sampai Tanggal"
    const val btnReset = "Reset"
    const val btnSave = "Simpan"

    // Quick tabs
    const val presetToday = "Hari ini"
    const val preset7Days = "7 hari terakhir"
    const val preset30Days = "30 hari terakhir"

    // Date dialog presets
    const val presetYesterday = "Kemarin"
    const val presetThisWeek = "Minggu ini"
    const val presetLastWeek = "Minggu lalu"
    const val presetThisMonth = "Bulan ini"
    const val presetLastMonth = "Bulan lalu"
    const val presetThisYear = "Tahun ini"

    // Product filter
    const val product = "Produk"
    const val allProducts = "Semua Produk"

    // Backward-compat aliases used by existing code
    const val filterPeriod = "Periode"
    const val today = presetToday
    const val last7Days = preset7Days
    const val last30Days = preset30Days
}