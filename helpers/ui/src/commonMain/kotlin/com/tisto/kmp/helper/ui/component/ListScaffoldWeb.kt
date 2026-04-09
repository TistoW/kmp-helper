package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.network.base.BaseUiState
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.icon.myicon.IcSearch
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Heights
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.ext.isMobilePhone
import com.tisto.kmp.helper.utils.model.FilterGroup

// ====================================
// 1. Add to BaseUiState
// ====================================

// Add this import:
// import androidx.compose.foundation.lazy.grid.LazyGridState

// Add this field in BaseUiState:
// val gridScrollState: LazyGridState = LazyGridState(),

// Full updated BaseUiState:


// ====================================
// 2. DisplayMode & GridColumns
// ====================================

enum class DisplayMode {
    List,
    Grid
}

sealed class GridColumns {
    /** Fixed number of columns, e.g. GridColumns.Fixed(2) */
    data class Fixed(val count: Int) : GridColumns()

    /** Adaptive columns based on minimum item width, e.g. GridColumns.Adaptive(150.dp) */
    data class Adaptive(val minWidth: Dp) : GridColumns()

    /** Convert to Compose GridCells */
    fun toGridCells(): GridCells = when (this) {
        is Fixed -> GridCells.Fixed(count)
        is Adaptive -> GridCells.Adaptive(minWidth)
    }
}

