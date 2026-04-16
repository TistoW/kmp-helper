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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import kotlin.apply
import kotlin.collections.filterNotNull
import kotlin.collections.firstOrNull
import kotlin.collections.toMutableMap

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

@Composable
fun GeneralFilterBottomSheet(
    onClose: () -> Unit,
    options: List<FilterGroup> = defaultFilter(),
    preselected: List<FilterItem> = listOf(), // ⬅️ kirim selected dari parent
    onApply: (List<FilterItem>) -> Unit = {} // hasil apply dikirim keluar
) {

    // State semua pilihan: Map<GroupTitle, SelectedOption?>
    // Gunakan map: GroupTitle -> SelectedFilterItem?
    var selectedMap by remember {
        mutableStateOf(buildMap {
            options.forEach { group ->
                if (group.selected != null) {
                    put(group.title, group.selected)
                }
            }
            // override kalau ada preselected dari parent
            preselected.forEach { item ->
                val groupTitle = options.firstOrNull { g -> g.options.contains(item) }?.title
                if (groupTitle != null) put(groupTitle, item)
            }
        }
        )
    }

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
        // 🔹 Loop semua grup filter
        options.forEach { group ->
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
                val selectedList = selectedMap.values.filterNotNull()
                onApply(selectedList) // kirim hasil ke parent
                onClose()
            }

            Spacer(Modifier.width(Spacing.box))

            ButtonNormal(
                modifier = Modifier.weight(1f),
                text = "Terapkan",
                backgroundColor = Colors.Black
            ) {
                val selectedList = selectedMap.values.filterNotNull()
                onApply(selectedList) // kirim hasil ke parent
                onClose()
            }

        }
    }
}

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
                    colors = CardDefaults.cardColors(containerColor = Colors.ColorPrimary),
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