package com.tisto.kmp.helper.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * Read-only date field that opens a [DatePickerDialog] on tap.
 *
 * @param value          Currently displayed date string ("yyyy-MM-dd").
 * @param label          Field label.
 * @param showPicker     Whether the dialog is currently open.
 * @param onToggle       Open/close the dialog.
 * @param onDateSelected Called with the chosen "yyyy-MM-dd" string on confirm.
 * @param modifier       Forwarded to the text field.
 * @param enabled        When false the field is non-interactive.
 * @param endIcon        Icon shown on the right (defaults to calendar).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: String,
    label: String,
    showPicker: Boolean,
    onToggle: () -> Unit,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    endIcon: ImageVector = Icons.Default.DateRange,
) {
    SelectableTextField(
        value = value,
        label = label,
        strokeWidth = 1.dp,
        modifier = modifier,
        endIcon = endIcon,
        enabled = enabled,
        onClick = { if (enabled) onToggle() },
    )

    if (showPicker) {
        val initialMillis = remember(value) {
            runCatching {
                LocalDate.parse(value)
                    .atStartOfDayIn(TimeZone.UTC)
                    .toEpochMilliseconds()
            }.getOrNull()
        }
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

        DatePickerDialog(
            onDismissRequest = onToggle,
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC)
                            .date
                            .toString()
                        onDateSelected(date)
                    }
                    onToggle()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = onToggle) { Text("Batal") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