// ====================================
// 3. ListGridScaffoldWeb
// ====================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <STATE, ITEMS> ListGridScaffoldWeb(
    modifier: Modifier = Modifier,
    title: String = "Title",
    screenConfig: ScreenConfig = ScreenConfig(),
    uiState: BaseUiState<STATE>,
    items: List<ITEMS> = emptyList(),
    horizontalPadding: Float? = null,

    contentModifier: Modifier = Modifier
        .fillMaxWidth(screenConfig.getHorizontalPaddingListWeight(horizontalPadding))
        .then(
            if (screenConfig.isMobile)
                Modifier.padding(horizontal = Spacing.normal)
            else Modifier
        ),

    // ✅ Display mode: List or Grid
    displayMode: DisplayMode = DisplayMode.List,
    gridColumns: GridColumns = GridColumns.Fixed(2),

    // Callbacks
    onUpdateUiState: (BaseUiState<STATE>.() -> BaseUiState<STATE>) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
    onRowsPerPageChange: (Int) -> Unit = {},
    onPrevPage: () -> Unit = {},
    onNextPage: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onAdd: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,

    // UI options
    filterOptions: List<FilterGroup> = emptyList(),
    showToolbar: Boolean = true,
    showSearch: Boolean = true,
    showPaginationButton: Boolean = true,
    header: (@Composable () -> Unit)? = null,

    // ✅ Two separate content lambdas
    listContent: (LazyListScope.() -> Unit)? = null,
    gridContent: (LazyGridScope.() -> Unit)? = null,

    // Min height
    minListHeight: Dp = 350.dp,
    estimatedRowHeight: Dp = 56.dp,
) {
    val isLoading = uiState.isLoading
    val isRefreshing = uiState.isRefreshing

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val isMobile = screenConfig.isMobile || isMobilePhone()

    // ✅ Filler height logic
    val contentHeight = estimatedRowHeight * items.size
    val fillerHeight = (minListHeight - contentHeight).coerceAtLeast(0.dp)

    // ✅ Scroll states based on mode
    val listState = uiState.listScrollState
    val gridState = uiState.gridScrollState

    // ✅ Scroll to top after refresh
    LaunchedEffect(isRefreshing, displayMode) {
        if (!isRefreshing) {
            when (displayMode) {
                DisplayMode.List -> {
                    if (listState.firstVisibleItemIndex > 0) {
                        listState.animateScrollToItem(0)
                    }
                }

                DisplayMode.Grid -> {
                    if (gridState.firstVisibleItemIndex > 0) {
                        gridState.animateScrollToItem(0)
                    }
                }
            }
        }
    }

    // ✅ Load more when scrolled to bottom
    when (displayMode) {
        DisplayMode.List -> {
            LaunchedEffect(listState.canScrollForward, listState.isScrollInProgress) {
                if (!listState.canScrollForward && !listState.isScrollInProgress
                    && uiState.hasMore && !isLoading && !showPaginationButton
                ) {
                    onLoadMore()
                }
            }
        }

        DisplayMode.Grid -> {
            LaunchedEffect(gridState.canScrollForward, gridState.isScrollInProgress) {
                if (!gridState.canScrollForward && !gridState.isScrollInProgress
                    && uiState.hasMore && !isLoading && !showPaginationButton
                ) {
                    onLoadMore()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .background(Colors.White)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // =============================
            // TOOLBAR
            // =============================
            if (showToolbar) {
                ToolbarRow(
                    screenConfig = screenConfig,
                    backIcon = backIcon,
                    title = title,
                    onAdd = onAdd,
                    onBack = onBack
                )
            }

            RefreshContainer(
                isRefreshing = isRefreshing || (isLoading && uiState.isSearching),
                onRefresh = onRefresh,
                modifier = contentModifier
            ) {
                when (displayMode) {
                    // =============================
                    // LIST MODE
                    // =============================
                    DisplayMode.List -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            // Spacer top
                            item {
                                Spacer(
                                    modifier = Modifier.height(
                                        if (screenConfig.isMobile) Spacing.normal else Spacing.extraLarge
                                    )
                                )
                            }

                            // Search + Filter
                            searchFilterItem(
                                isMobile = isMobile,
                                showSearch = showSearch,
                                filterOptions = filterOptions,
                                searchQuery = searchQuery,
                                uiState = uiState,
                                onSearchQueryChange = {
                                    searchQuery = it
                                    onSearch(it)
                                },
                                onClearSearch = {
                                    searchQuery = ""
                                    onSearch("")
                                },
                                onRefresh = onRefresh,
                                onOpenFilter = { showFilterSheet = true }
                            )

                            // Header
                            header?.let { item(key = "header") { it() } }

                            // Content or Empty
                            if (items.isEmpty() && !isLoading) {
                                item(key = "empty") { EmptyStateItem() }
                            } else {
                                listContent?.invoke(this)
                            }

                            // Filler
                            if (items.isNotEmpty() && fillerHeight > 0.dp) {
                                item(key = "filler") { Spacer(Modifier.height(fillerHeight)) }
                            }

                            // Pagination
                            paginationItem(
                                items = items,
                                showPaginationButton = showPaginationButton,
                                uiState = uiState,
                                onNextPage = onNextPage,
                                onPrevPage = onPrevPage,
                                onRowsPerPageChange = onRowsPerPageChange
                            )
                        }
                    }

                    // =============================
                    // GRID MODE
                    // =============================
                    DisplayMode.Grid -> {
                        LazyVerticalGrid(
                            columns = gridColumns.toGridCells(),
                            state = gridState,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            // Spacer top — span full width
                            item(key = "spacer-top", span = { GridItemSpan(maxLineSpan) }) {
                                Spacer(
                                    modifier = Modifier.height(
                                        if (screenConfig.isMobile) Spacing.normal else Spacing.extraLarge
                                    )
                                )
                            }

                            // Search + Filter — span full width
                            if (showSearch || filterOptions.isNotEmpty()) {
                                item(key = "search-filter", span = { GridItemSpan(maxLineSpan) }) {
                                    SearchFilterRow(
                                        isMobile = isMobile,
                                        showSearch = showSearch,
                                        filterOptions = filterOptions,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = {
                                            searchQuery = it
                                            onSearch(it)
                                        },
                                        onClearSearch = {
                                            searchQuery = ""
                                            onSearch("")
                                        },
                                        refreshCount = uiState.filters.size,
                                        onRefresh = onRefresh,
                                        onOpenFilter = { showFilterSheet = true }
                                    )
                                    Spacer(modifier = Modifier.height(Spacing.normal))
                                }
                            }

                            // Header — span full width
                            header?.let {
                                item(key = "header", span = { GridItemSpan(maxLineSpan) }) { it() }
                            }

                            // Content or Empty
                            if (items.isEmpty() && !isLoading) {
                                item(
                                    key = "empty",
                                    span = { GridItemSpan(maxLineSpan) }
                                ) { EmptyStateItem() }
                            } else {
                                gridContent?.invoke(this)
                            }

                            // Filler — span full width
                            if (items.isNotEmpty() && fillerHeight > 0.dp) {
                                item(
                                    key = "filler",
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    Spacer(Modifier.height(fillerHeight))
                                }
                            }

                            // Pagination — span full width
                            if (items.isNotEmpty() && showPaginationButton) {
                                item(
                                    key = "pagination",
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    TablePaginationFooter(
                                        rowsPerPage = uiState.pageLimit,
                                        totalItems = uiState.totalSize,
                                        currentPage = uiState.page,
                                        onNextPage = onNextPage,
                                        onPrevPage = onPrevPage,
                                        onRowsPerPageChange = onRowsPerPageChange
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // =============================
        // LOADING INDICATOR
        // =============================
        if (isLoading && !isRefreshing) {
            val isFirstLoading = !uiState.isSearching && uiState.page == 1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.normal)
                    .then(
                        if (isFirstLoading || showPaginationButton) {
                            Modifier.align(Alignment.Center)
                        } else {
                            Modifier.align(Alignment.BottomCenter)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Colors.ColorPrimary)
            }
        }

        // =============================
        // FILTER SHEET
        // =============================
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
}


// ====================================
// Helper: shared composables to reduce duplication
// ====================================

@Composable
private fun EmptyStateItem() {
    EmptyState(
        title = "Data Kosong",
        subtitle = "Belum ada data tersedia",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.extraLarge)
    )
}

/** Search filter as LazyList item — extracted to avoid code duplication in list mode */
private fun <STATE> LazyListScope.searchFilterItem(
    isMobile: Boolean,
    showSearch: Boolean,
    filterOptions: List<FilterGroup>,
    searchQuery: String,
    uiState: BaseUiState<STATE>,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRefresh: () -> Unit,
    onOpenFilter: () -> Unit,
) {
    if (showSearch || filterOptions.isNotEmpty()) {
        item(key = "search-filter") {
            SearchFilterRow(
                isMobile = isMobile,
                showSearch = showSearch,
                filterOptions = filterOptions,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onClearSearch = onClearSearch,
                refreshCount = uiState.filters.size,
                onRefresh = onRefresh,
                onOpenFilter = onOpenFilter
            )
            Spacer(modifier = Modifier.height(Spacing.normal))
        }
    }
}

/** Pagination as LazyList item */
private fun <STATE, ITEMS> LazyListScope.paginationItem(
    items: List<ITEMS>,
    showPaginationButton: Boolean,
    uiState: BaseUiState<STATE>,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit,
    onRowsPerPageChange: (Int) -> Unit,
) {
    if (items.isNotEmpty() && showPaginationButton) {
        item(key = "pagination") {
            TablePaginationFooter(
                rowsPerPage = uiState.pageLimit,
                totalItems = uiState.totalSize,
                currentPage = uiState.page,
                onNextPage = onNextPage,
                onPrevPage = onPrevPage,
                onRowsPerPageChange = onRowsPerPageChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <STATE, ITEMS> ListScaffoldWeb(
    modifier: Modifier = Modifier,
    title: String = "Title",
    screenConfig: ScreenConfig = ScreenConfig(),
    uiState: BaseUiState<STATE>,
    items: List<ITEMS> = emptyList(),
    horizontalPadding: Float? = null,

    // Keep your default width behavior (80% on web), but we’ll wrap it with fillMaxSize() internally
    contentModifier: Modifier = Modifier
        .fillMaxWidth(screenConfig.getHorizontalPaddingListWeight(horizontalPadding))
        .then(
            if (screenConfig.isMobile)
                Modifier
                    .padding(horizontal = Spacing.normal)
            else Modifier
        ),

    onUpdateUiState: (BaseUiState<STATE>.() -> BaseUiState<STATE>) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
    onRowsPerPageChange: (Int) -> Unit = {},
    onPrevPage: () -> Unit = {},
    onNextPage: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onAdd: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,

    filterOptions: List<FilterGroup> = emptyList(),
    showToolbar: Boolean = true,
    showSearch: Boolean = true,
    showPaginationButton: Boolean = false,
    header: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit,

    // ✅ New: minimum list height that follows perPage (estimation)
    // If your row/table height differs, just adjust this value.
    minListHeight: Dp = 350.dp,
    estimatedRowHeight: Dp = 56.dp,
) {
    val listState = uiState.listScrollState
    val isLoading = uiState.isLoading
    val isRefreshing = uiState.isRefreshing

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ✅ Scroll to top after refresh is done
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing && listState.firstVisibleItemIndex > 0) {
            listState.animateScrollToItem(0)
        }
    }

    // load more ketika scroll mentok
    LaunchedEffect(listState.canScrollForward, listState.isScrollInProgress) {
        if (!listState.canScrollForward && !listState.isScrollInProgress && uiState.hasMore && !isLoading && !showPaginationButton) {
            onLoadMore()
        }
    }

    val isMobile = screenConfig.isMobile || isMobilePhone()

// ✅ static minimum list height logic
    val contentHeight = estimatedRowHeight * items.size
    val fillerHeight = (minListHeight - contentHeight)
        .coerceAtLeast(0.dp)

    Box(
        modifier = modifier
            .background(Colors.White)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // =============================
            // TOOLBAR
            // =============================
            if (showToolbar) {
                ToolbarRow(
                    screenConfig = screenConfig,
                    backIcon = backIcon,
                    title = title,
                    onAdd = onAdd,
                    onBack = onBack
                )
            }

            RefreshContainer(
                isRefreshing = isRefreshing || (isLoading && uiState.isSearching),
                onRefresh = onRefresh,
                modifier = contentModifier
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                ) {

                    item {
                        if (screenConfig.isMobile) {
                            Spacer(modifier = Modifier.height(Spacing.normal))
                        } else {
                            Spacer(modifier = Modifier.height(Spacing.extraLarge))
                        }
                    }

                    // =============================
                    // SEARCH + FILTER
                    // =============================
                    if (showSearch || filterOptions.isNotEmpty()) {
                        item(key = "search-filter") {
                            SearchFilterRow(
                                isMobile = isMobile,
                                showSearch = showSearch,
                                filterOptions = filterOptions,
                                searchQuery = searchQuery,
                                onSearchQueryChange = {
                                    searchQuery = it
                                    onSearch(it)
                                },
                                onClearSearch = {
                                    searchQuery = ""
                                    onSearch("")
                                },
                                refreshCount = uiState.filters.size,
                                onRefresh = onRefresh,
                                onOpenFilter = { showFilterSheet = true }
                            )
                            Spacer(modifier = Modifier.height(Spacing.normal))
                        }
                    }

                    // =============================
                    // HEADER (e.g., table header)
                    // =============================
                    header?.let { item(key = "header") { it() } }

                    // =============================
                    // CONTENT ROWS
                    // =============================
                    if (items.isEmpty() && !isLoading) {
                        item(key = "empty") {
                            EmptyState(
                                title = "Data Kosong",
                                subtitle = "Belum ada data tersedia",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = Spacing.extraLarge)
                            )
                        }
                    } else {
                        content()
                    }

//                // ✅ Filler spacer to keep minimum list height based on perPage
                    if (items.isNotEmpty() && fillerHeight > 0.dp) {
                        item(key = "filler") {
                            Spacer(Modifier.height(fillerHeight))
                        }
                    }

                    // =============================
                    // FOOTER PAGINATION
                    // =============================
                    if (items.isNotEmpty() && showPaginationButton) {
                        item(key = "pagination") {
                            TablePaginationFooter(
                                rowsPerPage = uiState.pageLimit,
                                totalItems = uiState.totalSize,
                                currentPage = uiState.page,
                                onNextPage = onNextPage,
                                onPrevPage = onPrevPage,
                                onRowsPerPageChange = onRowsPerPageChange
                            )
                        }
                    }
                }
            }

        }

        // Loading indicator (shown at the end of the list)


        if (isLoading && !isRefreshing) {
            val isFirstLoading = !uiState.isSearching && uiState.page == 1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.normal)
                    .then(
                        if (isFirstLoading || showPaginationButton) {
                            Modifier.align(Alignment.Center)
                        } else {
                            Modifier.align(Alignment.BottomCenter)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Colors.ColorPrimary)
            }
        }

        // =============================
        // FILTER SHEET (overlay)
        // =============================
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
}

@Composable
private fun ToolbarRow(
    screenConfig: ScreenConfig = ScreenConfig(),
    title: String,
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onBack: (() -> Unit)?,
    onAdd: (() -> Unit)?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onBack != null) {
                        Modifier
                            .padding(end = if (screenConfig.isMobile) Spacing.normal else Spacing.large)
                            .padding(start = if (screenConfig.isMobile) Spacing.tiny else Spacing.normal)
                    } else {
                        Modifier.padding(horizontal = if (screenConfig.isMobile) Spacing.normal else Spacing.large)
                    }
                )
                .padding(vertical = Spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = backIcon,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(Spacing.tiny))
            }

            Text(
                text = title,
                style = TextAppearance.title2Bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            if (onAdd != null) {
                ButtonNormal(
                    text = "Tambah",
                    backgroundColor = Colors.Black,
                    horizontalContentPadding = Spacing.normal,
                    imageVector = Icons.Default.Add,
                    onClick = onAdd,
                    modifier = Modifier
                        .height(Heights.normal)
                        .defaultMinSize(minWidth = 90.dp)
                )
            }
        }

        SimpleHorizontalDivider(modifier = Modifier)
    }
}

@Composable
private fun SearchFilterRow(
    isMobile: Boolean,
    showSearch: Boolean,
    filterOptions: List<FilterGroup>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    refreshCount: Int,
    onRefresh: () -> Unit,
    onOpenFilter: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.box),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showSearch) {
            CustomTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                hint = "Cari...",
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 0.5.dp,
                strokeColor = Colors.Gray3,
                floatingLabel = false,
                cornerRadius = Radius.normal,
                leadingIcon = MyIcon.IcSearch,
                endIcon = if (searchQuery.isNotEmpty()) Icons.Default.Close else null,
                endIconOnClick = onClearSearch,
                modifier = Modifier.weight(1f)
            )
        }

        if (!isMobile) {
            Spacer(Modifier.weight(1.5f))
        }

        if (filterOptions.isNotEmpty()) {
            if (!isMobile) {
                RefreshButton(onClick = onRefresh)
//                Spacer(Modifier.width(Spacing.tiny))
            }
            FilterButton(count = refreshCount, onClick = onOpenFilter)
        }
    }
}


