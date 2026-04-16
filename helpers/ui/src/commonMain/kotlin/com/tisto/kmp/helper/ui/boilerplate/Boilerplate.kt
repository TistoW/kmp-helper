@file:Suppress("unused", "RedundantVisibilityModifier")

package com.tisto.kmp.helper.ui.boilerplate

// ══════════════════════════════════════════════════════════════════════════════════════════
// MVI / UDF BOILERPLATE — The rulebook + full concrete reference for new features.
//
// READ THIS FIRST. This single file holds everything: the non-negotiable rules,
// then 12 copy-ready sections mirroring the canonical Category feature shipped at:
//
//   data → kmp/feature/transaction/.../categoryFinance/data/
//     ├── model/CategoryFinance.kt
//     ├── request/CategoryFinanceRequest.kt
//     ├── CategoryFinanceApi.kt                ← Ktor HttpClient
//     ├── CategoryFinanceRepository.kt         ← apiCall() → Flow<Resource<T>>
//     └── categoryFinanceModule.kt
//   ui   → kmp/feature/transaction/.../categoryFinance/presentation/
//     ├── CategoryFinanceRoute.kt              ← Crossfade wrapper
//     ├── CategoryFinanceStrings.kt            ← Indonesian strings (no R.string)
//     ├── list/{Contract,ViewModel,Screen}.kt
//     └── form/{Contract,ViewModel,Screen}.kt
//
// When the user asks for a new feature, produce something indistinguishable from
// CategoryFinance in shape. Any deviation from the rules below is a bug.
//
// ──────────────────────────────────────────────────────────────────────────────────────────
// HOW TO USE
//
//   1. Copy sections below into your feature module.
//   2. Find-and-replace "Example" → your feature name (e.g. "Invoice").
//   3. Split into files per the target layout.
//   4. Add your Koin module to app/.../core/di/ListModules.kt.
//
// Data-layer and VM/Screen code lives inside comment blocks because this file
// sits in :helpers:ui, which cannot import Ktor, Koin, or :helpers:network types.
// The code inside those blocks is production-shaped — copy verbatim.
//
// ──────────────────────────────────────────────────────────────────────────────────────────
// TARGET FILE LAYOUT
//
//   feature/<name>/
//   ├── data/
//   │   ├── model/<Name>.kt                   ← @Serializable domain model (doubles as DTO)
//   │   ├── request/<Name>Request.kt          ← outbound DTO, @Transient PickedImage
//   │   ├── <Name>Api.kt                      ← Ktor HttpClient, BaseResponse<T>
//   │   ├── <Name>Repository.kt               ← apiCall() wrappers, Flow<Resource<T>>
//   │   └── <name>Module.kt                   ← Koin module: Api + Repo + both VMs
//   └── presentation/
//       ├── <Name>Route.kt                    ← Crossfade wrapper (List ↔ Form)
//       ├── <Name>Strings.kt                  ← Indonesian strings (KMP-ready; no R.string)
//       ├── list/
//       │   ├── <Name>ListContract.kt         ← UiState / Event / Effect (sealed)
//       │   ├── <Name>ListViewModel.kt        ← plain ViewModel, single MutableStateFlow
//       │   └── <Name>ListScreen.kt           ← Route (stateful) + Screen (stateless)
//       └── form/
//           ├── <Name>FormContract.kt
//           ├── <Name>FormViewModel.kt        ← combine(fields, isLoading, isSubmitting)
//           └── <Name>FormScreen.kt
//
// Folder convention: subfolders only when ≥2 files will realistically live there.
//   model/, request/, list/, form/ → folders (grow over time).
//   Api, Repository, Module, Route, Strings → flat files at the parent level
//   (one-file-per-feature by design; subfolder = ceremony).
//
// ══════════════════════════════════════════════════════════════════════════════════════════
// NON-NEGOTIABLE RULES
//
// NETWORKING
//   ▸ Ktor HttpClient only. NEVER Retrofit. Use helper-network extensions:
//     `postMethod`, `getMethod`, `putMethod`, `deleteMethod`.
//   ▸ `postMethod` / `putMethod` accept a `pickedImage` parameter for multipart uploads.
//   ▸ API methods return BaseResponse<T> raw — no unwrapping in the Api layer.
//   ▸ Use `$v2` (from `com.zenenta.core.utils.constants.Constants`) as the URL prefix.
//
// REPOSITORY
//   ▸ No interface. Concrete class only.
//   ▸ Every method wraps an Api call with `apiCall(apiCall = { ... }, mapper = { it })`.
//   ▸ Return type is `Flow<Resource<T>>` — never `suspend fun T`. Never `LiveData`.
//   ▸ No try/catch. No caching. No SSOT. No Flow orchestration. No UseCase layer.
//
// DOMAIN MODEL vs DTO
//   ▸ One type only: `@Serializable data class <Name>(...)` in data/model/.
//   ▸ No separate `<Name>Dto` with `.toDomain()`. No `<Name>Draft`.
//   ▸ `<Name>Request` is separate — outbound body with `@Transient val pickedImage`.
//
// VIEWMODEL — GENERAL
//   ▸ Extend `FeatureViewModel<EFFECT>` (from `com.tisto.kmp.helper.network.base`).
//     It owns the effect channel and provides `sendEffect`, `showMessage`,
//     `showSuccess`, `showError`, and the `.launchResource(...)` extension.
//     NEVER use the older `BaseViewModel<STATE>` with `BaseUiState<T>` — that one
//     bundles pagination/scroll/search into UiState and is too heavy for MVI features.
//   ▸ Wire toast once per VM by overriding `showMessage`:
//         override fun showMessage(msg: String, type: MessageType) {
//             sendEffect(<Name>Effect.ShowMessage(msg, type))
//         }
//     After that call `showSuccess(...)` / `showError(...)` — no per-VM helper funs.
//   ▸ Single `onEvent(event)` entry point per VM.
//   ▸ Repo calls default to `.launchResource(fallbackError, onSuccess = { ... })`.
//     `launchResource` folds `viewModelScope.launch { flow.onResource(...) }` into
//     one call and routes errors to `showError` by default (pass `onError = { ... }`
//     when you need to mutate UiState directly, e.g. List `UiState.Error`).
//   ▸ Drop to raw `viewModelScope.launch { flow.onResource(...) }` ONLY when you
//     need extra bookkeeping around the call (e.g. Form's `performSubmission`
//     flipping `isSubmittingFlow` in try/finally). NEVER write
//     `.collect { when (resource) { Resource.Success -> ... } }` manually.
//     NEVER wrap an `onResource` call in your own try/catch.
//
// VIEWMODEL — LIST
//   ▸ Single `MutableStateFlow<UiState>`. Direct `.value =` assignment. No `combine()`.
//     No `flatMapLatest`. No derived LoadPhase.
//   ▸ `query` is a plain `private var` — only user events mutate it; reload is explicit.
//   ▸ On refresh: if current state is Success, set `isRefreshing = true`;
//     if Empty, keep Empty (so the search bar stays visible); else Loading.
//   ▸ Initial load: `init { refresh() }`.
//   ▸ Search payload: `SearchRequest(page = 1, perpage = 100, simpleQuery = buildList { ... })`
//     — always `add(Search("isActive", "true"))`; add `Search("name", query)` when non-blank.
//   ▸ Empty result list → `UiState.Empty(query = query)` (not `Success(items = emptyList())`).
//     `Empty` carries the current query so the UI can keep the search bar visible.
//   ▸ Search debounce: `QueryChanged` must NOT call `refresh()` directly.
//     Instead: update state.query immediately (so the text field never flickers),
//     cancel any pending `searchJob`, then schedule `refresh()` after 500 ms:
//         searchJob?.cancel()
//         searchJob = viewModelScope.launch { delay(500); refresh() }
//   ▸ `Refresh` (pull-to-refresh / back from form) calls `refresh()` directly — no debounce.
//
// VIEWMODEL — FORM
//   ▸ Takes constructor param `itemId: String?`. Null = Create, non-null = Edit.
//   ▸ Derive `mode: FormMode = if (itemId == null) Create else Edit(itemId)`.
//   ▸ Single `MutableStateFlow<FormUiState>` — same pattern as List VM. No `combine`,
//     no `stateIn`. Field values, `isLoading`, `isSubmitting`, per-field errors all
//     live on UiState. Mutate with `_uiState.update { copy(...) }`.
//   ▸ Inner `private data class Validation(...)` with `val hasErrors: Boolean` returned
//     from a pure `validate(state): Validation` function.
//   ▸ `performSubmission(action)` scaffold flips `isSubmitting` on/off via try/finally.
//     Use for submit AND delete. No catch block — onResource owns that.
//   ▸ Clearing field errors: on NameChanged, also set `nameError = null`.
//   ▸ Load existing in `init { if (itemId != null) loadExisting(itemId) }`. Toggle
//     `isLoading` in try/finally around the call.
//
// ROUTE / SCREEN SPLIT
//   ▸ Every screen has both. Route = stateful; Screen = pure
//     `(screenConfig, state, snackbar, onEvent, onBack) -> Unit`.
//   ▸ Route responsibilities:
//       – resolve VM via `safeKoinViewModel()` — NOT `koinViewModel()`. Always `?: return`.
//       – own `SnackbarHostState`.
//       – collect effects in a private `@Composable EffectHandler(...)`.
//       – wrap with `ZenentaTheme { screenConfig -> ... }` to receive the responsive config.
//       – pass `viewModel::onEvent` and `screenConfig` to Screen.
//   ▸ `ZenentaTheme` is a project-level Composable that resolves the current `ScreenConfig`
//     (mobile/tablet/desktop breakpoints) and injects it via its lambda parameter.
//     Never derive ScreenConfig inside Screen from `LocalConfiguration` — always receive it.
//   ▸ `Screen` always declares `screenConfig: ScreenConfig = ScreenConfig()` as first param
//     so it can be called in previews without a real theme wrapper.
//   ▸ List Route also takes `refreshToken: Int = 0` and does
//       `LaunchedEffect(refreshToken) { if (refreshToken > 0) viewModel.onEvent(Refresh) }`.
//   ▸ Form Route passes `itemId.toString()` into `safeKoinViewModel(itemId.toString())`
//     as the VM key, so Create ("null") and Edit ("<id>") get distinct instances.
//
// UI COMPONENTS (use these — never raw Material3 equivalents in feature screens)
//   ▸ `ToolbarRow(title, screenConfig, onBack, onAdd)` — toolbar with "Tambah" ButtonNormal.
//   ▸ `SearchFilterRow(screenConfig, searchQuery, onSearchChange)` — search + filter bar.
//   ▸ `ListColumn<T>(key, title, weight, cell)` — column descriptor for list/tablet tables.
//   ▸ `ListHeader(columns)` — tablet column header row.
//   ▸ `ListRow(item, columns, onClick)` — tablet data row with divider.
//   ▸ `ListMobileRow(imageUrl, text, secondary, onClick)` — mobile compact row.
//   ▸ `ListActions(onEdit, onDelete)` — standardized edit/delete icon buttons.
//   ▸ `DeleteConfirmationDialog(showDialog, onDismiss, onConfirm, itemName)` — delete confirm.
//   ▸ `FormContainer(forceTitle, screenConfig, item, onBack, onSave, onDelete)` — form wrapper
//     with back/save/delete wired; drop field composables into its trailing lambda.
//   ▸ `ScaffoldBox(snackbarHostState, isLoadingProcess)` — Scaffold + loading overlay.
//   ▸ `CustomTextField(value, onValueChange, hint, style, isError, supportingText)` — text input.
//   ▸ `SwitchCard(checked, onCheckedChange, text)` — labeled switch row.
//   ▸ `CardImagePicker(imageUrl, onPicker)` — image picker tile.
//   ▸ `BackHandler { onBack() }` — hardware back support; place at top of Screen.
//
// PREVIEWS
//   ▸ Use `@MobilePreview` and `@TabletPreview` (from `com.tisto.kmp.helper.ui.ext`).
//     Never use raw `@Preview(widthDp=..., heightDp=...)` in feature screens.
//   ▸ Wrap preview content in a private `ScreenContentPreview(screenConfig)` helper,
//     then call it from `@MobilePreview` and `@TabletPreview` functions.
//   ▸ List: one preview per target showing Success state with 2-3 sample items.
//   ▸ Form: one preview per target showing fully filled Edit mode state.
//   ▸ Place all @Preview functions at the bottom of the Screen file, `private`.
//
// EFFECT HANDLING
//   ▸ `ShowMessage` → `snackbar.showSnackbar(effect.message)`.
//   ▸ `NavigateToForm(id)` → `onNavigateToForm(effect.itemId)`.
//   ▸ `NavigateBack` → `onDone()` or equivalent caller.
//   ▸ EffectHandler is always `LaunchedEffect(Unit) { effects.collect { … } }`.
//
// WRAPPER ROUTE (top-level)
//   ▸ Crossfade over a `remember` (NOT rememberSaveable) `stage: Stage` sealed interface.
//   ▸ Stage is a non-Parcelable sealed interface — rememberSaveable crashes with
//     "MutableState containing List cannot be saved using the current SaveableStateRegistry"
//     because Bundle serialization fails on custom sealed types. `remember` is correct here:
//     navigation stage does not need to survive process death.
//   ▸ `refreshToken` via `remember { mutableIntStateOf(0) }` — increment on form `onDone`.
//   ▸ `private sealed interface Stage { data object List; data class Form(val id: String?) }`.
//   ▸ No nav library. No NavHost.
//
// KOIN MODULE
//   ▸ One module file per feature, declared `val <name>Module = module { ... }`.
//   ▸ `singleOf(::<Name>Api)`, `singleOf(::<Name>Repository)`.
//   ▸ `viewModelOf(::<Name>ListViewModel)`.
//   ▸ Form VM: `viewModel { params -> <Name>FormViewModel(repo = get(), itemId = params.getOrNull()) }`.
//   ▸ Register the module in `app/.../core/di/ListModules.kt`.
//
// STRINGS
//   ▸ User-facing copy is Indonesian: "Kembali", "Cari...", "Tambah", "Simpan",
//     "Menyimpan...", "Belum ada data", "Gagal memuat data", "Gagal menyimpan",
//     "Gagal menghapus", "Data tersimpan", "Data berhasil dihapus", "Nama wajib diisi".
//
// NAMING & PACKAGING
//   ▸ Feature package name is lowerCamelCase (e.g. `categoryNew`).
//   ▸ Types are PascalCase with feature prefix: `CategoryListViewModel`, `CategoryFormEvent`.
//   ▸ Koin module file is lowerCamelCase: `composeProductModule.kt`, `exampleModule.kt`.
//
// WHAT YOU MUST NOT DO
//   ✗ Do not introduce interfaces (`IExampleRepository`, `ExampleRepositoryImpl`).
//   ✗ Do not introduce a `UseCase` layer for passthrough CRUD.
//   ✗ Do not use `send()` on Channels — always `trySend`.
//   ✗ Do not put try/catch in the repository.
//   ✗ Do not write `.collect { when (resource) { Resource.Success/Error/Loading ... } }`
//     in ViewModels. Always use `.onResource(...)`.
//   ✗ Do not wrap a `.onResource(...)` call in your own try/catch.
//   ✗ Do not use `LiveData`, `asLiveData()`, or any XML/legacy paths.
//   ✗ Do not combine flows in either VM — List and Form both use a single
//     MutableStateFlow<UiState>. No `combine`, no `stateIn`, no separate
//     `fieldsFlow` / `isLoadingFlow` / `isSubmittingFlow`.
//   ✗ Do not use `koinViewModel()` — always `safeKoinViewModel()`.
//   ✗ Do not split Example model into `<Name>Dto` + `<Name>` + `toDomain()`.
//   ✗ Do not extend the legacy `BaseViewModel<STATE>` — use `FeatureViewModel<EFFECT>`.
//   ✗ Do not re-declare `effectChannel`, `effect`, `showMessage/Success/Error` in
//     the feature VM — all are provided by `FeatureViewModel`.
//   ✗ Do not use raw `OutlinedTextField` in forms — use `CustomTextField`.
//   ✗ Do not use raw `Switch` in a `Row` — use `SwitchCard`.
//   ✗ Do not use `Scaffold` + `TopAppBar` + `FloatingActionButton` in list/form screens —
//     use `ToolbarRow` + `ScaffoldBox` + `FormContainer`.
//   ✗ Do not derive `ScreenConfig` from `LocalConfiguration` inside Screen —
//     always receive it as a parameter from `ZenentaTheme { screenConfig -> }` in Route.
//   ✗ Do not use raw `@Preview(widthDp=..., heightDp=...)` — use `@MobilePreview` / `@TabletPreview`.
// ══════════════════════════════════════════════════════════════════════════════════════════

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
import kotlinx.serialization.Serializable

