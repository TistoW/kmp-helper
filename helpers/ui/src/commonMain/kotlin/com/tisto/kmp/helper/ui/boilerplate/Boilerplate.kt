@file:Suppress("unused", "RedundantVisibilityModifier")

package com.tisto.kmp.helper.ui.boilerplate

// ══════════════════════════════════════════════════════════════════════════
// MVI / UDF BOILERPLATE — Rules + copy-ready templates.
// Replace "Example" with your feature name (e.g. "Invoice").
//
// REFERENCE IMPLEMENTATIONS (read side by side with this file):
//   1. Category (image + picker + CRUD): kmp/feature/product/.../category/
//      Use when: image upload, isActive toggle, picker mode, text+image form.
//   2. Discount (numeric + type chips):  kmp/feature/product/.../discount/
//      Use when: numeric/currency fields, type chips, conditional form fields, no image.
//
// FILE LAYOUT:
//   feature/<name>/
//   ├── data/
//   │   ├── model/<Name>.kt            ← @Serializable domain model (= DTO)
//   │   ├── request/<Name>Request.kt   ← outbound body, @Transient PickedImage
//   │   ├── <Name>Api.kt              ← Ktor HttpClient, BaseResponse<T>
//   │   ├── <Name>Repository.kt       ← apiCall() wrappers → Flow<Resource<T>>
//   │   └── <name>Module.kt           ← Koin: Api + Repo + both VMs
//   └── presentation/
//       ├── <Name>Route.kt             ← Crossfade wrapper (List ↔ Form)
//       ├── <Name>Strings.kt           ← Indonesian UI strings
//       ├── list/
//       │   ├── <Name>ListContract.kt  ← Effect only (UiState/Event are generic)
//       │   ├── <Name>ListViewModel.kt
//       │   └── <Name>ListScreen.kt
//       └── form/
//           ├── <Name>FormContract.kt
//           ├── <Name>FormViewModel.kt
//           └── <Name>FormScreen.kt
//   Folder convention: subfolders only when >=2 files (model/, request/, list/, form/).
//   Api, Repository, Module, Route, Strings stay flat at parent level.
//
// ══════════════════════════════════════════════════════════════════════════
// RULES — any deviation is a bug
// ══════════════════════════════════════════════════════════════════════════
//
// ── NETWORKING ──
//   Ktor HttpClient only, NEVER Retrofit.
//   Use helper-network extensions: postMethod, getMethod, putMethod, deleteMethod.
//   postMethod/putMethod accept a pickedImage param for multipart uploads.
//   API methods return BaseResponse<T> raw — no unwrapping in the Api layer.
//   URL prefix: $v2 from com.zenenta.core.utils.constants.Constants.
//
// ── REPOSITORY ──
//   Concrete class only, no interface, no UseCase layer.
//   Every method: apiCall(apiCall = { ... }, mapper = { it }).
//   Return type: Flow<Resource<T>>. Never suspend fun T. Never LiveData.
//   No try/catch. No caching. No Flow orchestration.
//
// ── DOMAIN MODEL vs DTO ──
//   One @Serializable data class in data/model/ — serves as both domain model and DTO.
//   No separate <Name>Dto with .toDomain(). No <Name>Draft.
//   <Name>Request is separate — outbound body with @Transient val pickedImage.
//
// ── VIEWMODEL — GENERAL ──
//   Extend FeatureViewModel<EFFECT> from com.tisto.kmp.helper.network.base.
//   Why not BaseViewModel<STATE>: it bundles pagination/scroll/search into UiState, too heavy for MVI.
//   FeatureViewModel provides: sendEffect, showMessage, showSuccess, showError, .launchResource().
//   Wire toast by overriding showMessage → sendEffect(<Name>Effect.ShowMessage(msg, type)).
//   After that, just call showSuccess(...) / showError(...) — no per-VM helper funs.
//   Single onEvent(event) entry point per VM.
//   ALL repo calls use .launchResource(fallbackError, onSuccess = { ... }). No exceptions.
//   NEVER use raw viewModelScope.launch — .launchResource() handles launching internally.
//   NEVER use try/catch in ViewModel — .launchResource() handles errors via onError callback.
//   NEVER use .onResource() directly — use .launchResource() which wraps it.
//   NEVER write .collect { when (resource) { Resource.Success -> ... } } manually.
//
// ── VIEWMODEL — LIST ──
//   Import ListUiState and ListEvent from com.tisto.kmp.helper.ui.component.
//   Do NOT declare per-feature UiState/Event — only Effect is feature-specific.
//   MutableStateFlow<ListUiState<T>>, direct .value assignment. No combine/flatMapLatest.
//   query, filters, perPage are plain private var — only events mutate them.
//   init { loadPage(1) }.
//   Extract buildSearchRequest(page) helper: SORT filters → orderBy/orderType,
//     FILTER items → simpleQuery entries. Default isActive="true" when no status filter.
//
//   Refresh state transitions (important — each state type has different behavior):
//     Success → copy(isRefreshing = true)
//     Empty → keep Empty as-is (so search bar stays visible)
//     else → ListUiState.Loading
//
//   Empty result: use ListUiState.Empty(query, filters), NOT Success(items = emptyList()).
//     Empty carries query + filters so the UI keeps search bar and filter badge visible.
//
//   Search debounce: QueryChanged must NOT call loadPage() directly.
//     Update state.query immediately (so text field never flickers),
//     cancel any pending searchJob, schedule loadPage(1) after 500ms delay.
//   Refresh (pull-to-refresh / back from form) calls loadPage(1) directly — no debounce.
//   FiltersApplied: store filters, update current state's filters, then loadPage(1).
//   DeleteConfirmed receives whole item (not just id) — extract id inside handler.
//
// ── PAGINATION ──
//   Success state carries: page, perPage, totalItems, lastPage, isLoadingMore.
//   Derived: hasMore = page < lastPage.
//   Events: LoadNextPage, PageChanged(page), RowsPerPageChanged(perPage).
//   loadPage(page) is the single fetch entry point (refresh, search, filter, page change).
//   loadNextPage() appends. Guard: if (isLoadingMore || !hasMore) return.
//   Mobile: infinite scroll via load_more sentinel triggering LoadNextPage.
//   Web/Desktop: manual pagination via TablePaginationFooter.
//
// ── LIST SCREEN ──
//   Use ListContainer<T> — it handles Scaffold, Toolbar, search/filter, pull-to-refresh,
//     pagination, infinite scroll, loading/error/empty states, delete confirmation.
//   Caller provides only: columns, mobileRow, filterOptions, strings, itemKey.
//   columns callback: (isPicker: Boolean, onEdit: (T)->Unit, onDelete: (T)->Unit) -> List<ListColumn<T>>
//   Hide action column when isPicker = true. Show titlePicker instead of title.
//   backIcon: default ArrowBack. Use AppIcon.IcMenuSolar for POS drawer features.
//   Do NOT manually write Scaffold/Toolbar/SearchFilterRow/pull-to-refresh/pagination in Screen.
//   Pull-to-refresh: mobile only (PullToRefreshBox). Web gets RefreshButton instead.
//   Filter: define private featureFilterOptions() returning List<FilterGroup>,
//     pass to ListContainer(filterOptions = ...).
//
// ── VIEWMODEL — FORM ──
//   Constructor param initialItem: <Name>?. Null = Create, non-null = Edit(initialItem.id).
//   Pre-populate UiState fields from initialItem in MutableStateFlow constructor.
//   init { if (initialItem != null) refreshInBackground(initialItem.id) }
//   refreshInBackground: repo.getOne(id).launchResource(onError = { /* silent */ }) { ... }.
//     Only updates fields the user hasn't touched yet (compare current against initialItem).
//     No viewModelScope.launch, no try/catch — launchResource handles both.
//   Single MutableStateFlow<FormUiState>. No combine, no stateIn, no separate flows.
//   Private helper: setSubmitting(Boolean) = _uiState.update { it.copy(isSubmitting = ...) }.
//     Call setSubmitting(true) before .launchResource(), reset in both onError and onSuccess.
//     No performSubmission wrapper, no try/finally, no viewModelScope.launch.
//   On NameChanged: also clear nameError = null.
//   Success message: send NavigateBack(message). Do NOT call showSuccess() before NavigateBack
//     — the form screen is disposed before the snackbar appears. The message gets forwarded
//     to the list screen via pendingMessage where it shows correctly.
//
// ── ROUTE / SCREEN SPLIT ──
//   Every screen has both. Route = stateful, Screen = pure @Composable.
//   Route: safeKoinViewModel() ?: return (NEVER koinViewModel — it crashes on missing VM).
//     Owns SnackbarHostState. Collects effects in private @Composable EffectHandler.
//     Wraps with ZenentaTheme { screenConfig -> ... } to get responsive ScreenConfig.
//     Passes viewModel::onEvent and screenConfig to Screen.
//   Screen: always declares screenConfig: ScreenConfig = ScreenConfig() as first param
//     so previews can call it without a theme wrapper.
//   List Route also takes: refreshToken, pendingMessage, onMessageShown.
//     LaunchedEffect(refreshToken) { if (refreshToken > 0) viewModel.onEvent(Refresh) }
//     LaunchedEffect(pendingMessage) { if (pendingMessage != null) { snackbar.show(); onMessageShown() } }
//   Form Route takes formKey, uses "${item?.id}_$formKey" as VM key.
//     Why: ensures fresh ViewModel each time form opens — prevents stale data.
//     Passes initialItem via parameters = { parametersOf(item) }.
//   EffectHandler: always LaunchedEffect(Unit) { effects.collect { when... } }.
//   ShowMessage → snackbar.showSnackbar(AppSnackbarVisuals(message, type)).
//     Map: MessageType.Success→SnackbarType.SUCCESS, Error→ERROR, Warning→WARNING, Info→INFO.
//   NavigateToForm(item) → onNavigateToForm(effect.item).
//   NavigateBack(message) → onDone(effect.message).
//
// ── WRAPPER ROUTE ──
//   Crossfade over a remember (NOT rememberSaveable) Stage sealed interface.
//   Why remember, not rememberSaveable: Stage is a non-Parcelable sealed interface —
//     rememberSaveable crashes with "MutableState containing List cannot be saved"
//     because Bundle serialization fails on custom sealed types.
//   refreshToken via remember { mutableIntStateOf(0) } — increment on form onDone.
//   formKey via remember { mutableIntStateOf(0) } — increment each time navigating to form.
//   pendingMessage via remember { mutableStateOf<String?>(null) } — captures form success
//     message, forwarded to list. Cleared via onMessageShown = { pendingMessage = null }.
//   private sealed interface Stage { data object List; data class Form(val item: <Name>?) }
//   No nav library. No NavHost.
//
// ── CROSS-MODULE PICKER SLOTS ──
//   When a Route accepts a picker from another kmp:feature module (e.g. customerPicker
//   in SalesCreateRoute), the two modules must stay independent — no direct type imports.
//   1. Consumer defines a local boundary type: data class Picked<Name>(val id, val name, ...)
//      Only include fields this module actually needs. Lives in <feature>/model/.
//   2. Picker slot signature uses the boundary type:
//        customerPicker: @Composable ((PickedCustomer) -> Unit, () -> Unit) -> Unit
//   3. Provider module (e.g. CustomerRoute) exposes its OWN internal type in onPick:
//        fun CustomerRoute(onPick: ((Customer) -> Unit)? = null)
//      No CoreCustomer alias, no toCoreX() bridge function.
//   4. Composition root (DashboardRouter / app module) maps between the two:
//        CustomerRoute(onPick = { customer -> onPick(PickedCustomer(id = customer.id, ...)) })
//   5. Inside the consumer, FormRoute converts boundary type → core model if needed:
//        LaunchedEffect(customerResult) { viewModel.onEvent(CustomerSelected(Customer(...))) }
//   NEVER import another kmp:feature module's model types directly.
//   NEVER create a shared :core:domain module just for 1-2 boundary types — only when
//     3+ modules share the same types or shared business logic is needed.
//
// ── KOIN MODULE ──
//   One module file per feature: val <name>Module = module { ... }.
//   singleOf(::<Name>Api), singleOf(::<Name>Repository).
//   viewModelOf(::<Name>ListViewModel).
//   Form VM: viewModel { params -> <Name>FormViewModel(repo = get(), initialItem = params.getOrNull()) }
//   Register in app/.../core/di/ListModules.kt.
//
// ── STRINGS ──
//   internal object <Name>Strings — all user-facing strings in one file.
//   Indonesian: "Kembali", "Cari...", "Tambah", "Simpan", "Menyimpan...",
//     "Belum ada data", "Gagal memuat data", "Gagal menyimpan", "Gagal menghapus",
//     "Data tersimpan", "Data berhasil dihapus", "Nama wajib diisi".
//   Include: titleList, titlePicker, titleCreate, titleEdit, search, empty,
//     label fields, err fields, loadFailed, saveFailed, deleteFailed, saved, deleted.
//
// ── UI COMPONENTS (use these — never raw Material3 equivalents) ──
//   ListContainer, ListColumn, ListHeader, ListRow, ListMobileRow, ListActions,
//   DeleteConfirmationDialog, FormContainer, ScaffoldBox, CustomTextField,
//   CurrencyTextField, SearchTextField, SwitchCard, CardImagePicker, BackHandler,
//   PullToRefreshBox, TablePaginationFooter, GeneralFilterBottomSheet, FilterButton,
//   RefreshButton, Toolbar, SearchFilterRow.
//
// ── PREVIEWS ──
//   Use @MobilePreview and @TabletPreview from com.tisto.kmp.helper.ui.ext.
//   Never use raw @Preview(widthDp=..., heightDp=...).
//   Wrap preview content in private ScreenContentPreview(screenConfig) helper.
//   List: Success state with 2-3 sample items. Form: fully filled Edit mode.
//
// ── DO NOT ──
//   Declare per-feature list UiState/Event (use generic ListUiState<T>/ListEvent<T>).
//   Use koinViewModel() (use safeKoinViewModel — it returns null instead of crashing).
//   Use BaseViewModel<STATE> (use FeatureViewModel<EFFECT>).
//   Re-declare effectChannel, effect, showMessage/Success/Error in feature VM.
//   Use LiveData or asLiveData().
//   Use viewModelScope.launch in feature VM (use .launchResource() which handles launching).
//   Use try/catch in feature VM (use .launchResource(onError = { ... }) for error handling).
//   Use .onResource() directly (use .launchResource() which wraps it).
//   Use performSubmission wrapper (use setSubmitting + .launchResource() directly).
//   Import another kmp:feature module's types (use boundary types + composition root mapping).
//   Manually write Scaffold/Toolbar/SearchFilterRow in list screens (ListContainer does it).
//   Use combine/stateIn/flatMapLatest in either VM — single MutableStateFlow only.
//   Split model into <Name>Dto + toDomain().
//   Use raw OutlinedTextField/TextField (use CustomTextField/CurrencyTextField/SearchTextField).
//   Use raw Switch in Row (use SwitchCard).
//   Use Scaffold+TopAppBar+FAB in list/form (use ListContainer / ScaffoldBox+FormContainer).
//   Derive ScreenConfig from LocalConfiguration inside Screen (receive it from ZenentaTheme).
//   Use raw @Preview (use @MobilePreview/@TabletPreview).
//   Use loadExisting(itemId) in form VM (use initialItem + refreshInBackground).
// ══════════════════════════════════════════════════════════════════════════

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tisto.kmp.helper.network.MessageType
import com.tisto.kmp.helper.utils.model.FilterItem
import kotlinx.serialization.Serializable

