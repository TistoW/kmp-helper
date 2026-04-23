package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.icon.myicon.IcFilterSolar
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.utils.model.FilterGroup
import com.tisto.kmp.helper.utils.model.FilterItem
import com.tisto.kmp.helper.utils.model.FilterType
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.apply
import kotlin.collections.filterNotNull
import kotlin.collections.firstOrNull
import kotlin.collections.toMutableMap
import kotlin.time.Clock

@Composable
fun simpleFilter(): List<FilterGroup> {
    return listOf(
        FilterGroup(
            title = "Urutkan",
            type = FilterType.SORT,
            listOf(
                FilterItem("Nama: A-Z", "asc", "name"),
                FilterItem("Nama: Z-A", "desc", "name"),
                FilterItem("Terbaru", "desc", "createdAt"),
                FilterItem("Terlama", "asc", "createdAt"),
                FilterItem("Terakhir Diubah", "desc", "updatedAt")
            )
        ),
        FilterGroup(
            title = "Status",
            type = FilterType.FILTER,
            listOf(
                FilterItem("Active", "true", "isActive"),
                FilterItem("Non Active", "false", "isActive"),
            )
        )
    )
}

@Composable
fun defaultFilter() = listOf(
    FilterGroup(
        title = "Urutkan",
        type = FilterType.SORT,
        listOf(
            FilterItem("Nama: A-Z", "asc", "name"),
            FilterItem("Nama: Z-A", "desc", "name"),
            FilterItem("Terbaru", "desc", "createdAt"),
            FilterItem("Terlama", "asc", "createdAt"),
        )
    )
)

/**
 * Result for DATE_RANGE filter groups.
 * @param preset  The selected preset key (e.g. "today", "thisWeek", "custom") or empty if reset.
 * @param startDate Start date in "yyyy-MM-dd" format.
 * @param endDate   End date in "yyyy-MM-dd" format.
 */
data class DateRangeResult(
    val preset: String = "",
    val startDate: String = "",
    val endDate: String = "",
)

data class DatePresetOption(
    val label: String,
    val value: String,
)

/**
 * Resolves a preset key into a (startDate, endDate) pair in "yyyy-MM-dd" format.
 */
