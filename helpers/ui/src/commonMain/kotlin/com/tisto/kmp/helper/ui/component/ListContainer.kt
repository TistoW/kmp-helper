package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.utils.PlatformType
import com.tisto.kmp.helper.utils.model.FilterGroup
import com.tisto.kmp.helper.utils.model.FilterItem
import kotlinx.coroutines.launch
import kotlin.Unit

// ══════════════════════════════════════════════════════════════════════════════
// ListContainer — reusable list scaffold for KMP features.
//
// Pairs with ListUiState<T> + ListEvent<T>.
// Handles: Scaffold, Toolbar, search/filter, pull-to-refresh, pagination,
// infinite scroll, loading/error/empty states, delete confirmation.
//
// Caller provides only: columns, mobileRow, filterOptions, strings.
//
// Usage:
//   ListContainer(
//       state = state,
//       snackbar = snackbar,
//       title = "Diskon",
//       filterOptions = discountFilterOptions(),
//       onEvent = viewModel::onEvent,
//       itemKey = { it.id.def() },
//       columns = ::discountColumns,
//       mobileRow = { item, onClick -> ListMobileRow(...) },
//   )
// ══════════════════════════════════════════════════════════════════════════════

// ── Scroll-position holder ───────────────────────────────────────────────────
// Created in the Route (outside Crossfade) so the position survives
// Crossfade composition removal.  ListContainer reads/writes it automatically
// via LocalListScrollState.

class ListScrollState {
    var index by mutableIntStateOf(0)
    var offset by mutableIntStateOf(0)

    fun resetToTop() {
        index = 0
        offset = 0
    }
}

@Composable
fun rememberListScrollState() = remember { ListScrollState() }

val LocalListScrollState = staticCompositionLocalOf<ListScrollState?> { null }

// ── Generic list state ───────────────────────────────────────────────────────

sealed interface ListUiState<out T> {
    data object Loading : ListUiState<Nothing>
    data class Error(val message: String) : ListUiState<Nothing>
    data class Empty(
        val query: String = "",
        val filters: List<FilterItem> = emptyList(),
    ) : ListUiState<Nothing>

    data class Success<T>(
        val items: List<T>,
        val query: String = "",
        val filters: List<FilterItem> = emptyList(),
        val isRefreshing: Boolean = false,
        val page: Int = 1,
        val perPage: Int = 10,
        val totalItems: Int = 0,
        val lastPage: Int = 1,
        val isLoadingMore: Boolean = false,
    ) : ListUiState<T> {
        val hasMore: Boolean get() = page < lastPage
    }
}

// ── Generic list event ───────────────────────────────────────────────────────

sealed interface ListEvent<out T> {
    data object Refresh : ListEvent<Nothing>
    data class QueryChanged(val query: String) : ListEvent<Nothing>
    data class FiltersApplied(val filters: List<FilterItem>) : ListEvent<Nothing>
    data object CreateClicked : ListEvent<Nothing>
    data class EditClicked<T>(val item: T) : ListEvent<T>
    data class DeleteConfirmed<T>(val item: T) : ListEvent<T>
    data object LoadNextPage : ListEvent<Nothing>
    data class PageChanged(val page: Int) : ListEvent<Nothing>
    data class RowsPerPageChanged(val perPage: Int) : ListEvent<Nothing>
}