// ── Shared UI Helpers ─────────────────────────────────────────────────

@Composable
private fun CenteredLoader(padding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center,
    ) { CircularProgressIndicator() }
}

@Composable
private fun CenteredMessage(padding: PaddingValues, message: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center,
    ) { Text(message) }
}

// ══════════════════════════════════════════════════════════════════════════
// SECTION 1 — DOMAIN MODEL (data/model/Example.kt)
// ══════════════════════════════════════════════════════════════════════════

@Serializable
data class Example(
    var id: String = "",
    var outletId: String? = null,
    var name: String = "",
    var description: String? = null,
    var image: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var isActive: Boolean = true,
    var type: String = ExampleTypeTypes.pos,
)

object ExampleTypeTypes {
    val olshop: String = "olshop"
    val mall: String = "mall"
    val pos: String = "pos"
}

// ══════════════════════════════════════════════════════════════════════════
// SECTION 2 — REQUEST DTO (data/request/ExampleRequest.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// @Serializable
// data class ExampleRequest(
//     var id: String? = null,
//     var name: String? = null,
//     var description: String? = null,
//     var image: String? = null,
//     var isActive: Boolean = true,
//     var type: String = ExampleTypeTypes.pos,
//     @Transient val pickedImage: PickedImage? = null,
// )

