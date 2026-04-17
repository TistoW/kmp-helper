package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.network.base.BaseUiState
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.utils.ext.reformatDate
import com.tisto.kmp.helper.utils.model.FilterGroup
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <STATE, ITEMS> ListScaffold(
    modifier: Modifier = Modifier,
    title: String = "",
    screenConfig: ScreenConfig = ScreenConfig(),
    uiState: BaseUiState<STATE>,
    items: List<ITEMS> = listOf(),
    horizontalPadding: Float? = null,
    contentModifier: Modifier = Modifier
        .fillMaxWidth(screenConfig.getHorizontalPaddingListWeight(horizontalPadding))   // ✅ 80% width
        .padding(horizontal = if (screenConfig.isMobile) Spacing.normal else 0.dp)
        .padding(top = if (screenConfig.isMobile) Spacing.normal else Spacing.huge),
    onUpdateUiState: (BaseUiState<STATE>.() -> BaseUiState<STATE>) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onBack: (() -> Unit)? = null,
    onAddClick: (() -> Unit)? = null,

    saveText: String = "Simpan",
    emptyTitle: String = "Data Kosong",
    emptySubtitle: String = "Belum ada data tersedia",
    onSave: (() -> Unit)? = null,

    filterOptions: List<FilterGroup> = emptyList(),
    showToolbar: Boolean = true,
    showSearch: Boolean = true,

    content: LazyListScope.() -> Unit
) {

    val listState = uiState.listScrollState
    val isLoading = uiState.isLoading
    val isRefreshing = uiState.isRefreshing

    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    /* =============================
     * SIDE EFFECTS
     * ============================= */

    // scroll to top setelah refresh selesai
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing && listState.firstVisibleItemIndex > 0) {
            listState.animateScrollToItem(0)
        }
    }

    // load more ketika scroll mentok
    LaunchedEffect(listState.canScrollForward, listState.isScrollInProgress) {
        if (
            !listState.canScrollForward &&
            !listState.isScrollInProgress &&
            uiState.hasMore &&
            !isLoading
        ) {
            onLoadMore()
        }
    }

    /* =============================
     * UI
     * ============================= */

    Column(
        modifier = modifier.background(Colors.White)
    ) {

        if (showToolbar) {
            Toolbars(
                title = title,
                onBack = onBack,
                onSave = if (!screenConfig.isMobileOnly) onSave else null,
                saveText = saveText
            )
        }

        RefreshContainer(
            isRefreshing = isRefreshing || (isLoading && uiState.isSearching),
            onRefresh = onRefresh,
            modifier = contentModifier.fillMaxSize()
        ) {

            Column(
                modifier = contentModifier.fillMaxSize()
            ) {

                /* =============================
                 * SEARCH + FILTER
                 * ============================= */

                if (showSearch || filterOptions.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spacing.box),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (showSearch) {
                            CustomTextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                    onSearch(it)
                                },
                                label = "Cari...",
                                style = TextFieldStyle.OUTLINED,
                                strokeWidth = 0.5.dp,
                                strokeColor = Colors.Gray3,
                                floatingLabel = false,
                                cornerRadius = Radius.normal,
                                leadingIcon = Icons.Default.Search,
                                endIcon = if (searchQuery.isNotEmpty()) Icons.Default.Close
                                else null,
                                endIconOnClick = {
                                    searchQuery = ""
                                    onSearch("")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (filterOptions.isNotEmpty()) {
                            FilterButton(
                                count = uiState.filters.size,
                                onClick = { showFilterSheet = true }
                            )
                        }
                    }
                }

                /* =============================
                 * LIST + STATE
                 * ============================= */

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                ) {

                    LazyColumn(state = listState) {
                        content()


                        if (list.size < 10) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .height(500.dp)
                                        .fillMaxWidth()
                                        .background(Colors.Gray5)
                                )
                            }
                        }
                    }

                    // loading indicator
                    if (isLoading && !isRefreshing && !uiState.isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(
                                    if (uiState.isEmpty)
                                        Alignment.Center
                                    else Alignment.BottomCenter
                                )
                                .padding(16.dp),
                            color = Colors.ColorPrimary
                        )
                    }

                    // empty state
                    if (items.isEmpty() && !isLoading) {
                        EmptyState(
                            title = emptyTitle,
                            subtitle = emptySubtitle,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // FAB add
                    onAddClick?.let {
                        FloatingActionButton(
                            onClick = it,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(Spacing.normal),
                            containerColor = Colors.ColorPrimary
                        ) {
                            Icon(Icons.Default.Add, null, tint = Colors.White)
                        }
                    }

                    // filter bottom sheet
                    if (showFilterSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showFilterSheet = false },
                            sheetState = sheetState,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        ) {
                            GeneralFilterBottomSheet(
                                options = filterOptions,
                                preselected = uiState.filters,
                                onClose = { showFilterSheet = false },
                                onApply = { selected ->
                                    onUpdateUiState { copy(filters = selected) }
                                    showFilterSheet = false
                                    onRefresh()
                                }
                            )
                        }
                    }
                }

                if (onSave != null && screenConfig.isMobileOnly) {
                    ButtonNormal(
                        text = saveText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spacing.box, top = Spacing.small),
                        onClick = onSave
                    )
                }

            }
        }
    }
}


@Serializable
data class ExampleModel(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val daerah: String = "",
    val createdAt: String = "",
)


private val list = List(10) {
    ExampleModel(
        id = it.toString(),
        name = "Desa $it",
        code = "CODE-${it}${it - 1}",
        daerah = "Kabupaten $it",
        createdAt = "12 Des 2025"
    )
}

@Composable
private fun ListItem(
    item: ExampleModel,
    onClick: () -> Unit = {},
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(Spacing.box)
        ) {

            Text(
                text = item.name,
                style = TextAppearance.body1Bold(),
                color = Colors.Dark,
                maxLines = 1,                     // ✅ limit to one line
                overflow = TextOverflow.Ellipsis, // ✅ show "..."
                modifier = Modifier
            )

            Spacer(Modifier.height(Spacing.tiny))

            Text(
                text = item.createdAt.reformatDate(),
                style = TextAppearance.body2(),
                color = Colors.Gray2,
                modifier = Modifier
            )
        }

        HorizontalDivider(thickness = 0.5.dp)
    }

}

@Composable
fun ScreenContentPreview(
    screenConfig: ScreenConfig = ScreenConfig(),
) {
    ListScaffold(
        uiState = BaseUiState(
            data = ExampleModel()
        ),
        items = listOf<ExampleModel>(),
        screenConfig = screenConfig
    ) {
        items(list, key = { it.id }) { item ->
            ListItem(item = item)
        }
    }
}

@MobilePreview
@Composable
fun MobilePreview() {
    ScreenContentPreview()
}

@TabletPreview
@Composable
fun TabletPreview() {
    ScreenContentPreview(ScreenConfig(700.dp))
}

