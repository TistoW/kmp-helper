package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.utils.PlatformType
import com.tisto.kmp.helper.utils.model.FilterGroup

// ══════════════════════════════════════════════════════════════════════════════
// ListComponent — reusable list/table pattern for KMP features.
//
// Usage:
//   1. Define columns:  val cols = buildList<ListColumn<MyItem>> { add(...) }
//   2. Tablet header:   if (isTablet) ListHeader(cols)
//   3. Tablet rows:     ListRow(item, cols, onClick = { ... })
//   4. Mobile rows:     ListMobileRow(imageUrl, text, onClick = { ... })
//   5. Actions column:  ListActions(onEdit = { ... }, onDelete = { ... })
//
// Does NOT replace the old TableComponent.kt — that stays for backward compat.
// ══════════════════════════════════════════════════════════════════════════════

// ── Column definition ────────────────────────────────────────────────────────

data class ListColumn<T>(
    val key: String,
    val title: String,
    val weight: Float = 1f,
    val contentArrangement: Arrangement.Horizontal = Arrangement.Start,
    val header: (@Composable () -> Unit)? = null,
    val cell: @Composable (T) -> Unit,
)

// ── Header (tablet only) ─────────────────────────────────────────────────────

@Composable
fun <T> ListHeader(
    columns: List<ListColumn<T>>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Colors.Gray5)
            .padding(vertical = Spacing.normal, horizontal = Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        columns.forEachIndexed { index, col ->
            Row(
                modifier = Modifier.weight(col.weight),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (col.header != null) col.header.invoke()
                    else Text(
                        text = col.title,
                        style = TextAppearance.body1Bold(),
                        color = Color.Gray,
                    )
                }
                if (index != columns.lastIndex) {
                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                            .padding(horizontal = Spacing.small),
                        thickness = 1.dp,
                        color = Colors.Gray4,
                    )
                }
            }
        }
    }
}

// ── Row (tablet) ─────────────────────────────────────────────────────────────

@Composable
fun <T> ListRow(
    item: T,
    columns: List<ListColumn<T>>,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.normal, vertical = Spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            columns.forEach { col ->
                Row(
                    modifier = Modifier.weight(col.weight),
                    horizontalArrangement = col.contentArrangement,
                ) {
                    col.cell(item)
                    Spacer(modifier = Modifier.width(Spacing.small))
                }
            }
        }
        HorizontalDivider(thickness = 0.4.dp, color = Colors.Gray4)
    }
}

// ── Mobile row ───────────────────────────────────────────────────────────────
// Simple row for mobile: optional image → text + optional secondary.
// For custom mobile layouts, write your own composable instead.

@Composable
fun ListMobileRow(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    imageSize: Dp = 50.dp,
    text: String,
    secondary: String? = null,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.box)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (imageUrl != null) {
                CustomImageView(imageUrl = imageUrl, size = imageSize)
                Spacer(Modifier.width(Spacing.normal))
            }
            RowText(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                secondary = secondary,
            )
        }
        HorizontalDivider(thickness = 0.5.dp)
    }
}

// ── Action buttons ───────────────────────────────────────────────────────────
// Drop into an action column: ListColumn("action", "", weight = 0.3f) { item ->
//     ListActions(onEdit = { ... }, onDelete = { ... })
// }

@Composable
fun ListActions(
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onMore: (() -> Unit)? = null,
    options: List<String> = listOf(),
    onOptionsClicked: (String) -> Unit = {},
) {
    Row {
        onEdit?.let {
            IconButton(onClick = it) {
                Icon(Icons.Default.Edit, "Edit", tint = Color.Gray)
            }
        }
        onDelete?.let {
            IconButton(onClick = it) {
                Icon(Icons.Default.Delete, "Delete", tint = Color.Gray)
            }
        }
        onMore?.let {
            Box {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, "More", tint = Color.Gray)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Colors.White),
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            colors = MenuDefaults.itemColors(),
                            text = { Text(option) },
                            onClick = { expanded = false; onOptionsClicked(option) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchFilterRow(
    modifier: Modifier = Modifier,
    screenConfig: ScreenConfig,
    showSearch: Boolean = true,
    filterOptions: List<FilterGroup> = defaultFilter(),
    searchQuery: String = "",
    hint: String = "Cari",
    onSearchChange: (String) -> Unit = {},
    onClearSearch: () -> Unit = {},
    refreshCount: Int = 0,
    onRefresh: () -> Unit = {},
    onOpenFilter: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.box),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showSearch) {
            SearchTextField(
                modifier = Modifier.then(
                    if (screenConfig.isMobile) Modifier.weight(1f)
                    else Modifier.width(300.dp)
                ),
                value = searchQuery,
                onValueChange = onSearchChange,
                onClear = onClearSearch,
                label = hint
            )
        }

        if (!screenConfig.isMobile) {
            Spacer(Modifier.weight(1f))
        }

        if (filterOptions.isNotEmpty()) {
            if (PlatformType.isWeb) {
                RefreshButton(onClick = onRefresh)
            }
            FilterButton(count = refreshCount, onClick = onOpenFilter)
        }
    }
}