// ── ListContainer ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ListContainer(
    screenConfig: ScreenConfig = ScreenConfig(),
    modifier: Modifier = Modifier,
    state: ListUiState<T>,
    snackbar: SnackbarHostState,
    title: String,
    titlePicker: String = title,
    emptyText: String = "Data tidak ditemukan",
    searchHint: String = "Cari",
    filterOptions: List<FilterGroup> = defaultFilter(),
    datePresets: List<DatePresetOption> = emptyList(),
    currentDateRange: DateRangeResult? = null,
    onDateRangeApply: (DateRangeResult) -> Unit = {},
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    showAddButton: Boolean = true,
    overTakeOnAddClicked: (() -> Unit)? = null,
    onEvent: (ListEvent<T>) -> Unit = {},
    onBack: () -> Unit = {},
    onPick: ((T) -> Unit)? = null,
    deleteItemName: (T) -> String = { "" },
    itemKey: (T) -> Any,
    tabletRow: (isPicker: Boolean, onEdit: (T) -> Unit, onDelete: (T) -> Unit) -> List<ListColumn<T>>,
    additionalCompose: @Composable () -> Unit = {},
    mobileRow: @Composable (item: T, onClick: () -> Unit) -> Unit,
) {
    val isPicker = onPick != null
    var pendingDeleteItem by remember { mutableStateOf<T?>(null) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentFilters = when (state) {
        is ListUiState.Success -> state.filters
        is ListUiState.Empty -> state.filters
        else -> emptyList()
    }

    val resolvedColumns = remember(screenConfig.isTablet, isPicker) {
        tabletRow(
            isPicker,
            { item -> onEvent(ListEvent.EditClicked(item)) },
            { item -> pendingDeleteItem = item },
        )
    }

    Box(modifier = modifier) {
        Scaffold(
            topBar = {
                Toolbar(
                    screenConfig = screenConfig,
                    title = if (isPicker) titlePicker else title,
                    onAdd = if (showAddButton) {
                        {
                            if (overTakeOnAddClicked != null) overTakeOnAddClicked()
                            else onEvent(ListEvent.CreateClicked)
                        }
                    } else null,
                    onBack = if (screenConfig.isMobile) onBack else null,
                    backIcon = backIcon,
                )
            },
            containerColor = Color.White,
        ) { padding ->
            when (state) {
                ListUiState.Loading -> ListCenteredLoader(padding)
                is ListUiState.Error -> ListCenteredMessage(
                    padding,
                    state.message,
                    onRefresh = { onEvent(ListEvent.Refresh) })

                is ListUiState.Empty -> ListContainerEmpty(
                    filterOptions = filterOptions,
                    padding = padding,
                    query = state.query,
                    filterCount = currentFilters.size,
                    screenConfig = screenConfig,
                    emptyText = emptyText,
                    searchHint = searchHint,
                    onQueryChanged = { onEvent(ListEvent.QueryChanged(it)) },
                    onRefresh = { onEvent(ListEvent.Refresh) },
                    onOpenFilter = { showFilterSheet = true },
                )

                is ListUiState.Success -> ListContainerBody(
                    filterOptions = filterOptions,
                    padding = padding,
                    state = state,
                    screenConfig = screenConfig,
                    isPicker = isPicker,
                    columns = resolvedColumns,
                    itemKey = itemKey,
                    mobileRow = mobileRow,
                    additionalCompose = additionalCompose,
                    onEvent = onEvent,
                    onOpenFilter = { showFilterSheet = true },
                    onPick = onPick,
                )
            }
        }

        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    DeleteConfirmationDialog(
        showDialog = pendingDeleteItem != null,
        onDismiss = { pendingDeleteItem = null },
        onConfirm = {
            pendingDeleteItem?.let { onEvent(ListEvent.DeleteConfirmed(it)) }
            pendingDeleteItem = null
        },
        itemName = pendingDeleteItem?.let { deleteItemName(it) }.orEmpty(),
    )

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = Color.White,
        ) {
            GeneralFilterBottomSheet(
                options = filterOptions,
                preselected = currentFilters,
                datePresets = datePresets,
                currentDateRange = currentDateRange,
                onClose = { showFilterSheet = false },
                onApply = { selected ->
                    onEvent(ListEvent.FiltersApplied(selected))
                    showFilterSheet = false
                },
                onDateRangeApply = { dateRange ->
                    onDateRangeApply(dateRange)
                    showFilterSheet = false
                },
            )
        }
    }
}

