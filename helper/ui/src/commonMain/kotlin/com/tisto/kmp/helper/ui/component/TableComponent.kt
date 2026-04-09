package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import kotlin.math.min

data class TableColumn<T>(
    val key: String,
    val title: String,
    val weight: Float = 1f,
    val header: (@Composable () -> Unit)? = null,
    val cell: @Composable (T) -> Unit
)

data class TableSpec<T>(
    val columns: List<TableColumn<T>>,
    val actionsWidth: Dp = 80.dp, // your "action" area width
    val actions: (@Composable RowScope.(T) -> Unit)? = null,
)

fun <T> tableActions(
    onEdit: ((T) -> Unit)? = null,
    onDelete: ((T) -> Unit)? = null,
    onMore: ((T) -> Unit)? = null,
    onOptionsClicked: (T, String) -> Unit = { _, _ -> },
    options: List<String> = listOf()
): @Composable RowScope.(T) -> Unit = { item ->

    onEdit?.let {
        IconButton(onClick = { it(item) }) {
            Icon(Icons.Default.Edit, "Edit", tint = Color.Gray)
        }
    }

    onDelete?.let {
        IconButton(onClick = { it(item) }) {
            Icon(Icons.Default.Delete, "Delete", tint = Color.Gray)
        }
    }

    onMore?.let {
        Box {
            var expanded by remember { mutableStateOf(false) }

            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.Gray
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Colors.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(),
                        text = { Text(option) },
                        onClick = {
                            expanded = false
                            onOptionsClicked(item, option)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun <T> TableHeader(
    spec: TableSpec<T>,
    modifier: Modifier = Modifier,
    showActions: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Colors.Gray5)
            .padding(vertical = Spacing.normal, horizontal = Spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        spec.columns.forEachIndexed { index, col ->
            Row(
                modifier = Modifier.weight(col.weight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (col.header != null) col.header.invoke()
                    else Text(
                        text = col.title,
                        style = TextAppearance.body1Bold(),
                        color = Color.Gray
                    )
                }

                if (showActions) {
                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                            .padding(horizontal = Spacing.small),
                        thickness = 1.dp,
                        color = Colors.Gray4
                    )
                }
            }
        }

        if (showActions) {
            Spacer(Modifier.width(spec.actionsWidth))
        }
    }
}

@Composable
fun <T> TableRow(
    contentPaddingVertical: Dp = Spacing.small,
    contentPaddingHorizontal: Dp = Spacing.normal,
    modifier: Modifier = Modifier.padding(
        horizontal = contentPaddingHorizontal,
        vertical = contentPaddingVertical
    ),
    item: T,
    spec: TableSpec<T>,
    showActions: Boolean = true,
    onClick: (() -> Unit)? = null,
    actions: (@Composable RowScope.(T) -> Unit)? = null
) {
    Column(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        ).fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            spec.columns.forEach { col ->
                Row(modifier = Modifier.weight(col.weight)) {
                    col.cell(item)
                    Spacer(modifier = Modifier.width(Spacing.small))
                }
            }

            // Actions column at the end (same width as header spacer)
            if (showActions) {
                if (actions != null) {
                    Box(modifier = Modifier.width(spec.actionsWidth)) {
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            actions(item)
                        }
                    }
                } else {
                    Spacer(Modifier.width(spec.actionsWidth))
                }
            }
        }

        HorizontalDivider(thickness = 0.4.dp, color = Colors.Gray4)
    }
}


@Composable
fun TablePaginationFooter(
    rowsPerPage: Int,
    totalItems: Int,
    currentPage: Int,
    onRowsPerPageChange: (Int) -> Unit = {},
    onPrevPage: () -> Unit = {},
    onNextPage: () -> Unit = {},
) {
    val page = (currentPage - 1)
    val start = if (totalItems == 0) 0 else (page * rowsPerPage) + 1
    val end = if (totalItems == 0) 0 else min((page + 1) * rowsPerPage, totalItems)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.Gray5)
            .padding(horizontal = Spacing.medium),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Rows per page
        Text(
            text = "Rows per page:",
            style = TextAppearance.body1(),
            color = Color.Gray
        )

        Spacer(Modifier.width(8.dp))

        RowsPerPageDropdown(
            value = rowsPerPage,
            onValueChange = onRowsPerPageChange
        )

        Spacer(Modifier.width(24.dp))

        // Range text
        Text(
            text = "$start–$end of $totalItems",
            style = TextAppearance.body1(),
            color = Color.Gray
        )

        Spacer(Modifier.width(16.dp))

        // Prev
        IconButton(
            onClick = onPrevPage,
            enabled = page > 0
        ) {
            Icon(
                Icons.Default.ChevronLeft,
                contentDescription = "Previous",
                tint = if (page > 0) Color.DarkGray else Color.LightGray
            )
        }

        // Next
        IconButton(
            onClick = onNextPage,
            enabled = end < totalItems
        ) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Next",
                tint = if (end < totalItems) Color.DarkGray else Color.LightGray
            )
        }
    }
}

@Composable
fun RowsPerPageDropdown(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(5, 10, 20, 50)

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable { expanded = true }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.toString(),
                style = TextAppearance.body1(),
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Colors.White)
        ) {
            options.forEach {
                DropdownMenuItem(
                    colors = MenuDefaults.itemColors(),
                    text = { Text(it.toString()) },
                    onClick = {
                        expanded = false
                        onValueChange(it)
                    }
                )
            }
        }
    }
}