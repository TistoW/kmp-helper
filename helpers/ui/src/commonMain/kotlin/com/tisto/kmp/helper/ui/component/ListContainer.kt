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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.utils.PlatformType
import com.tisto.kmp.helper.utils.model.FilterGroup
import com.tisto.kmp.helper.utils.model.FilterItem
import kotlinx.coroutines.launch

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
    filterOptions: List<FilterGroup> = emptyList(),
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    showAddButton: Boolean = true,
    onEvent: (ListEvent<T>) -> Unit = {},
    onBack: () -> Unit = {},
    onPick: ((T) -> Unit)? = null,
    deleteItemName: (T) -> String = { "" },
    itemKey: (T) -> Any,
    columns: (isPicker: Boolean, onEdit: (T) -> Unit, onDelete: (T) -> Unit) -> List<ListColumn<T>>,
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
        columns(
            isPicker,
            { item -> onEvent(ListEvent.EditClicked(item)) },
            { item -> pendingDeleteItem = item },
        )
    }

    Scaffold(
        topBar = {
            Toolbar(
                screenConfig = screenConfig,
                title = if (isPicker) titlePicker else title,
                onAdd = if (showAddButton) {
                    { onEvent(ListEvent.CreateClicked) }
                } else null,
                onBack = if (screenConfig.isMobile) onBack else null,
                backIcon = backIcon,
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = Color.White,
        modifier = modifier
    ) { padding ->
        when (state) {
            ListUiState.Loading -> ListCenteredLoader(padding)
            is ListUiState.Error -> ListCenteredMessage(padding, state.message)
            is ListUiState.Empty -> ListContainerEmpty(
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
                padding = padding,
                state = state,
                screenConfig = screenConfig,
                isPicker = isPicker,
                columns = resolvedColumns,
                itemKey = itemKey,
                mobileRow = mobileRow,
                onEvent = onEvent,
                onOpenFilter = { showFilterSheet = true },
                onPick = onPick,
            )
        }
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
        ) {
            GeneralFilterBottomSheet(
                options = filterOptions,
                preselected = currentFilters,
                onClose = { showFilterSheet = false },
                onApply = { selected ->
                    onEvent(ListEvent.FiltersApplied(selected))
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
    onEvent: (ListEvent<T>) -> Unit,
    onOpenFilter: () -> Unit,
    onPick: ((T) -> Unit)?,
) {
    val usePullToRefresh = !PlatformType.isWeb && !PlatformType.isJvm
    val useInfiniteScroll = usePullToRefresh
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val wasRefreshing = remember { mutableStateOf(false) }
    LaunchedEffect(state.isRefreshing) {
        if (wasRefreshing.value && !state.isRefreshing) {
            listState.animateScrollToItem(0)
        }
        wasRefreshing.value = state.isRefreshing
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
                onOpenFilter = onOpenFilter,
            )

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
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(ListEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
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
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(emptyText)
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
private fun ListCenteredMessage(padding: PaddingValues, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center,
    ) { Text(message) }
}
