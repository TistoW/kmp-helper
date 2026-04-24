package com.tisto.kmp.helper.ui.boilerplate.sample.presentation.list

import androidx.lifecycle.viewModelScope
import com.tisto.kmp.helper.network.MessageType
import com.tisto.kmp.helper.network.base.FeatureViewModel
import com.tisto.kmp.helper.network.model.Search
import com.tisto.kmp.helper.network.model.SearchRequest
import com.tisto.kmp.helper.ui.boilerplate.sample.data.SampleRepository
import com.tisto.kmp.helper.ui.boilerplate.sample.data.model.Sample
import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.SampleStrings
import com.tisto.kmp.helper.ui.component.ListEvent
import com.tisto.kmp.helper.ui.component.ListUiState
import com.tisto.kmp.helper.utils.model.FilterItem
import com.tisto.kmp.helper.utils.model.FilterType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ══════════════════════════════════════════════════════════════════════════
// LIST VIEWMODEL
//
// Extends FeatureViewModel<Effect>, BUKAN BaseViewModel<State>.
// Import ListUiState dan ListEvent — TIDAK deklarasi per-feature UiState/Event.
// MutableStateFlow<ListUiState<T>>, direct .value assignment.
// query, filters, perPage = plain private var — hanya events yang mutate.
// init { loadPage(1) }.
//
// Key patterns:
//   - loadPage(page): single fetch entry point (refresh, search, filter, page)
//   - loadNextPage(): append, guard isLoadingMore/hasMore
//   - buildSearchRequest(page): SORT → orderBy/orderType, FILTER → simpleQuery
//   - Search debounce: QueryChanged → delay 500ms → loadPage(1)
//   - Refresh state: Success → copy(isRefreshing=true), Empty → keep, else → Loading
//   - Empty result: ListUiState.Empty(query, filters), BUKAN Success(items=emptyList())
//   - DeleteConfirmed menerima whole item — extract id di handler
// ══════════════════════════════════════════════════════════════════════════

internal class SampleListViewModel(
    private val repo: SampleRepository,
) : FeatureViewModel<SampleListEffect>() {

    private val _uiState = MutableStateFlow<ListUiState<Sample>>(ListUiState.Loading)
    val uiState: StateFlow<ListUiState<Sample>> = _uiState.asStateFlow()

    override fun showMessage(msg: String, type: MessageType) {
        sendEffect(SampleListEffect.ShowMessage(msg, type))
    }

    private var query = ""
    private var perPage = 20
    private var filters: List<FilterItem> = emptyList()
    private var searchJob: Job? = null

    init {
        loadPage(1)
    }

    fun onEvent(event: ListEvent<Sample>) {
        when (event) {
            ListEvent.Refresh -> loadPage(1)

            is ListEvent.QueryChanged -> {
                // Update query + state immediately (text field tidak flicker)
                query = event.query
                val current = _uiState.value
                if (current is ListUiState.Success) {
                    _uiState.value = current.copy(query = event.query)
                }
                // Debounce: cancel pending, schedule after 500ms
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    loadPage(1)
                }
            }

            is ListEvent.FiltersApplied -> {
                filters = event.filters
                val current = _uiState.value
                if (current is ListUiState.Success) {
                    _uiState.value = current.copy(filters = event.filters)
                }
                loadPage(1)
            }

            ListEvent.CreateClicked -> sendEffect(SampleListEffect.NavigateToForm(null))
            is ListEvent.EditClicked -> sendEffect(SampleListEffect.NavigateToForm(event.item))
            is ListEvent.DeleteConfirmed -> delete(event.item.id)
            ListEvent.LoadNextPage -> loadNextPage()
            is ListEvent.PageChanged -> loadPage(event.page)
            is ListEvent.RowsPerPageChanged -> {
                perPage = event.perPage
                loadPage(1)
            }
        }
    }

    // ── Single fetch entry point ────────────────────────────────────────

    private fun loadPage(page: Int) {
        // Refresh state transitions:
        //   Success → copy(isRefreshing = true)
        //   Empty   → keep Empty as-is (search bar tetap visible)
        //   else    → ListUiState.Loading
        val current = _uiState.value
        _uiState.value = when (current) {
            is ListUiState.Success -> current.copy(isRefreshing = true)
            is ListUiState.Empty -> current
            else -> ListUiState.Loading
        }

        repo.get(buildSearchRequest(page)).launchResourcePaged(
            fallbackError = SampleStrings.loadFailed,
            onError = { _uiState.value = ListUiState.Error(it) },
        ) { result ->
            val items = result.data
            if (items.isEmpty() && page == 1) {
                _uiState.value = ListUiState.Empty(query = query, filters = filters)
            } else {
                _uiState.value = ListUiState.Success(
                    items = items,
                    query = query,
                    filters = filters,
                    page = result.currentPage ?: page,
                    perPage = result.perPage ?: perPage,
                    totalItems = result.total ?: items.size,
                    lastPage = result.lastPage,
                )
            }
        }
    }

    // ── Infinite scroll append ──────────────────────────────────────────

    private fun loadNextPage() {
        val current = _uiState.value
        if (current !is ListUiState.Success || current.isLoadingMore || !current.hasMore) return

        val nextPage = current.page + 1
        _uiState.value = current.copy(isLoadingMore = true)

        repo.get(buildSearchRequest(nextPage)).launchResourcePaged(
            fallbackError = SampleStrings.loadFailed,
            onError = {
                _uiState.value = current.copy(isLoadingMore = false)
                showError(it)
            },
        ) { result ->
            val prev = _uiState.value as? ListUiState.Success ?: return@launchResourcePaged
            _uiState.value = prev.copy(
                items = prev.items + result.data,
                page = result.currentPage ?: nextPage,
                perPage = result.perPage ?: perPage,
                totalItems = result.total ?: prev.totalItems,
                lastPage = result.lastPage,
                isLoadingMore = false,
            )
        }
    }

    // ── Search request builder ──────────────────────────────────────────

    private fun buildSearchRequest(page: Int): SearchRequest {
        val sortFilter = filters.firstOrNull { it.type == FilterType.SORT }
        val statusFilter = filters.firstOrNull {
            it.type == FilterType.FILTER && it.key == "isActive"
        }
        return SearchRequest(
            page = page,
            perpage = perPage,
            orderBy = sortFilter?.key,
            orderType = sortFilter?.value,
            simpleQuery = buildList {
                add(Search("isActive", statusFilter?.value ?: "true"))
                if (query.isNotBlank()) add(Search("name", query))
            },
        )
    }

    // ── Delete ──────────────────────────────────────────────────────────

    private fun delete(id: String) {
        repo.delete(id).launchResource(fallbackError = SampleStrings.deleteFailed) {
            showSuccess(SampleStrings.deleted)
            loadPage(1)
        }
    }
}