// ══════════════════════════════════════════════════════════════════════════
// SECTION 3 — API SERVICE (data/ExampleApi.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// class ExampleApi(private val client: HttpClient) {
//     suspend fun create(body: ExampleRequest?): BaseResponse<Example> = client.postMethod(
//         url = "$v2/example", body = body, pickedImage = body?.pickedImage,
//     )
//     suspend fun get(search: SearchRequest? = null): BaseResponse<List<Example>> = client.getMethod(
//         url = "$v2/example", query = search?.convertToQuery(),
//     )
//     suspend fun getOne(id: String): BaseResponse<Example> = client.getMethod(url = "$v2/example/$id")
//     suspend fun update(body: ExampleRequest?): BaseResponse<Example> = client.putMethod(
//         url = "$v2/example/${body?.id}", body = body, pickedImage = body?.pickedImage,
//     )
//     suspend fun delete(id: String): BaseResponse<Example> = client.deleteMethod(url = "$v2/example/$id")
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 4 — REPOSITORY (data/ExampleRepository.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// class ExampleRepository(private val api: ExampleApi) {
//     fun create(body: ExampleRequest?) = apiCall(apiCall = { api.create(body) }, mapper = { it })
//     fun get(search: SearchRequest? = null) = apiCall(apiCall = { api.get(search) }, mapper = { it })
//     fun getOne(id: String) = apiCall(apiCall = { api.getOne(id) }, mapper = { it })
//     fun update(body: ExampleRequest?) = apiCall(apiCall = { api.update(body) }, mapper = { it })
//     fun delete(id: String) = apiCall(apiCall = { api.delete(id) }, mapper = { it })
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 5 — KOIN MODULE (data/exampleModule.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// val exampleModule = module {
//     singleOf(::ExampleApi)
//     singleOf(::ExampleRepository)
//     viewModelOf(::ExampleListViewModel)
//     viewModel { params -> ExampleFormViewModel(repo = get(), initialItem = params.getOrNull()) }
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 5B — STRINGS (presentation/ExampleStrings.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// internal object ExampleStrings {
//     const val titleList = "Example"
//     const val titlePicker = "Pilih Example"
//     const val titleCreate = "Tambah Example"
//     const val titleEdit = "Edit Example"
//     const val back = "Kembali"
//     const val add = "Tambah"
//     const val edit = "Edit"
//     const val delete = "Hapus"
//     const val save = "Simpan"
//     const val saving = "Menyimpan..."
//     const val search = "Cari..."
//     const val empty = "Belum ada data"
//     const val labelName = "Nama"
//     const val labelDescription = "Deskripsi"
//     const val labelActive = "Aktif"
//     const val columnUpdate = "Update"
//     const val errName = "Nama wajib diisi"
//     const val loadFailed = "Gagal memuat data"
//     const val saveFailed = "Gagal menyimpan"
//     const val deleteFailed = "Gagal menghapus"
//     const val saved = "Data tersimpan"
//     const val deleted = "Data berhasil dihapus"
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 6 — LIST CONTRACT (presentation/list/ExampleListContract.kt)
// UiState/Event are generic ListUiState<T>/ListEvent<T>. Only Effect is feature-specific.
// ══════════════════════════════════════════════════════════════════════════

sealed interface ExampleListEffect {
    data class ShowMessage(val message: String, val type: MessageType = MessageType.Info) : ExampleListEffect
    data class NavigateToForm(val item: Example?) : ExampleListEffect
}

// ══════════════════════════════════════════════════════════════════════════
// SECTION 7 — LIST VIEWMODEL (presentation/list/ExampleListViewModel.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// class ExampleListViewModel(
//     private val repo: ExampleRepository,
// ) : FeatureViewModel<ExampleListEffect>() {
//
//     private val _uiState = MutableStateFlow<ListUiState<Example>>(ListUiState.Loading)
//     val uiState: StateFlow<ListUiState<Example>> = _uiState.asStateFlow()
//
//     override fun showMessage(msg: String, type: MessageType) {
//         sendEffect(ExampleListEffect.ShowMessage(msg, type))
//     }
//
//     private var query = ""
//     private var perPage = 20
//     private var filters: List<FilterItem> = emptyList()
//     private var searchJob: Job? = null
//
//     init { loadPage(1) }
//
//     fun onEvent(event: ListEvent<Example>) {
//         when (event) {
//             ListEvent.Refresh -> loadPage(1)
//             is ListEvent.QueryChanged -> {
//                 query = event.query
//                 val current = _uiState.value
//                 if (current is ListUiState.Success) _uiState.value = current.copy(query = event.query)
//                 searchJob?.cancel()
//                 searchJob = viewModelScope.launch { delay(500); loadPage(1) }
//             }
//             is ListEvent.FiltersApplied -> {
//                 filters = event.filters
//                 val current = _uiState.value
//                 if (current is ListUiState.Success) _uiState.value = current.copy(filters = event.filters)
//                 loadPage(1)
//             }
//             ListEvent.CreateClicked -> sendEffect(ExampleListEffect.NavigateToForm(null))
//             is ListEvent.EditClicked -> sendEffect(ExampleListEffect.NavigateToForm(event.item))
//             is ListEvent.DeleteConfirmed -> delete(event.item.id)
//             ListEvent.LoadNextPage -> loadNextPage()
//             is ListEvent.PageChanged -> loadPage(event.page)
//             is ListEvent.RowsPerPageChanged -> { perPage = event.perPage; loadPage(1) }
//         }
//     }
//
//     private fun loadPage(page: Int) {
//         val current = _uiState.value
//         _uiState.value = when (current) {
//             is ListUiState.Success -> current.copy(isRefreshing = true)
//             is ListUiState.Empty -> current
//             else -> ListUiState.Loading
//         }
//         repo.get(buildSearchRequest(page)).launchResourcePaged(
//             fallbackError = ExampleStrings.loadFailed,
//             onError = { _uiState.value = ListUiState.Error(it) },
//         ) { result ->
//             val items = result.data
//             if (items.isEmpty() && page == 1) {
//                 _uiState.value = ListUiState.Empty(query = query, filters = filters)
//             } else {
//                 _uiState.value = ListUiState.Success(
//                     items = items, query = query, filters = filters,
//                     page = result.currentPage ?: page, perPage = result.perPage ?: perPage,
//                     totalItems = result.total ?: items.size, lastPage = result.lastPage,
//                 )
//             }
//         }
//     }
//
//     private fun loadNextPage() {
//         val current = _uiState.value
//         if (current !is ListUiState.Success || current.isLoadingMore || !current.hasMore) return
//         val nextPage = current.page + 1
//         _uiState.value = current.copy(isLoadingMore = true)
//         repo.get(buildSearchRequest(nextPage)).launchResourcePaged(
//             fallbackError = ExampleStrings.loadFailed,
//             onError = { _uiState.value = current.copy(isLoadingMore = false); showError(it) },
//         ) { result ->
//             val prev = _uiState.value as? ListUiState.Success ?: return@launchResourcePaged
//             _uiState.value = prev.copy(
//                 items = prev.items + result.data,
//                 page = result.currentPage ?: nextPage, perPage = result.perPage ?: perPage,
//                 totalItems = result.total ?: prev.totalItems, lastPage = result.lastPage,
//                 isLoadingMore = false,
//             )
//         }
//     }
//
//     private fun buildSearchRequest(page: Int): SearchRequest {
//         val sortFilter = filters.firstOrNull { it.type == FilterType.SORT }
//         val statusFilter = filters.firstOrNull { it.type == FilterType.FILTER && it.key == "isActive" }
//         return SearchRequest(
//             page = page, perpage = perPage,
//             orderBy = sortFilter?.key, orderType = sortFilter?.value,
//             simpleQuery = buildList {
//                 add(Search("isActive", statusFilter?.value ?: "true"))
//                 if (query.isNotBlank()) add(Search("name", query))
//             },
//         )
//     }
//
//     private fun delete(id: String) {
//         repo.delete(id).launchResource(fallbackError = ExampleStrings.deleteFailed) {
//             showSuccess(ExampleStrings.deleted); loadPage(1)
//         }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 8 — LIST SCREEN (presentation/list/ExampleListScreen.kt)
// Uses ListContainer<T> — handles Scaffold, Toolbar, search, filter, pagination, empty/loading/error.
// ══════════════════════════════════════════════════════════════════════════
//
// @Composable
// fun ExampleListRoute(
//     onNavigateToForm: (item: Example?) -> Unit,
//     onBack: () -> Unit,
//     onPick: ((Example) -> Unit)? = null,
//     refreshToken: Int = 0,
//     pendingMessage: String? = null,
//     onMessageShown: () -> Unit = {},
// ) {
//     val viewModel: ExampleListViewModel = safeKoinViewModel() ?: return
//     val state by viewModel.uiState.collectAsStateWithLifecycle()
//     val snackbar = remember { SnackbarHostState() }
//
//     ExampleListEffectHandler(viewModel.effect, snackbar, onNavigateToForm)
//     LaunchedEffect(refreshToken) { if (refreshToken > 0) viewModel.onEvent(ListEvent.Refresh) }
//     LaunchedEffect(pendingMessage) { if (pendingMessage != null) { snackbar.showSnackbar(pendingMessage); onMessageShown() } }
//
//     ZenentaTheme { screenConfig ->
//         ExampleListScreen(screenConfig = screenConfig, state = state, snackbar = snackbar,
//             onEvent = viewModel::onEvent, onBack = onBack, onPick = onPick)
//     }
// }
//
// @Composable
// private fun ExampleListEffectHandler(effects: Flow<ExampleListEffect>, snackbar: SnackbarHostState, onNavigateToForm: (Example?) -> Unit) {
//     LaunchedEffect(Unit) {
//         effects.collect { effect -> when (effect) {
//             is ExampleListEffect.ShowMessage -> snackbar.showSnackbar(effect.message)
//             is ExampleListEffect.NavigateToForm -> onNavigateToForm(effect.item)
//         }}
//     }
// }
//
// @Composable
// fun ExampleListScreen(
//     screenConfig: ScreenConfig = ScreenConfig(), state: ListUiState<Example>,
//     snackbar: SnackbarHostState, onEvent: (ListEvent<Example>) -> Unit,
//     onBack: () -> Unit, onPick: ((Example) -> Unit)? = null,
// ) {
//     ListContainer(
//         screenConfig = screenConfig, state = state, snackbar = snackbar,
//         title = ExampleStrings.titleList, titlePicker = ExampleStrings.titlePicker,
//         emptyText = ExampleStrings.empty, searchHint = ExampleStrings.search,
//         filterOptions = exampleFilterOptions(), backIcon = AppIcon.IcMenuSolar,
//         onEvent = onEvent, onBack = onBack, onPick = onPick,
//         deleteItemName = { it.name }, itemKey = { it.id },
//         columns = ::exampleColumns,
//         mobileRow = { item, onClick ->
//             ListMobileRow(imageUrl = item.image, text = item.name.def(),
//                 secondary = item.description?.takeIf { it.isNotBlank() }, onClick = onClick)
//         },
//     )
// }
//
// private fun exampleColumns(isPicker: Boolean, onEdit: (Example) -> Unit, onDelete: (Example) -> Unit): List<ListColumn<Example>> = buildList {
//     add(ListColumn(key = "name", title = ExampleStrings.labelName, weight = 3f, cell = { item ->
//         Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
//             CustomImageView(imageUrl = item.image, size = 50.dp)
//             Spacer(Modifier.width(12.dp))
//             RowText(modifier = Modifier.weight(1f), text = item.name.def(), secondary = item.description?.takeIf { it.isNotBlank() })
//         }
//     }))
//     add(ListColumn(key = "updated", title = ExampleStrings.columnUpdate, weight = 1f, cell = { item ->
//         RowText(modifier = Modifier.fillMaxWidth(), text = item.updatedAt?.formatDate("dd MMM yyyy").def("-"), secondary = item.updatedAt?.formatDate("HH:mm"))
//     }))
//     if (!isPicker) {
//         add(ListColumn(key = "action", title = "", weight = 0.5f, contentArrangement = Arrangement.Center, cell = { item ->
//             ListActions(onEdit = { onEdit(item) }, onDelete = { onDelete(item) })
//         }))
//     }
// }
//
// private fun exampleFilterOptions() = listOf(
//     FilterGroup(title = "Urutkan", type = FilterType.SORT, options = listOf(
//         FilterItem("Nama: A-Z", "asc", "name", FilterType.SORT),
//         FilterItem("Nama: Z-A", "desc", "name", FilterType.SORT),
//         FilterItem("Terbaru", "desc", "createdAt", FilterType.SORT),
//         FilterItem("Terlama", "asc", "createdAt", FilterType.SORT),
//     )),
// )
//
// @Composable private fun ScreenContentPreview(screenConfig: ScreenConfig = ScreenConfig()) {
//     ZenentaTheme {
//         ExampleListScreen(screenConfig = screenConfig, state = ListUiState.Success(
//             items = listOf(Example(id="1",name="Item Satu",updatedAt="2025-12-22T04:12:09.000Z"),
//                 Example(id="2",name="Item Dua",updatedAt="2025-12-22T04:12:09.000Z"),
//                 Example(id="3",name="Item Tiga",updatedAt="2025-12-22T04:12:09.000Z")),
//             totalItems = 25, lastPage = 3),
//             snackbar = SnackbarHostState(), onEvent = {}, onBack = {})
//     }
// }
// @MobilePreview @Composable private fun PreviewExampleList() { ScreenContentPreview() }
// @TabletPreview @Composable private fun TabletPreviewExampleList() { ScreenContentPreview(ScreenConfig(700.dp)) }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 9 — FORM CONTRACT (presentation/form/ExampleFormContract.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// sealed interface ExampleFormMode {
//     data object Create : ExampleFormMode
//     data class Edit(val itemId: String) : ExampleFormMode
// }
//
// data class ExampleFormUiState(
//     val mode: ExampleFormMode = ExampleFormMode.Create,
//     val name: String = "", val description: String = "",
//     val imageUrl: String? = null, val pickedImage: PickedImage? = null,
//     val isActive: Boolean = true, val nameError: String? = null,
//     val isLoading: Boolean = false, val isSubmitting: Boolean = false,
// ) { val canSubmit: Boolean get() = !isSubmitting && !isLoading && name.isNotBlank() }
//
// sealed interface ExampleFormEvent {
//     data class NameChanged(val value: String) : ExampleFormEvent
//     data class DescriptionChanged(val value: String) : ExampleFormEvent
//     data class ImagePicked(val value: PickedImage?) : ExampleFormEvent
//     data class ActiveChanged(val value: Boolean) : ExampleFormEvent
//     data object Submit : ExampleFormEvent
//     data object Delete : ExampleFormEvent
// }
//
// sealed interface ExampleFormEffect {
//     data class ShowMessage(val message: String, val type: MessageType = MessageType.Info) : ExampleFormEffect
//     data class NavigateBack(val message: String? = null) : ExampleFormEffect
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 10 — FORM VIEWMODEL (presentation/form/ExampleFormViewModel.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// class ExampleFormViewModel(
//     private val repo: ExampleRepository,
//     private val initialItem: Example?,
// ) : FeatureViewModel<ExampleFormEffect>() {
//
//     private val mode: ExampleFormMode =
//         if (initialItem == null) ExampleFormMode.Create else ExampleFormMode.Edit(initialItem.id)
//
//     private val _uiState = MutableStateFlow(ExampleFormUiState(
//         mode = mode, name = initialItem?.name.orEmpty(),
//         description = initialItem?.description.orEmpty(),
//         imageUrl = initialItem?.image, isActive = initialItem?.isActive ?: true,
//     ))
//     val uiState: StateFlow<ExampleFormUiState> = _uiState.asStateFlow()
//
//     private fun setSubmitting(isSubmitting: Boolean) {
//         _uiState.update { it.copy(isSubmitting = isSubmitting) }
//     }
//
//     override fun showMessage(msg: String, type: MessageType) {
//         sendEffect(ExampleFormEffect.ShowMessage(msg, type))
//     }
//
//     init { if (initialItem != null) refreshInBackground(initialItem.id) }
//
//     fun onEvent(event: ExampleFormEvent) {
//         when (event) {
//             is ExampleFormEvent.NameChanged -> _uiState.update { it.copy(name = event.value, nameError = null) }
//             is ExampleFormEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.value) }
//             is ExampleFormEvent.ImagePicked -> _uiState.update { it.copy(pickedImage = event.value) }
//             is ExampleFormEvent.ActiveChanged -> _uiState.update { it.copy(isActive = event.value) }
//             ExampleFormEvent.Submit -> submit()
//             ExampleFormEvent.Delete -> delete()
//         }
//     }
//
//     private fun refreshInBackground(id: String) {
//         repo.getOne(id).launchResource(
//             fallbackError = ExampleStrings.loadFailed,
//             onError = { /* silent — we already have list data */ },
//         ) { item ->
//             val current = _uiState.value
//             _uiState.update { it.copy(
//                 name = if (current.name == initialItem?.name.orEmpty()) item.name else current.name,
//                 description = if (current.description == initialItem?.description.orEmpty()) item.description.orEmpty() else current.description,
//                 imageUrl = if (current.pickedImage == null) item.image else current.imageUrl,
//                 isActive = if (current.isActive == initialItem?.isActive) item.isActive else current.isActive,
//             )}
//         }
//     }
//
//     private fun submit() {
//         val state = _uiState.value
//         if (state.name.isBlank()) { _uiState.update { it.copy(nameError = ExampleStrings.errName) }; return }
//         setSubmitting(true)
//         val request = ExampleRequest(
//             id = (mode as? ExampleFormMode.Edit)?.itemId,
//             name = state.name.trim(), description = state.description.trim().takeIf { it.isNotEmpty() },
//             image = state.imageUrl, isActive = state.isActive, pickedImage = state.pickedImage,
//         )
//         val flow = if (mode is ExampleFormMode.Create) repo.create(request) else repo.update(request)
//         flow.launchResource(
//             fallbackError = ExampleStrings.saveFailed,
//             onError = { setSubmitting(false); showError(it) },
//         ) {
//             setSubmitting(false)
//             sendEffect(ExampleFormEffect.NavigateBack(ExampleStrings.saved))
//         }
//     }
//
//     private fun delete() {
//         val editMode = mode as? ExampleFormMode.Edit ?: return
//         setSubmitting(true)
//         repo.delete(editMode.itemId).launchResource(
//             fallbackError = ExampleStrings.deleteFailed,
//             onError = { setSubmitting(false); showError(it) },
//         ) {
//             setSubmitting(false)
//             sendEffect(ExampleFormEffect.NavigateBack(ExampleStrings.deleted))
//         }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 11 — FORM SCREEN (presentation/form/ExampleFormScreen.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// @Composable
// fun ExampleFormRoute(item: Example?, formKey: Int = 0, onDone: (message: String?) -> Unit) {
//     val viewModel: ExampleFormViewModel = safeKoinViewModel(
//         key = "${item?.id}_$formKey", parameters = { parametersOf(item) },
//     ) ?: return
//     val state by viewModel.uiState.collectAsStateWithLifecycle()
//     val snackbar = remember { SnackbarHostState() }
//     ExampleFormEffectHandler(viewModel.effect, snackbar, onDone)
//     ZenentaTheme { screenConfig ->
//         ExampleFormScreen(state = state, snackbar = snackbar, screenConfig = screenConfig,
//             onEvent = viewModel::onEvent, onBack = { onDone(null) })
//     }
// }
//
// @Composable
// private fun ExampleFormEffectHandler(effects: Flow<ExampleFormEffect>, snackbar: SnackbarHostState, onDone: (message: String?) -> Unit) {
//     LaunchedEffect(Unit) {
//         effects.collect { effect -> when (effect) {
//             is ExampleFormEffect.ShowMessage -> snackbar.showSnackbar(AppSnackbarVisuals(
//                 message = effect.message, type = when (effect.type) {
//                     MessageType.Success -> SnackbarType.SUCCESS; MessageType.Error -> SnackbarType.ERROR
//                     MessageType.Warning -> SnackbarType.WARNING; MessageType.Info -> SnackbarType.INFO
//                 }))
//             is ExampleFormEffect.NavigateBack -> onDone(effect.message)
//         }}
//     }
// }
//
// @Composable
// fun ExampleFormScreen(
//     state: ExampleFormUiState, snackbar: SnackbarHostState,
//     screenConfig: ScreenConfig = ScreenConfig(),
//     onEvent: (ExampleFormEvent) -> Unit, onBack: () -> Unit,
// ) {
//     BackHandler { onBack() }
//     ScaffoldBox(snackbarHostState = snackbar) {
//         FormContainer(
//             forceTitle = if (state.mode is ExampleFormMode.Edit) ExampleStrings.titleEdit else ExampleStrings.titleCreate,
//             screenConfig = screenConfig, item = (state.mode as? ExampleFormMode.Edit)?.itemId,
//             selectedItemName = state.name, isFormValid = state.canSubmit,
//             isLoadingProcess = state.isSubmitting, onBack = onBack,
//             onSave = { onEvent(ExampleFormEvent.Submit) },
//             onDelete = { onEvent(ExampleFormEvent.Delete) },
//             horizontalAlignment = Alignment.CenterHorizontally,
//         ) {
//             Spacer(Modifier.height(if (screenConfig.isMobile) Spacing.normal else Spacing.large))
//             CardImagePicker(modifier = Modifier.width(120.dp).height(120.dp),
//                 imageUrl = state.imageUrl, onPicker = { onEvent(ExampleFormEvent.ImagePicked(it)) })
//             Spacer(Modifier.height(Spacing.large))
//             CustomTextField(value = state.name, onValueChange = { onEvent(ExampleFormEvent.NameChanged(it)) },
//                 label = ExampleStrings.labelName, style = TextFieldStyle.OUTLINED, strokeWidth = 1.dp,
//                 isError = state.nameError != null, supportingText = state.nameError,
//                 modifier = Modifier.fillMaxWidth(), singleLine = true)
//             Spacer(Modifier.height(Spacing.normal))
//             CustomTextField(value = state.description, onValueChange = { onEvent(ExampleFormEvent.DescriptionChanged(it)) },
//                 label = ExampleStrings.labelDescription, style = TextFieldStyle.OUTLINED,
//                 strokeWidth = 1.dp, modifier = Modifier.fillMaxWidth())
//         }
//     }
// }
//
// @MobilePreview @Composable private fun PreviewExampleFormPhone() {
//     ExampleFormScreen(state = ExampleFormUiState(mode = ExampleFormMode.Edit("1"),
//         name = "Contoh Item", description = "Deskripsi contoh item"),
//         snackbar = SnackbarHostState(), screenConfig = ScreenConfig(maxWidth = 360.dp), onEvent = {}, onBack = {})
// }
// @TabletPreview @Composable private fun PreviewExampleFormTablet() {
//     ExampleFormScreen(state = ExampleFormUiState(mode = ExampleFormMode.Edit("1"),
//         name = "Contoh Item", description = "Deskripsi contoh item"),
//         snackbar = SnackbarHostState(), screenConfig = ScreenConfig(maxWidth = 840.dp), onEvent = {}, onBack = {})
// }

// ══════════════════════════════════════════════════════════════════════════
// SECTION 12 — WRAPPER ROUTE (presentation/ExampleRoute.kt)
// ══════════════════════════════════════════════════════════════════════════
//
// @Composable
// fun ExampleRoute(onBack: () -> Unit = {}, onPick: ((Example) -> Unit)? = null) {
//     var stage by remember { mutableStateOf<Stage>(Stage.List) }
//     var refreshToken by remember { mutableIntStateOf(0) }
//     var formKey by remember { mutableIntStateOf(0) }
//     var pendingMessage by remember { mutableStateOf<String?>(null) }
//
//     Crossfade(targetState = stage, modifier = Modifier.fillMaxSize()) { current ->
//         when (current) {
//             Stage.List -> ExampleListRoute(
//                 onNavigateToForm = { item -> formKey++; stage = Stage.Form(item) },
//                 onBack = onBack, onPick = onPick, refreshToken = refreshToken,
//                 pendingMessage = pendingMessage, onMessageShown = { pendingMessage = null })
//             is Stage.Form -> ExampleFormRoute(
//                 item = current.item, formKey = formKey,
//                 onDone = { message -> pendingMessage = message; refreshToken++; stage = Stage.List })
//         }
//     }
// }
//
// private sealed interface Stage {
//     data object List : Stage
//     data class Form(val item: Example?) : Stage
// }
