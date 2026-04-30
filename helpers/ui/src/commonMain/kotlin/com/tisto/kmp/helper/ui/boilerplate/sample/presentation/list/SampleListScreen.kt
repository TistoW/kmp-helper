@file:Suppress("unused")

package com.tisto.kmp.helper.ui.boilerplate.sample.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tisto.kmp.helper.ui.boilerplate.sample.data.model.Sample
import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.SampleStrings
import com.tisto.kmp.helper.ui.component.AppSnackbarVisuals
import com.tisto.kmp.helper.ui.component.CustomImageView
import com.tisto.kmp.helper.ui.component.ListActions
import com.tisto.kmp.helper.ui.component.ListColumn
import com.tisto.kmp.helper.ui.component.ListContainer
import com.tisto.kmp.helper.ui.component.ListEvent
import com.tisto.kmp.helper.ui.component.ListMobileRow
import com.tisto.kmp.helper.ui.component.ListUiState
import com.tisto.kmp.helper.ui.component.RowText
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.ext.safeKoinViewModel
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.utils.ext.def
import com.tisto.kmp.helper.utils.ext.formatDate
import com.tisto.kmp.helper.utils.model.FilterGroup
import com.tisto.kmp.helper.utils.model.FilterItem
import com.tisto.kmp.helper.utils.model.FilterType
import com.tisto.kmp.helper.ui.component.toSnackbarType
import kotlinx.coroutines.flow.Flow

// ══════════════════════════════════════════════════════════════════════════
// LIST SCREEN
//
// Route = stateful: safeKoinViewModel, SnackbarHostState, effect handler.
// Screen = pure @Composable: hanya terima data + callbacks.
//
// Gunakan ListContainer<T> — handles Scaffold, Toolbar, search/filter,
//   pull-to-refresh, pagination, infinite scroll, loading/error/empty,
//   delete confirmation.
// Caller provides: columns, mobileRow, filterOptions, strings, itemKey.
//
// columns callback: (isPicker, onEdit, onDelete) -> List<ListColumn<T>>
//   Hide action column saat isPicker = true.
//
// backIcon: default ArrowBack. Gunakan AppIcon.IcMenuSolar untuk POS drawer features.
// ══════════════════════════════════════════════════════════════════════════

// ── Route (stateful) ────────────────────────────────────────────────────

@Composable
internal fun SampleListRoute(
    onNavigateToForm: (item: Sample?) -> Unit,
    onBack: () -> Unit,
    onPick: ((Sample) -> Unit)? = null,
    refreshToken: Int = 0,
    pendingMessage: String? = null,
    onMessageShown: () -> Unit = {},
) {
    val viewModel: SampleListViewModel = safeKoinViewModel() ?: return
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    // Effect handler
    SampleListEffectHandler(viewModel.effect, snackbar, onNavigateToForm)

    // Refresh saat kembali dari form
    LaunchedEffect(refreshToken) {
        if (refreshToken > 0) viewModel.onEvent(ListEvent.Refresh)
    }

    // Forward pending message dari form
    LaunchedEffect(pendingMessage) {
        if (pendingMessage != null) {
            snackbar.showSnackbar(pendingMessage)
            onMessageShown()
        }
    }

    // Di project gunakan AppTheme { screenConfig -> ... }
    // Di helper gunakan HelperTheme + ScreenConfig default
    HelperTheme {
        SampleListScreen(
            state = state,
            snackbar = snackbar,
            onEvent = viewModel::onEvent,
            onBack = onBack,
            onPick = onPick,
        )
    }
}

// ── Effect Handler ──────────────────────────────────────────────────────

@Composable
private fun SampleListEffectHandler(
    effects: Flow<SampleListEffect>,
    snackbar: SnackbarHostState,
    onNavigateToForm: (Sample?) -> Unit,
) {
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is SampleListEffect.ShowMessage -> snackbar.showSnackbar(
                    AppSnackbarVisuals(
                        message = effect.message,
                        type = effect.type.toSnackbarType(),
                    )
                )
                is SampleListEffect.NavigateToForm -> onNavigateToForm(effect.item)
            }
        }
    }
}