// ── Success body ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> ListContainerBody(
    padding: PaddingValues,
    state: ListUiState.Success<T>,
    screenConfig: ScreenConfig,
    isPicker: Boolean,
    columns: List<ListColumn<T>>,
    itemKey: (T) -> Any,
    mobileRow: @Composable (item: T, onClick: () -> Unit) -> Unit,
    additionalCompose: @Composable () -> Unit = {},
    filterOptions: List<FilterGroup> = defaultFilter(),
    onEvent: (ListEvent<T>) -> Unit,
    onOpenFilter: () -> Unit,
    onPick: ((T) -> Unit)?,
) {
    val usePullToRefresh = !PlatformType.isWeb && !PlatformType.isJvm
    val useInfiniteScroll = usePullToRefresh
    val scrollState = LocalListScrollState.current
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollState?.index ?: 0,
        initialFirstVisibleItemScrollOffset = scrollState?.offset ?: 0,
    )
    val scope = rememberCoroutineScope()

    // Persist scroll position so it survives Crossfade recomposition
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                scrollState?.index = index
                scrollState?.offset = offset
            }
    }

    val contentHorizontalPadding =
        if (screenConfig.isMobile) Spacing.normal else Spacing.extraLarge

    val content: @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(Spacing.small))

            SearchFilterRow(
                modifier = Modifier.padding(horizontal = contentHorizontalPadding, Spacing.box),
                screenConfig = screenConfig,
                searchQuery = state.query,
                onSearchChange = { onEvent(ListEvent.QueryChanged(it)) },
                onClearSearch = { onEvent(ListEvent.QueryChanged("")) },
                refreshCount = state.filters.size,
                onRefresh = { onEvent(ListEvent.Refresh) },
                filterOptions = filterOptions,
                onOpenFilter = onOpenFilter,
            )

            additionalCompose()

            if (screenConfig.isTablet) {
                ListHeader(
                    columns = columns,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = contentHorizontalPadding)
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = contentHorizontalPadding)
            ) {
                items(state.items, key = itemKey) { item ->
                    val onClick = {
                        if (isPicker) onPick?.invoke(item)
                        else onEvent(ListEvent.EditClicked(item))
                    }
                    if (screenConfig.isMobile) {
                        mobileRow(item) { onClick() }
                    } else {
                        ListRow(item = item, columns = columns, onClick = { onClick() })
                    }
                }

                if (useInfiniteScroll && state.hasMore) {
                    item(key = "load_more") {
                        LaunchedEffect(state.page) {
                            onEvent(ListEvent.LoadNextPage)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.normal),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (PlatformType.isWeb || PlatformType.isJvm) {
                Box(modifier = Modifier.padding(horizontal = contentHorizontalPadding)) {
                    TablePaginationFooter(
                        rowsPerPage = state.perPage,
                        totalItems = state.totalItems,
                        currentPage = state.page,
                        onNextPage = {
                            onEvent(ListEvent.PageChanged(state.page + 1))
                            scope.launch { listState.scrollToItem(0) }
                        },
                        onPrevPage = {
                            onEvent(ListEvent.PageChanged(state.page - 1))
                            scope.launch { listState.scrollToItem(0) }
                        },
                        onRowsPerPageChange = {
                            onEvent(ListEvent.RowsPerPageChanged(it))
                            scope.launch { listState.scrollToItem(0) }
                        },
                    )
                }
            }
        }
    }

    if (usePullToRefresh) {
        val pullState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(ListEvent.Refresh) },
            state = pullState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullState,
                    isRefreshing = state.isRefreshing,
                    containerColor = Color.White,
                    color = Color.Black,
                )
            },
        ) {
            content()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            content()
        }
    }
}

// ── Empty body ───────────────────────────────────────────────────────────────

@Composable
private fun ListContainerEmpty(
    filterOptions: List<FilterGroup> = defaultFilter(),
    padding: PaddingValues,
    query: String,
    filterCount: Int,
    screenConfig: ScreenConfig,
    emptyText: String,
    searchHint: String,
    onQueryChanged: (String) -> Unit,
    onRefresh: () -> Unit,
    onOpenFilter: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Spacer(modifier = Modifier.height(Spacing.small))

        SearchFilterRow(
            modifier = Modifier.padding(horizontal = Spacing.normal, Spacing.box),
            screenConfig = screenConfig,
            searchQuery = query,
            hint = searchHint,
            onSearchChange = onQueryChanged,
            onClearSearch = { onQueryChanged("") },
            refreshCount = filterCount,
            onRefresh = onRefresh,
            onOpenFilter = onOpenFilter,
            filterOptions = filterOptions
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = emptyText, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(Spacing.small))
                ButtonNormal(
                    text = "Reload", onClick = onRefresh,
                    style = ButtonStyle.Outlined,
                    strokeColor = Color.Black,
                    contentPadding = PaddingValues(horizontal = Spacing.normal),
                    imageVector = Icons.Default.Refresh
                )
            }

        }
    }
}

// ── Utility composables ──────────────────────────────────────────────────────

@Composable
private fun ListCenteredLoader(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center,
    ) { CircularProgressIndicator() }
}

@Composable
private fun ListCenteredMessage(
    padding: PaddingValues, message: String,
    onRefresh: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = message, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(Spacing.small))
            ButtonNormal(
                text = "Reload", onClick = onRefresh,
                style = ButtonStyle.Outlined,
                strokeColor = Color.Black,
                contentPadding = PaddingValues(horizontal = Spacing.normal),
                imageVector = Icons.Default.Refresh
            )
        }

    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

private data class PreviewProduct(
    val id: Int,
    val name: String,
    val price: String,
    val stock: String
)

private val previewProductItems = listOf(
    PreviewProduct(1, "Nasi Goreng Spesial", "Rp 25.000", "10"),
    PreviewProduct(2, "Es Teh Manis", "Rp 5.000", "50"),
    PreviewProduct(3, "Ayam Bakar", "Rp 35.000", "8"),
    PreviewProduct(4, "Mie Goreng", "Rp 20.000", "15"),
)