// ══════════════════════════════════════════════════════════════════════════════════════════
// SHARED UI HELPERS — used by both list and form screens across features.
// ══════════════════════════════════════════════════════════════════════════════════════════

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

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 1 — DOMAIN MODEL  (data/model/Example.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Domain model doubles as the wire DTO. Default values + nullability so partial
// JSON responses parse cleanly.

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

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 2 — REQUEST DTO  (data/request/ExampleRequest.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import com.tisto.kmp.helper.utils.model.PickedImage
// import kotlinx.serialization.Serializable
// import kotlinx.serialization.Transient
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

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 3 — API SERVICE  (data/ExampleApi.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import com.tisto.kmp.helper.network.model.BaseResponse
// import com.tisto.kmp.helper.network.model.SearchRequest
// import com.tisto.kmp.helper.network.model.convertToQuery
// import com.tisto.kmp.helper.network.utils.deleteMethod
// import com.tisto.kmp.helper.network.utils.getMethod
// import com.tisto.kmp.helper.network.utils.postMethod
// import com.tisto.kmp.helper.network.utils.putMethod
// import com.zenenta.core.utils.constants.Constants.v2
// import io.ktor.client.HttpClient
//
// class ExampleApi(private val client: HttpClient) {
//
//     suspend fun create(body: ExampleRequest?): BaseResponse<Example> = client.postMethod(
//         url = "$v2/example",
//         body = body,
//         pickedImage = body?.pickedImage,
//     )
//
//     suspend fun get(search: SearchRequest? = null): BaseResponse<List<Example>> = client.getMethod(
//         url = "$v2/example",
//         query = search?.convertToQuery(),
//     )
//
//     suspend fun getOne(id: String): BaseResponse<Example> = client.getMethod(
//         url = "$v2/example/$id",
//     )
//
//     suspend fun update(body: ExampleRequest?): BaseResponse<Example> = client.putMethod(
//         url = "$v2/example/${body?.id}",
//         body = body,
//         pickedImage = body?.pickedImage,
//     )
//
//     suspend fun delete(id: String): BaseResponse<Example> = client.deleteMethod(
//         url = "$v2/example/$id",
//     )
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 4 — REPOSITORY  (data/ExampleRepository.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import com.tisto.kmp.helper.network.model.SearchRequest
// import com.tisto.kmp.helper.network.utils.apiCall
//
// class ExampleRepository(private val api: ExampleApi) {
//
//     fun create(body: ExampleRequest?) = apiCall(
//         apiCall = { api.create(body) },
//         mapper  = { it },
//     )
//
//     fun get(search: SearchRequest? = null) = apiCall(
//         apiCall = { api.get(search) },
//         mapper  = { it },
//     )
//
//     fun getOne(id: String) = apiCall(
//         apiCall = { api.getOne(id) },
//         mapper  = { it },
//     )
//
//     fun update(body: ExampleRequest?) = apiCall(
//         apiCall = { api.update(body) },
//         mapper  = { it },
//     )
//
//     fun delete(id: String) = apiCall(
//         apiCall = { api.delete(id) },
//         mapper  = { it },
//     )
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 5 — KOIN MODULE  (data/exampleModule.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import org.koin.core.module.dsl.singleOf
// import org.koin.core.module.dsl.viewModelOf
// import org.koin.dsl.module
//
// val exampleModule = module {
//     singleOf(::ExampleApi)
//     singleOf(::ExampleRepository)
//     viewModelOf(::ExampleListViewModel)
//     viewModel { params -> ExampleFormViewModel(repo = get(), itemId = params.getOrNull()) }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 6 — LIST CONTRACT  (presentation/list/ExampleListContract.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════

sealed interface ExampleListUiState {
    data object Loading : ExampleListUiState
    data class Empty(val query: String = "") : ExampleListUiState  // carries query so the search bar stays visible
    data class Success(
        val items: List<Example>,
        val query: String = "",
        val isRefreshing: Boolean = false,
    ) : ExampleListUiState
    data class Error(val message: String) : ExampleListUiState
}

sealed interface ExampleListEvent {
    data object Refresh : ExampleListEvent
    data class QueryChanged(val query: String) : ExampleListEvent
    data object CreateClicked : ExampleListEvent
    data class EditClicked(val id: String) : ExampleListEvent
    data class DeleteConfirmed(val id: String) : ExampleListEvent
}

sealed interface ExampleListEffect {
    data class ShowMessage(val message: String, val type: MessageType = MessageType.Info) : ExampleListEffect
    data class NavigateToForm(val itemId: String?) : ExampleListEffect
}

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 7 — LIST VIEWMODEL  (presentation/list/ExampleListViewModel.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import androidx.lifecycle.viewModelScope
// import com.tisto.kmp.helper.network.base.FeatureViewModel
// import com.tisto.kmp.helper.network.model.Search
// import com.tisto.kmp.helper.network.model.SearchRequest
// import com.tisto.kmp.helper.network.MessageType
// import kotlinx.coroutines.Job
// import kotlinx.coroutines.delay
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.launch
//
// class ExampleListViewModel(
//     private val repo: ExampleRepository,
// ) : FeatureViewModel<ExampleListEffect>() {
//
//     private val _uiState = MutableStateFlow<ExampleListUiState>(ExampleListUiState.Loading)
//     val uiState: StateFlow<ExampleListUiState> = _uiState.asStateFlow()
//
//     override fun showMessage(msg: String, type: MessageType) {
//         sendEffect(ExampleListEffect.ShowMessage(msg, type))
//     }
//
//     private var query = ""
//     private var searchJob: Job? = null
//
//     init { refresh() }
//
//     fun onEvent(event: ExampleListEvent) {
//         when (event) {
//             ExampleListEvent.Refresh -> refresh()
//             is ExampleListEvent.QueryChanged -> {
//                 query = event.query
//                 // Reflect new query in state immediately so the text field never flickers
//                 val current = _uiState.value
//                 if (current is ExampleListUiState.Success) {
//                     _uiState.value = current.copy(query = event.query)
//                 }
//                 // Debounce: cancel the previous pending search and wait 500 ms
//                 searchJob?.cancel()
//                 searchJob = viewModelScope.launch { delay(500); refresh() }
//             }
//             ExampleListEvent.CreateClicked -> sendEffect(ExampleListEffect.NavigateToForm(null))
//             is ExampleListEvent.EditClicked -> sendEffect(ExampleListEffect.NavigateToForm(event.id))
//             is ExampleListEvent.DeleteConfirmed -> delete(event.id)
//         }
//     }
//
//     private fun refresh() {
//         val current = _uiState.value
//         _uiState.value = when (current) {
//             is ExampleListUiState.Success -> current.copy(isRefreshing = true)
//             is ExampleListUiState.Empty -> current  // keep search bar visible while loading
//             else -> ExampleListUiState.Loading
//         }
//
//         val search = SearchRequest(
//             page = 1,
//             perpage = 100,
//             simpleQuery = buildList {
//                 add(Search("isActive", "true"))
//                 if (query.isNotBlank()) add(Search("name", query))
//             },
//         )
//         repo.get(search).launchResource(
//             fallbackError = "Gagal memuat data",
//             onError = { _uiState.value = ExampleListUiState.Error(it) }, // override to set UiState.Error
//         ) { items ->
//             // Pass query into Empty so the UI can keep the search bar visible
//             _uiState.value = if (items.isEmpty()) ExampleListUiState.Empty(query = query)
//                 else ExampleListUiState.Success(items = items, query = query)
//         }
//     }
//
//     private fun delete(id: String) {
//         repo.delete(id).launchResource(fallbackError = "Gagal menghapus") {
//             showSuccess("Data berhasil dihapus"); refresh()
//         }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 8 — LIST SCREEN  (presentation/list/ExampleListScreen.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import androidx.compose.foundation.layout.*
// import androidx.compose.foundation.lazy.LazyColumn
// import androidx.compose.foundation.lazy.items
// import androidx.compose.material3.SnackbarHostState
// import androidx.compose.runtime.*
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import com.tisto.kmp.helper.ui.component.CustomImageView
// import com.tisto.kmp.helper.ui.component.DeleteConfirmationDialog
// import com.tisto.kmp.helper.ui.component.ListActions
// import com.tisto.kmp.helper.ui.component.ListColumn
// import com.tisto.kmp.helper.ui.component.ListHeader
// import com.tisto.kmp.helper.ui.component.ListMobileRow
// import com.tisto.kmp.helper.ui.component.ListRow
// import com.tisto.kmp.helper.ui.component.RowText
// import com.tisto.kmp.helper.ui.component.SearchFilterRow
// import com.tisto.kmp.helper.ui.component.ToolbarRow
// import com.tisto.kmp.helper.ui.ext.MobilePreview
// import com.tisto.kmp.helper.ui.ext.ScreenConfig
// import com.tisto.kmp.helper.ui.ext.TabletPreview
// import com.tisto.kmp.helper.ui.ext.safeKoinViewModel
// import com.tisto.kmp.helper.utils.ext.def
// import com.tisto.kmp.helper.utils.ext.formatDate
// import com.zenenta.pos.core.ui.ui.theme.ZenentaTheme
// import kotlinx.coroutines.flow.Flow
//
// @Composable
// fun ExampleListRoute(
//     onNavigateToForm: (itemId: String?) -> Unit,
//     onBack: () -> Unit,
//     refreshToken: Int = 0,
// ) {
//     val viewModel: ExampleListViewModel = safeKoinViewModel() ?: return
//     val state by viewModel.uiState.collectAsStateWithLifecycle()
//     val snackbar = remember { SnackbarHostState() }
//
//     ExampleListEffectHandler(viewModel.effect, snackbar, onNavigateToForm)
//
//     LaunchedEffect(refreshToken) {
//         if (refreshToken > 0) viewModel.onEvent(ExampleListEvent.Refresh)
//     }
//
//     ZenentaTheme { screenConfig ->
//         ExampleListScreen(
//             screenConfig = screenConfig,
//             state = state,
//             snackbar = snackbar,
//             onEvent = viewModel::onEvent,
//             onBack = onBack,
//         )
//     }
// }
//
// @Composable
// private fun ExampleListEffectHandler(
//     effects: Flow<ExampleListEffect>,
//     snackbar: SnackbarHostState,
//     onNavigateToForm: (String?) -> Unit,
// ) {
//     LaunchedEffect(Unit) {
//         effects.collect { effect ->
//             when (effect) {
//                 is ExampleListEffect.ShowMessage -> snackbar.showSnackbar(effect.message)
//                 is ExampleListEffect.NavigateToForm -> onNavigateToForm(effect.itemId)
//             }
//         }
//     }
// }
//
// @Composable
// fun ExampleListScreen(
//     screenConfig: ScreenConfig = ScreenConfig(),
//     state: ExampleListUiState,
//     snackbar: SnackbarHostState,
//     onEvent: (ExampleListEvent) -> Unit,
//     onBack: () -> Unit,
// ) {
//     var pendingDeleteItem by remember { mutableStateOf<Example?>(null) }
//
//     ScaffoldBox(
//         topBar = {
//             ToolbarRow(
//                 screenConfig = screenConfig,
//                 title = "Example",
//                 onBack = onBack,
//                 onAdd = { onEvent(ExampleListEvent.CreateClicked) },
//             )
//         },
//         snackbarHostState = snackbar,
//     ) { padding ->
//         when (state) {
//             ExampleListUiState.Loading -> CenteredLoader(padding)
//             is ExampleListUiState.Error -> CenteredMessage(padding, state.message)
//             is ExampleListUiState.Empty -> ExampleEmptyBody(padding, state.query, screenConfig, onEvent)
//             is ExampleListUiState.Success -> ExampleListBody(
//                 padding = padding,
//                 state = state,
//                 screenConfig = screenConfig,
//                 onEvent = onEvent,
//                 onRequestDelete = { item -> pendingDeleteItem = item },
//             )
//         }
//     }
//
//     DeleteConfirmationDialog(
//         showDialog = pendingDeleteItem != null,
//         onDismiss = { pendingDeleteItem = null },
//         onConfirm = {
//             pendingDeleteItem?.let { onEvent(ExampleListEvent.DeleteConfirmed(it.id)) }
//             pendingDeleteItem = null
//         },
//         itemName = pendingDeleteItem?.name.orEmpty(),
//     )
// }
//
// @Composable
// private fun ExampleEmptyBody(
//     padding: PaddingValues,
//     query: String,
//     screenConfig: ScreenConfig,
//     onEvent: (ExampleListEvent) -> Unit,
// ) {
//     Column(modifier = Modifier.fillMaxSize().padding(padding)) {
//         Spacer(Modifier.height(Spacing.small))
//         SearchFilterRow(
//             modifier = Modifier.padding(horizontal = Spacing.normal, Spacing.box),
//             screenConfig = screenConfig,
//             searchQuery = query,
//             onSearchChange = { onEvent(ExampleListEvent.QueryChanged(it)) },
//         )
//         Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//             Text("Belum ada data")
//         }
//     }
// }
//
// @Composable
// private fun ExampleListBody(
//     padding: PaddingValues,
//     state: ExampleListUiState.Success,
//     screenConfig: ScreenConfig,
//     onEvent: (ExampleListEvent) -> Unit,
//     onRequestDelete: (Example) -> Unit,
// ) {
//     val columns = remember(screenConfig.isTablet) {
//         exampleColumns(
//             onEdit = { item -> onEvent(ExampleListEvent.EditClicked(item.id)) },
//             onDelete = onRequestDelete,
//         )
//     }
//
//     Column(modifier = Modifier.fillMaxSize().padding(padding)) {
//         Spacer(Modifier.height(Spacing.small))
//         SearchFilterRow(
//             modifier = Modifier.padding(horizontal = Spacing.normal, Spacing.box),
//             screenConfig = screenConfig,
//             searchQuery = state.query,
//             onSearchChange = { onEvent(ExampleListEvent.QueryChanged(it)) },
//         )
//         if (screenConfig.isTablet) {
//             ListHeader(columns = columns, modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.normal))
//         }
//         LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.normal)) {
//             items(state.items, key = { it.id }) { item ->
//                 if (screenConfig.isMobile) {
//                     ListMobileRow(
//                         imageUrl = item.image,
//                         text = item.name.def(),
//                         secondary = item.description?.takeIf { it.isNotBlank() },
//                         onClick = { onEvent(ExampleListEvent.EditClicked(item.id)) },
//                     )
//                 } else {
//                     ListRow(item = item, columns = columns, onClick = { onEvent(ExampleListEvent.EditClicked(item.id)) })
//                 }
//             }
//         }
//     }
// }
//
// private fun exampleColumns(
//     onEdit: (Example) -> Unit,
//     onDelete: (Example) -> Unit,
// ): List<ListColumn<Example>> = buildList {
//     add(ListColumn(
//         key = "name", title = "Nama", weight = 3f,
//         cell = { item ->
//             Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
//                 CustomImageView(imageUrl = item.image, size = 50.dp)
//                 Spacer(Modifier.width(12.dp))
//                 RowText(modifier = Modifier.weight(1f), text = item.name.def(), secondary = item.description?.takeIf { it.isNotBlank() })
//             }
//         },
//     ))
//     add(ListColumn(
//         key = "updated", title = "Update", weight = 1f,
//         cell = { item ->
//             RowText(
//                 modifier = Modifier.fillMaxWidth(),
//                 text = item.updatedAt?.formatDate("dd MMM yyyy").def("-"),
//                 secondary = item.updatedAt?.formatDate("HH:mm"),
//             )
//         },
//     ))
//     add(ListColumn(
//         key = "action", title = "", weight = 0.3f,
//         cell = { item -> ListActions(onEdit = { onEdit(item) }, onDelete = { onDelete(item) }) },
//     ))
// }
//
// // ── Previews ──────────────────────────────────────────────────────────────
//
// @Composable
// private fun ExampleListScreenPreview(screenConfig: ScreenConfig = ScreenConfig()) {
//     ZenentaTheme {
//         ExampleListScreen(
//             screenConfig = screenConfig,
//             state = ExampleListUiState.Success(
//                 items = listOf(
//                     Example(id = "1", name = "Item Satu", updatedAt = "2025-12-22T04:12:09.000Z"),
//                     Example(id = "2", name = "Item Dua", updatedAt = "2025-12-22T04:12:09.000Z"),
//                     Example(id = "3", name = "Item Tiga", updatedAt = "2025-12-22T04:12:09.000Z"),
//                 ),
//             ),
//             snackbar = SnackbarHostState(),
//             onEvent = {},
//             onBack = {},
//         )
//     }
// }
//
// @MobilePreview
// @Composable
// private fun PreviewExampleList() { ExampleListScreenPreview() }
//
// @TabletPreview
// @Composable
// private fun TabletPreviewExampleList() { ExampleListScreenPreview(ScreenConfig(700.dp)) }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 9 — FORM CONTRACT  (presentation/form/ExampleFormContract.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Flat data-class UiState (not sealed). Forms have many small independent
// fields — a sealed hierarchy would explode into near-identical states.
//
// import com.tisto.kmp.helper.network.MessageType
// import com.tisto.kmp.helper.utils.model.PickedImage
//
// sealed interface ExampleFormMode {
//     data object Create : ExampleFormMode
//     data class Edit(val itemId: String) : ExampleFormMode
// }
//
// data class ExampleFormUiState(
//     val mode: ExampleFormMode = ExampleFormMode.Create,
//     val name: String = "",
//     val description: String = "",
//     val image: String? = null,
//     val pickedImage: PickedImage? = null,
//     val isActive: Boolean = true,
//     val nameError: String? = null,
//     val isLoading: Boolean = false,
//     val isSubmitting: Boolean = false,
// ) {
//     val canSubmit: Boolean
//         get() = !isSubmitting && !isLoading && name.isNotBlank()
// }
//
// sealed interface ExampleFormEvent {
//     data class NameChanged(val value: String) : ExampleFormEvent
//     data class DescriptionChanged(val value: String) : ExampleFormEvent
//     data class ImagePicked(val image: PickedImage?) : ExampleFormEvent
//     data class ActiveChanged(val value: Boolean) : ExampleFormEvent
//     data object Submit : ExampleFormEvent
//     data object Delete : ExampleFormEvent
// }
//
// sealed interface ExampleFormEffect {
//     data class ShowMessage(val message: String, val type: MessageType = MessageType.Info) : ExampleFormEffect
//     data object NavigateBack : ExampleFormEffect
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 10 — FORM VIEWMODEL  (presentation/form/ExampleFormViewModel.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Single MutableStateFlow<UiState> — same pattern as List VM. `performSubmission`
// and `loadExisting` use raw `viewModelScope.launch` because they need try/finally
// around `isSubmitting` / `isLoading`. That's the ONE pattern that stays manual;
// everything else is a one-liner via `.launchResource { }`.
//
// import androidx.lifecycle.viewModelScope
// import com.tisto.kmp.helper.network.base.FeatureViewModel
// import com.tisto.kmp.helper.network.utils.onResource
// import com.tisto.kmp.helper.network.MessageType
// import com.tisto.kmp.helper.utils.ext.def
// import com.tisto.kmp.helper.utils.model.PickedImage
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.flow.update
// import kotlinx.coroutines.launch
//
// class ExampleFormViewModel(
//     private val repo: ExampleRepository,
//     private val itemId: String?,
// ) : FeatureViewModel<ExampleFormEffect>() {
//
//     private val mode: ExampleFormMode =
//         if (itemId == null) ExampleFormMode.Create else ExampleFormMode.Edit(itemId)
//
//     private val _uiState = MutableStateFlow(ExampleFormUiState(mode = mode))
//     val uiState: StateFlow<ExampleFormUiState> = _uiState.asStateFlow()
//
//     override fun showMessage(msg: String, type: MessageType) {
//         sendEffect(ExampleFormEffect.ShowMessage(msg, type))
//     }
//
//     init { if (itemId != null) loadExisting(itemId) }
//
//     fun onEvent(event: ExampleFormEvent) {
//         when (event) {
//             is ExampleFormEvent.NameChanged ->
//                 _uiState.update { it.copy(name = event.value, nameError = null) }
//             is ExampleFormEvent.DescriptionChanged ->
//                 _uiState.update { it.copy(description = event.value) }
//             is ExampleFormEvent.ImagePicked ->
//                 _uiState.update { it.copy(pickedImage = event.image) }
//             is ExampleFormEvent.ActiveChanged ->
//                 _uiState.update { it.copy(isActive = event.value) }
//             ExampleFormEvent.Submit -> submit()
//             ExampleFormEvent.Delete -> delete()
//         }
//     }
//
//     private fun loadExisting(id: String) {
//         viewModelScope.launch {
//             _uiState.update { it.copy(isLoading = true) }
//             try {
//                 repo.getOne(id).onResource(fallbackError = "Gagal memuat data", onError = ::showError) { item ->
//                     _uiState.update {
//                         it.copy(
//                             name = item.name.def(),
//                             description = item.description.def(),
//                             image = item.image,
//                             isActive = item.isActive,
//                         )
//                     }
//                 }
//             } finally { _uiState.update { it.copy(isLoading = false) } }
//         }
//     }
//
//     private fun submit() {
//         val state = _uiState.value
//         val validation = validate(state)
//         if (validation.hasErrors) {
//             _uiState.update { it.copy(nameError = validation.name) }
//             return
//         }
//         val request = ExampleRequest(
//             id = (mode as? ExampleFormMode.Edit)?.itemId,
//             name = state.name.trim(),
//             description = state.description.trim().ifBlank { null },
//             image = state.image,
//             isActive = state.isActive,
//             pickedImage = state.pickedImage,
//         )
//         performSubmission {
//             val flow = if (mode is ExampleFormMode.Create) repo.create(request) else repo.update(request)
//             flow.onResource(fallbackError = "Gagal menyimpan", onError = ::showError) {
//                 showSuccess("Data tersimpan")
//                 sendEffect(ExampleFormEffect.NavigateBack)
//             }
//         }
//     }
//
//     private fun delete() {
//         val editMode = mode as? ExampleFormMode.Edit ?: return
//         performSubmission {
//             repo.delete(editMode.itemId).onResource(fallbackError = "Gagal menghapus", onError = ::showError) {
//                 showSuccess("Data berhasil dihapus")
//                 sendEffect(ExampleFormEffect.NavigateBack)
//             }
//         }
//     }
//
//     private fun performSubmission(action: suspend () -> Unit) {
//         viewModelScope.launch {
//             _uiState.update { it.copy(isSubmitting = true) }
//             try { action() } finally { _uiState.update { it.copy(isSubmitting = false) } }
//         }
//     }
//
//     private fun validate(state: ExampleFormUiState): Validation = Validation(
//         name = if (state.name.isBlank()) "Nama wajib diisi" else null,
//     )
//
//     private data class Validation(val name: String?) {
//         val hasErrors: Boolean = listOf(name).any { it != null }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 11 — FORM SCREEN  (presentation/form/ExampleFormScreen.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import androidx.compose.foundation.layout.*
// import androidx.compose.material3.CircularProgressIndicator
// import androidx.compose.material3.SnackbarHostState
// import androidx.compose.runtime.*
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import com.tisto.kmp.helper.network.MessageType
// import com.tisto.kmp.helper.ui.component.AppSnackbarVisuals
// import com.tisto.kmp.helper.ui.component.BackHandler
// import com.tisto.kmp.helper.ui.component.CardImagePicker
// import com.tisto.kmp.helper.ui.component.CustomTextField
// import com.tisto.kmp.helper.ui.component.FormContainer
// import com.tisto.kmp.helper.ui.component.ScaffoldBox
// import com.tisto.kmp.helper.ui.component.SwitchCard
// import com.tisto.kmp.helper.ui.component.TextFieldStyle
// import com.tisto.kmp.helper.ui.ext.MobilePreview
// import com.tisto.kmp.helper.ui.ext.ScreenConfig
// import com.tisto.kmp.helper.ui.ext.TabletPreview
// import com.tisto.kmp.helper.ui.ext.safeKoinViewModel
// import com.tisto.kmp.helper.ui.theme.Spacing
// import com.tisto.kmp.helper.utils.SnackbarType
// import com.zenenta.pos.core.ui.ui.theme.ZenentaTheme
// import kotlinx.coroutines.flow.Flow
//
// @Composable
// fun ExampleFormRoute(
//     itemId: String?,
//     onDone: () -> Unit,
// ) {
//     val viewModel: ExampleFormViewModel = safeKoinViewModel(itemId.toString()) ?: return
//     val state by viewModel.uiState.collectAsStateWithLifecycle()
//     val snackbar = remember { SnackbarHostState() }
//
//     ExampleFormEffectHandler(viewModel.effect, snackbar, onDone)
//
//     ZenentaTheme { screenConfig ->
//         ExampleFormScreen(
//             state = state,
//             snackbar = snackbar,
//             screenConfig = screenConfig,
//             onEvent = viewModel::onEvent,
//             onBack = onDone,
//         )
//     }
// }
//
// @Composable
// private fun ExampleFormEffectHandler(
//     effects: Flow<ExampleFormEffect>,
//     snackbar: SnackbarHostState,
//     onDone: () -> Unit,
// ) {
//     LaunchedEffect(Unit) {
//         effects.collect { effect ->
//             when (effect) {
//                 is ExampleFormEffect.ShowMessage -> snackbar.showSnackbar(
//                     AppSnackbarVisuals(
//                         message = effect.message,
//                         type = when (effect.type) {
//                             MessageType.Success -> SnackbarType.SUCCESS
//                             MessageType.Error   -> SnackbarType.ERROR
//                             MessageType.Warning -> SnackbarType.WARNING
//                             MessageType.Info    -> SnackbarType.INFO
//                         }
//                     )
//                 )
//                 ExampleFormEffect.NavigateBack -> onDone()
//             }
//         }
//     }
// }
//
// @Composable
// fun ExampleFormScreen(
//     state: ExampleFormUiState,
//     snackbar: SnackbarHostState,
//     screenConfig: ScreenConfig = ScreenConfig(),
//     onEvent: (ExampleFormEvent) -> Unit,
//     onBack: () -> Unit,
// ) {
//     BackHandler { onBack() }
//
//     ScaffoldBox(
//         snackbarHostState = snackbar,
//         isLoadingProcess = state.isSubmitting,
//     ) { _ ->
//         FormContainer(
//             forceTitle = if (state.mode is ExampleFormMode.Edit) "Edit Example" else "Tambah Example",
//             screenConfig = screenConfig,
//             item = (state.mode as? ExampleFormMode.Edit)?.itemId,
//             selectedItemName = state.name,
//             isFormValid = state.canSubmit,
//             isLoadingProcess = state.isSubmitting,
//             onBack = onBack,
//             onSave = { onEvent(ExampleFormEvent.Submit) },
//             onDelete = { onEvent(ExampleFormEvent.Delete) },
//             horizontalAlignment = Alignment.CenterHorizontally,
//         ) {
//             if (state.isLoading) {
//                 Spacer(Modifier.height(Spacing.large))
//                 CircularProgressIndicator()
//             } else {
//                 Spacer(Modifier.height(if (screenConfig.isMobile) Spacing.normal else Spacing.large))
//
//                 CardImagePicker(
//                     modifier = Modifier.width(120.dp).height(120.dp),
//                     imageUrl = state.imageUrl,
//                     onPicker = { onEvent(ExampleFormEvent.ImagePicked(it)) },
//                 )
//
//                 Spacer(Modifier.height(Spacing.large))
//
//                 CustomTextField(
//                     value = state.name,
//                     onValueChange = { onEvent(ExampleFormEvent.NameChanged(it)) },
//                     hint = "Nama",
//                     style = TextFieldStyle.OUTLINED,
//                     strokeWidth = 1.dp,
//                     isError = state.nameError != null,
//                     supportingText = state.nameError,
//                     modifier = Modifier.fillMaxWidth(),
//                     singleLine = true,
//                 )
//
//                 Spacer(Modifier.height(Spacing.normal))
//
//                 CustomTextField(
//                     value = state.description,
//                     onValueChange = { onEvent(ExampleFormEvent.DescriptionChanged(it)) },
//                     hint = "Deskripsi",
//                     style = TextFieldStyle.OUTLINED,
//                     strokeWidth = 1.dp,
//                     modifier = Modifier.fillMaxWidth(),
//                 )
//
//                 Spacer(Modifier.height(Spacing.normal))
//
//                 SwitchCard(
//                     checked = state.isActive,
//                     onCheckedChange = { onEvent(ExampleFormEvent.ActiveChanged(it)) },
//                     text = "Aktif",
//                 )
//             }
//         }
//     }
// }
//
// // ── Previews ──────────────────────────────────────────────────────────────
//
// @Composable
// private fun ExampleFormScreenPreview(screenConfig: ScreenConfig = ScreenConfig()) {
//     ZenentaTheme {
//         ExampleFormScreen(
//             state = ExampleFormUiState(
//                 mode = ExampleFormMode.Edit("1"),
//                 name = "Contoh Item",
//                 description = "Deskripsi contoh item",
//                 isActive = true,
//             ),
//             snackbar = SnackbarHostState(),
//             screenConfig = screenConfig,
//             onEvent = {},
//             onBack = {},
//         )
//     }
// }
//
// @MobilePreview
// @Composable
// private fun PreviewExampleForm() { ExampleFormScreenPreview() }
//
// @TabletPreview
// @Composable
// private fun TabletPreviewExampleForm() { ExampleFormScreenPreview(ScreenConfig(840.dp)) }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 12 — WRAPPER ROUTE  (presentation/ExampleRoute.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import androidx.compose.animation.Crossfade
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableIntStateOf
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.setValue
//
// @Composable
// fun ExampleRoute(onBack: () -> Unit = {}) {
//     var stage by remember { mutableStateOf<Stage>(Stage.List) } // NOT rememberSaveable — Stage is not Parcelable
//     var refreshToken by remember { mutableIntStateOf(0) }
//
//     Crossfade(targetState = stage) { current ->
//         when (current) {
//             Stage.List -> ExampleListRoute(
//                 onNavigateToForm = { id -> stage = Stage.Form(id) },
//                 onBack = onBack,
//                 refreshToken = refreshToken,
//             )
//             is Stage.Form -> ExampleFormRoute(
//                 itemId = current.id,
//                 onDone = { refreshToken++; stage = Stage.List },
//             )
//         }
//     }
// }
//
// private sealed interface Stage {
//     data object List : Stage
//     data class Form(val id: String?) : Stage
// }