fun resolveDatesFromPreset(preset: String): Pair<String, String> {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return when (preset) {
        "today" -> today.toString() to today.toString()
        "yesterday" -> {
            val y = today.minus(DatePeriod(days = 1))
            y.toString() to y.toString()
        }
        "last7" -> today.minus(DatePeriod(days = 6)).toString() to today.toString()
        "last30" -> today.minus(DatePeriod(days = 29)).toString() to today.toString()
        "thisWeek" -> {
            val dayOfWeek = today.dayOfWeek.ordinal // Monday=0
            val start = today.minus(DatePeriod(days = dayOfWeek))
            val end = start.plus(DatePeriod(days = 6))
            start.toString() to end.toString()
        }
        "lastWeek" -> {
            val dayOfWeek = today.dayOfWeek.ordinal
            val thisWeekStart = today.minus(DatePeriod(days = dayOfWeek))
            val lastWeekStart = thisWeekStart.minus(DatePeriod(days = 7))
            val lastWeekEnd = lastWeekStart.plus(DatePeriod(days = 6))
            lastWeekStart.toString() to lastWeekEnd.toString()
        }
        "thisMonth" -> {
            val start = LocalDate(today.year, today.monthNumber, 1)
            start.toString() to today.toString()
        }
        "lastMonth" -> {
            val firstThisMonth = LocalDate(today.year, today.monthNumber, 1)
            val lastMonthEnd = firstThisMonth.minus(DatePeriod(days = 1))
            val lastMonthStart = LocalDate(lastMonthEnd.year, lastMonthEnd.monthNumber, 1)
            lastMonthStart.toString() to lastMonthEnd.toString()
        }
        "thisYear" -> {
            val start = LocalDate(today.year, 1, 1)
            start.toString() to today.toString()
        }
        else -> today.toString() to today.toString()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralFilterBottomSheet(
    onClose: () -> Unit,
    options: List<FilterGroup> = defaultFilter(),
    preselected: List<FilterItem> = listOf(),
    datePresets: List<DatePresetOption> = emptyList(),
    currentDateRange: DateRangeResult? = null,
    onApply: (List<FilterItem>) -> Unit = {},
    onDateRangeApply: (DateRangeResult) -> Unit = {},
) {

    var selectedMap by remember {
        mutableStateOf(buildMap {
            options.forEach { group ->
                if (group.selected != null) {
                    put(group.title, group.selected)
                }
            }
            preselected.forEach { item ->
                val groupTitle = options.firstOrNull { g -> g.options.contains(item) }?.title
                if (groupTitle != null) put(groupTitle, item)
            }
        }
        )
    }

    // Date range state — initialized from currentDateRange or first date group's values
    val dateGroup = options.firstOrNull { it.type == FilterType.DATE_RANGE }
    val initialPreset = currentDateRange?.preset
        ?: dateGroup?.selected?.value
        ?: "today"
    val initialDates = if (currentDateRange != null && currentDateRange.startDate.isNotBlank()) {
        currentDateRange.startDate to currentDateRange.endDate
    } else {
        resolveDatesFromPreset(initialPreset)
    }

    var selectedDatePreset by remember { mutableStateOf(initialPreset) }
    var startDate by remember { mutableStateOf(initialDates.first) }
    var endDate by remember { mutableStateOf(initialDates.second) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.box),
        ) {
            Text(
                text = "Filter",
                style = TextAppearance.title2Bold(),
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Spacing.tiny)
                    .size(22.dp)
                    .clickable {
                        onClose()
                    }
            )
        }

        HorizontalDivider()
        Spacer(Modifier.height(Spacing.box))

        options.forEach { group ->
            if (group.type == FilterType.DATE_RANGE) {
                // Date range group: preset chips + custom date pickers
                val presets = if (datePresets.isNotEmpty()) datePresets else defaultDatePresets()
                DateRangeFilterSection(
                    title = group.title,
                    presets = presets,
                    selectedPreset = selectedDatePreset,
                    startDate = startDate,
                    endDate = endDate,
                    showStartPicker = showStartPicker,
                    showEndPicker = showEndPicker,
                    onPresetSelected = { preset ->
                        selectedDatePreset = preset
                        val dates = resolveDatesFromPreset(preset)
                        startDate = dates.first
                        endDate = dates.second
                    },
                    onStartDateToggle = { showStartPicker = !showStartPicker },
                    onEndDateToggle = { showEndPicker = !showEndPicker },
                    onStartDateSelected = { date ->
                        startDate = date
                        selectedDatePreset = "custom"
                    },
                    onEndDateSelected = { date ->
                        endDate = date
                        selectedDatePreset = "custom"
                    },
                )
            } else {
                // Regular chip-based filter/sort group
                val selectedItem = selectedMap[group.title]
                SortOptionsFlowRow(
                    title = group.title,
                    items = group.options,
                    selected = selectedItem,
                    onSelect = { newItem ->
                        selectedMap = selectedMap.toMutableMap().apply {
                            this[group.title] = newItem
                        }
                    }
                )
            }
            Spacer(Modifier.height(Spacing.box))
        }

        Spacer(Modifier.height(Spacing.normal))

        Row {

            ButtonNormal(
                modifier = Modifier.weight(1f),
                text = "Reset",
                style = ButtonStyle.Outlined,
                strokeColor = Colors.Black
            ) {
                selectedMap = emptyMap()
                selectedDatePreset = "today"
                val todayDates = resolveDatesFromPreset("today")
                startDate = todayDates.first
                endDate = todayDates.second
                val selectedList = selectedMap.values.filterNotNull()
                onApply(selectedList)
                if (dateGroup != null) {
                    onDateRangeApply(DateRangeResult("today", todayDates.first, todayDates.second))
                }
                onClose()
            }

            Spacer(Modifier.width(Spacing.box))

            ButtonNormal(
                modifier = Modifier.weight(1f),
                text = "Terapkan",
                backgroundColor = Colors.Black
            ) {
                val selectedList = selectedMap.values.filterNotNull()
                onApply(selectedList)
                if (dateGroup != null) {
                    onDateRangeApply(DateRangeResult(selectedDatePreset, startDate, endDate))
                }
                onClose()
            }

        }
    }
}