fun exampleTableSpec(): TableSpec<ExampleModel> {
    val columns = listOf(
        TableColumn<ExampleModel>(
            key = "name",
            title = "Nama",
            weight = 2f,
            cell = { row ->
                RowText(
                    modifier = Modifier.fillMaxWidth(),
                    text = row.name,
                    secondary = row.code
                )
            }
        ),
        TableColumn(
            key = "region",
            title = "Daerah",
            weight = 2f,
            cell = { row ->
                RowText(
                    modifier = Modifier.fillMaxWidth(),
                    text = row.daerah
                )
            }
        ),
        TableColumn(
            key = "updated",
            title = "Update",
            weight = 1f,
            cell = { row ->
                RowText(
                    modifier = Modifier.fillMaxWidth(),
                    text = row.createdAt
                )
            }
        )
    )

    return TableSpec(
        columns = columns,
        actionsWidth = 80.dp
    )
}

@Composable
fun RowText(
    modifier: Modifier = Modifier,
    text: String = "",
    secondary: String? = null,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier,
            style = TextAppearance.body1(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (secondary != null) {
            Text(
                text = secondary,
                modifier = Modifier,
                style = TextAppearance.body2(),
                color = Colors.Gray2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


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
fun ScreenContentWebPreview(
    screenConfig: ScreenConfig = ScreenConfig(),
) {
    val spec = remember { exampleTableSpec() }

    fun onBack() {

    }

    ListScaffoldWeb(
        uiState = BaseUiState(
            data = ExampleModel()
        ),
        items = list,
        screenConfig = screenConfig,
        filterOptions = defaultFilter(),
        header = { TableHeader(spec) },
        onAdd = {},
        onBack = if (screenConfig.isMobile) ::onBack else null,
        content = {
            items(list, key = { it.id }) { item ->
                TableRow(item = item, spec = spec, actions = {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(Spacing.box))
                    Icon(Icons.Default.MoreVert, null)
                })
            }
        })
}


@TabletPreview
@Composable
fun TabletNewPreview() {
    ScreenContentWebPreview(ScreenConfig(750.dp))
}

@MobilePreview
@Composable
fun MobileWebPreview() {
    ScreenContentWebPreview(ScreenConfig(500.dp))
}