private fun previewTabletColumns(
    isPicker: Boolean,
    onEdit: (PreviewProduct) -> Unit,
    onDelete: (PreviewProduct) -> Unit,
): List<ListColumn<PreviewProduct>> = listOf(
    ListColumn("name", "Nama Produk", weight = 2f) { Text(it.name) },
    ListColumn("price", "Harga", weight = 1f) { Text(it.price) },
    ListColumn("stock", "Stok", weight = 1f) { Text(it.stock) },
    ListColumn("actions", "Aksi", weight = 0.8f) {
        if (!isPicker) ListActions(onEdit = { onEdit(it) }, onDelete = { onDelete(it) })
    },
)

@Preview(showBackground = true, widthDp = 360, name = "ListContainer - Loading (Mobile)")
@Composable
private fun ListContainerLoadingPreview() {
    HelperTheme {
        ListContainer(
            screenConfig = ScreenConfig(maxWidth = 360.dp),
            state = ListUiState.Loading,
            snackbar = remember { SnackbarHostState() },
            title = "Produk",
            itemKey = { it.id },
            tabletRow = ::previewTabletColumns,
            mobileRow = { item, onClick ->
                ListMobileRow(text = item.name, secondary = item.price, onClick = onClick)
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 360, name = "ListContainer - Error (Mobile)")
@Composable
private fun ListContainerErrorPreview() {
    HelperTheme {
        ListContainer<PreviewProduct>(
            screenConfig = ScreenConfig(maxWidth = 360.dp),
            state = ListUiState.Error("Gagal memuat data. Periksa koneksi internet Anda."),
            snackbar = remember { SnackbarHostState() },
            title = "Produk",
            itemKey = { it.id },
            tabletRow = ::previewTabletColumns,
            mobileRow = { item, onClick ->
                ListMobileRow(text = item.name, secondary = item.price, onClick = onClick)
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 360, name = "ListContainer - Empty (Mobile)")
@Composable
private fun ListContainerEmptyPreview() {
    HelperTheme {
        ListContainer<PreviewProduct>(
            screenConfig = ScreenConfig(maxWidth = 360.dp),
            state = ListUiState.Empty(query = "nasi"),
            snackbar = remember { SnackbarHostState() },
            title = "Produk",
            itemKey = { it.id },
            tabletRow = ::previewTabletColumns,
            mobileRow = { item, onClick ->
                ListMobileRow(text = item.name, secondary = item.price, onClick = onClick)
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 360, name = "ListContainer - Success (Mobile)")
@Composable
private fun ListContainerSuccessMobilePreview() {
    HelperTheme {
        ListContainer<PreviewProduct>(
            screenConfig = ScreenConfig(maxWidth = 360.dp),
            state = ListUiState.Success(
                items = previewProductItems,
                query = "",
                totalItems = previewProductItems.size,
            ),
            snackbar = remember { SnackbarHostState() },
            title = "Produk",
            itemKey = { it.id },
            tabletRow = ::previewTabletColumns,
            mobileRow = { item, onClick ->
                ListMobileRow(
                    text = item.name,
                    secondary = "${item.price} • Stok: ${item.stock}",
                    onClick = onClick
                )
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 800, name = "ListContainer - Success (Tablet)")
@Composable
private fun ListContainerSuccessTabletPreview() {
    HelperTheme {
        ListContainer<PreviewProduct>(
            screenConfig = ScreenConfig(maxWidth = 800.dp),
            state = ListUiState.Success(
                items = previewProductItems,
                query = "",
                totalItems = previewProductItems.size,
                page = 1,
                lastPage = 3,
            ),
            snackbar = remember { SnackbarHostState() },
            title = "Produk",
            itemKey = { it.id },
            tabletRow = ::previewTabletColumns,
            mobileRow = { item, onClick ->
                ListMobileRow(text = item.name, secondary = item.price, onClick = onClick)
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 360, name = "ListContainer - Picker Mode")
@Composable
private fun ListContainerPickerPreview() {
    HelperTheme {
        ListContainer<PreviewProduct>(
            screenConfig = ScreenConfig(maxWidth = 360.dp),
            state = ListUiState.Success(
                items = previewProductItems,
                totalItems = previewProductItems.size,
            ),
            snackbar = remember { SnackbarHostState() },
            title = "Produk",
            titlePicker = "Pilih Produk",
            showAddButton = false,
            itemKey = { it.id },
            tabletRow = ::previewTabletColumns,
            mobileRow = { item, onClick ->
                ListMobileRow(text = item.name, secondary = item.price, onClick = onClick)
            },
            onPick = {},
        )
    }
}