// ── Screen (pure) ───────────────────────────────────────────────────────

@Composable
internal fun SampleListScreen(
    screenConfig: ScreenConfig = ScreenConfig(),
    state: ListUiState<Sample>,
    snackbar: SnackbarHostState,
    onEvent: (ListEvent<Sample>) -> Unit,
    onBack: () -> Unit,
    onPick: ((Sample) -> Unit)? = null,
) {
    ListContainer(
        screenConfig = screenConfig,
        state = state,
        snackbar = snackbar,
        title = SampleStrings.titleList,
        titlePicker = SampleStrings.titlePicker,
        emptyText = SampleStrings.empty,
        searchHint = SampleStrings.search,
        filterOptions = sampleFilterOptions(),
        onEvent = onEvent,
        onBack = onBack,
        onPick = onPick,
        deleteItemName = { it.name },
        itemKey = { it.id },
        tabletRow = ::sampleColumns,
        mobileRow = { item, onClick ->
            ListMobileRow(
                imageUrl = item.image,
                text = item.name.def(),
                secondary = item.description?.takeIf { it.isNotBlank() },
                onClick = onClick,
            )
        },
    )
}

// ── Columns (tablet/desktop) ────────────────────────────────────────────

private fun sampleColumns(
    isPicker: Boolean,
    onEdit: (Sample) -> Unit,
    onDelete: (Sample) -> Unit,
): List<ListColumn<Sample>> = buildList {
    add(ListColumn(
        key = "name",
        title = SampleStrings.labelName,
        weight = 3f,
        cell = { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CustomImageView(imageUrl = item.image, size = 50.dp)
                Spacer(Modifier.width(12.dp))
                RowText(
                    modifier = Modifier.weight(1f),
                    text = item.name.def(),
                    secondary = item.description?.takeIf { it.isNotBlank() },
                )
            }
        },
    ))
    add(ListColumn(
        key = "updated",
        title = SampleStrings.columnUpdate,
        weight = 1f,
        cell = { item ->
            RowText(
                modifier = Modifier.fillMaxWidth(),
                text = item.updatedAt?.formatDate("dd MMM yyyy").def("-"),
                secondary = item.updatedAt?.formatDate("HH:mm"),
            )
        },
    ))
    if (!isPicker) {
        add(ListColumn(
            key = "action",
            title = "",
            weight = 0.5f,
            contentArrangement = Arrangement.Center,
            cell = { item ->
                ListActions(onEdit = { onEdit(item) }, onDelete = { onDelete(item) })
            },
        ))
    }
}

// ── Filter Options ──────────────────────────────────────────────────────

private fun sampleFilterOptions() = listOf(
    FilterGroup(
        title = "Urutkan",
        type = FilterType.SORT,
        options = listOf(
            FilterItem("Nama: A-Z", "asc", "name", FilterType.SORT),
            FilterItem("Nama: Z-A", "desc", "name", FilterType.SORT),
            FilterItem("Terbaru", "desc", "createdAt", FilterType.SORT),
            FilterItem("Terlama", "asc", "createdAt", FilterType.SORT),
        ),
    ),
)

// ── Previews ────────────────────────────────────────────────────────────

@Composable
private fun SampleListPreviewContent(screenConfig: ScreenConfig = ScreenConfig()) {
    HelperTheme {
        SampleListScreen(
            screenConfig = screenConfig,
            state = ListUiState.Success(
                items = listOf(
                    Sample(id = "1", name = "Item Satu", updatedAt = "2025-12-22T04:12:09.000Z"),
                    Sample(id = "2", name = "Item Dua", updatedAt = "2025-12-22T04:12:09.000Z"),
                    Sample(id = "3", name = "Item Tiga", updatedAt = "2025-12-22T04:12:09.000Z"),
                ),
                totalItems = 25,
                lastPage = 3,
            ),
            snackbar = SnackbarHostState(),
            onEvent = {},
            onBack = {},
        )
    }
}

@MobilePreview
@Composable
private fun PreviewSampleList() {
    SampleListPreviewContent()
}

@TabletPreview
@Composable
private fun TabletPreviewSampleList() {
    SampleListPreviewContent(ScreenConfig(700.dp))
}