// ── Date range filter section ───────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DateRangeFilterSection(
    title: String,
    presets: List<DatePresetOption>,
    selectedPreset: String,
    startDate: String,
    endDate: String,
    showStartPicker: Boolean,
    showEndPicker: Boolean,
    onPresetSelected: (String) -> Unit,
    onStartDateToggle: () -> Unit,
    onEndDateToggle: () -> Unit,
    onStartDateSelected: (String) -> Unit,
    onEndDateSelected: (String) -> Unit,
) {
    Text(text = title, style = TextAppearance.body2())
    Spacer(Modifier.height(Spacing.tiny))

    // Preset chips
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        maxItemsInEachRow = Int.MAX_VALUE,
    ) {
        presets.forEach { option ->
            val isSelected = selectedPreset == option.value
            AssistChip(
                modifier = Modifier.padding(end = Spacing.small),
                onClick = { onPresetSelected(option.value) },
                label = { Text(option.label, style = TextAppearance.body3()) },
                colors = if (isSelected) AssistChipDefaults.assistChipColors(
                    containerColor = Colors.ColorPrimary50,
                    labelColor = Colors.Gray1
                ) else AssistChipDefaults.assistChipColors(),
                border = if (isSelected) AssistChipDefaults.assistChipBorder(
                    true,
                    borderColor = Colors.ColorPrimary,
                    borderWidth = 0.5.dp
                ) else AssistChipDefaults.assistChipBorder(true)
            )
        }
    }

    Spacer(Modifier.height(Spacing.small))
    HorizontalDivider(thickness = 0.5.dp, color = Colors.Gray4)
    Spacer(Modifier.height(Spacing.small))

    // Custom date range pickers
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.box),
    ) {
        DatePickerField(
            value = startDate,
            label = "Tanggal Mulai",
            showPicker = showStartPicker,
            onToggle = onStartDateToggle,
            onDateSelected = onStartDateSelected,
            modifier = Modifier.weight(1f),
        )
        DatePickerField(
            value = endDate,
            label = "Tanggal Akhir",
            showPicker = showEndPicker,
            onToggle = onEndDateToggle,
            onDateSelected = onEndDateSelected,
            modifier = Modifier.weight(1f),
        )
    }
}

// ── Default date presets ────────────────────────────────────────────────────

fun defaultDatePresets(): List<DatePresetOption> = listOf(
    DatePresetOption("Hari ini", "today"),
    DatePresetOption("Kemarin", "yesterday"),
    DatePresetOption("7 Hari Terakhir", "last7"),
    DatePresetOption("30 Hari Terakhir", "last30"),
    DatePresetOption("Minggu Ini", "thisWeek"),
    DatePresetOption("Minggu Lalu", "lastWeek"),
    DatePresetOption("Bulan Ini", "thisMonth"),
    DatePresetOption("Bulan Lalu", "lastMonth"),
    DatePresetOption("Tahun Ini", "thisYear"),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SortOptionsFlowRow(
    title: String = "Urutkan",
    items: List<FilterItem> = listOf(),
    selected: FilterItem? = null,
    onSelect: (FilterItem?) -> Unit
) {

    Text(
        text = title,
        style = TextAppearance.body2(),
    )

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Spacing.tiny),
        horizontalArrangement = Arrangement.Start,
        maxItemsInEachRow = Int.MAX_VALUE // biar otomatis wrap
    ) {
        items.forEach { item ->
            val isSelected = selected == item
            val name = item.title
            AssistChip(
                modifier = Modifier.padding(end = Spacing.small),
                onClick = {
                    onSelect(if (isSelected) null else item)
                },
                label = { Text(name, style = TextAppearance.body3()) },
                colors = if (isSelected) AssistChipDefaults.assistChipColors(
                    containerColor = Colors.ColorPrimary50,
                    labelColor = Colors.Gray1
                ) else AssistChipDefaults.assistChipColors(),
                border = if (isSelected) AssistChipDefaults.assistChipBorder(
                    true,
                    borderColor = Colors.ColorPrimary,
                    borderWidth = 0.5.dp
                ) else AssistChipDefaults.assistChipBorder(true)
            )
        }
    }
}

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    count: Int = 0,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(Spacing.box),
        colors = CardDefaults.cardColors(containerColor = Colors.Gray5),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = {
                    onClick()
                })
                .padding(
                    horizontal = Spacing.box,
                    vertical = Spacing.small
                )
                .padding(vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 Badge jumlah filter aktif
            if (count > 0) {
                Card(
                    shape = RoundedCornerShape(Spacing.small),
                    colors = CardDefaults.cardColors(containerColor = Colors.Red),
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = count.toString(),
                            color = Color.White,
                            style = TextAppearance.body2(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Icon(
                    imageVector = MyIcon.IcFilterSolar,
                    contentDescription = "Filter",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                )
            }

            // 🔹 Label “Filter”
            Spacer(modifier = Modifier.width(Spacing.small))
            Text(
                text = "Filter",
                style = TextAppearance.body2()
            )
        }
    }
}

@Composable
fun RefreshButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .wrapContentSize(),
        shape = RoundedCornerShape(Spacing.box),
        colors = CardDefaults.cardColors(containerColor = Colors.Gray5),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(
                    horizontal = Spacing.box,
                    vertical = Spacing.small
                )
                .padding(vertical = 1.dp)
        ) {
            Icon(
                painter = rememberVectorPainter(Icons.Default.Refresh),
                contentDescription = "Filter",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FilterButtonPreview() {
    HelperTheme {
        FilterButton()
    }
}

@Preview(showBackground = true)
@Composable
fun FilterButtonActivePreview() {
    HelperTheme {
        FilterButton(
            count = 3
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GeneralFilterPreview() {
    HelperTheme {
        GeneralFilterBottomSheet(
            onClose = {}
        )
    }
}