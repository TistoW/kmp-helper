@file:Suppress("unused", "RedundantVisibilityModifier")

package com.tisto.kmp.helper.ui.boilerplate

// ══════════════════════════════════════════════════════════════════════════════════════════
// MVI / UDF BOILERPLATE — The rulebook + full concrete reference for new features.
//
// READ THIS FIRST. This single file holds everything: the non-negotiable rules,
// then 12 copy-ready sections with "Example" placeholder names. But don't study
// only this file — open the two canonical implementations below and read them
// side by side. They are the living proof that the rules work.
//
// ──────────────────────────────────────────────────────────────────────────────────────────
// ★ REFERENCE IMPLEMENTATION 1 — Category (basic CRUD + image + picker)
//
//   Base path: kmp/feature/product/src/main/java/com/zenenta/pos/product/category/
//
//   data/
//     model/Category.kt               ← @Serializable, image field, isActive flag
//     request/CategoryRequest.kt      ← @Transient pickedImage for multipart upload
//     CategoryApi.kt                  ← Ktor HttpClient, postMethod with pickedImage
//     CategoryRepository.kt           ← apiCall() wrappers, Flow<Resource<T>>
//     categoryModule.kt               ← Koin: singleOf Api/Repo, viewModelOf ListVM, viewModel { params -> FormVM }
//   presentation/
//     CategoryRoute.kt                ← Crossfade wrapper with onPick support
//     CategoryStrings.kt              ← Indonesian strings including titlePicker, search, columnUpdate
//     list/
//       CategoryListContract.kt       ← Effect only — UiState/Event are generic ListUiState<T>/ListEvent<T>
//       CategoryListViewModel.kt      ← ListUiState<Category>, ListEvent<Category>, isActive default filter
//       CategoryListScreen.kt         ← ListContainer with image column, picker support, filter by status
//     form/
//       CategoryFormContract.kt       ← Flat UiState with image/pickedImage/isActive
//       CategoryFormViewModel.kt      ← initialItem + refreshInBackground pattern
//       CategoryFormScreen.kt         ← ScaffoldBox + FormContainer, CardImagePicker, SwitchCard
//
//   Use Category as your starting point when the feature has:
//     • image upload, isActive toggle, picker mode, or a straightforward text+image form
//
// ──────────────────────────────────────────────────────────────────────────────────────────
// ★ REFERENCE IMPLEMENTATION 2 — Discount (numeric fields + type chips + CurrencyTextField)
//
//   Base path: kmp/feature/product/src/main/java/com/zenenta/pos/product/discount/
//
//   data/
//     model/Discount.kt               ← @Serializable, type field (percent/nominal), numeric fields
//     request/DiscountRequest.kt       ← No image, numeric/type fields
//     DiscountApi.kt                   ← Ktor HttpClient, standard CRUD
//     DiscountRepository.kt            ← apiCall() wrappers
//     discountModule.kt                ← Koin module
//   presentation/
//     DiscountRoute.kt                 ← Crossfade wrapper with onPick
//     DiscountStrings.kt               ← Includes type labels, label texts, range errors
//     list/
//       DiscountListContract.kt        ← Effect only
//       DiscountListViewModel.kt       ← ListUiState<Discount>, filter by type
//       DiscountListScreen.kt          ← ListContainer, columns show formatted type/value
//     form/
//       DiscountFormContract.kt        ← Flat UiState with type, conditional validation (percent range)
//       DiscountFormViewModel.kt       ← initialItem pattern, conditional field logic per type
//       DiscountFormScreen.kt          ← FilterChip type selector, CurrencyTextField with prefix/suffix
//
//   Use Discount as your starting point when the feature has:
//     • numeric/currency fields, type selection chips, conditional form fields,
//       percentage vs nominal logic, or no image upload
//
// ──────────────────────────────────────────────────────────────────────────────────────────
// Also shipped: Voucher, Variant — same patterns, same module (:kmp:feature:product).
//
// When the user asks for a new feature, produce something indistinguishable from
// Category/Discount in shape. Any deviation from the rules below is a bug.
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
//       │   ├── <Name>ListContract.kt         ← Effect only (UiState + Event are generic)
//       │   ├── <Name>ListViewModel.kt        ← plain ViewModel, single MutableStateFlow
//       │   └── <Name>ListScreen.kt           ← Route (stateful) + Screen (stateless)
//       └── form/
//           ├── <Name>FormContract.kt
//           ├── <Name>FormViewModel.kt        ← single MutableStateFlow, no combine
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
// VIEWMODEL — LIST (uses generic ListUiState<T> + ListEvent<T>)
//   ▸ Import `ListUiState` and `ListEvent` from `com.tisto.kmp.helper.ui.component`.
//     Do NOT declare per-feature UiState/Event for list — only the Effect is feature-specific.
//   ▸ `MutableStateFlow<ListUiState<T>>`. Direct `.value =` assignment. No `combine()`.
//     No `flatMapLatest`. No derived LoadPhase.
//   ▸ `onEvent(event: ListEvent<T>)` — typed to your model (e.g. `ListEvent<Category>`).
//   ▸ `query`, `filters`, `perPage` are plain `private var` — only events mutate them.
//   ▸ On refresh: if current state is Success, set `isRefreshing = true`;
//     if Empty, keep Empty (so the search bar stays visible); else Loading.
//   ▸ Initial load: `init { loadPage(1) }`.
//   ▸ Extract a `buildSearchRequest(page)` helper that converts `query`, `filters`,
//     `perPage` into a `SearchRequest`. SORT-type filters → `orderBy`/`orderType`.
//     FILTER-type filters → `simpleQuery` entries. Default `isActive = "true"` when
//     no status filter is selected.
//   ▸ Empty result list → `ListUiState.Empty(query, filters)` (not `Success(items = emptyList())`).
//     `Empty` carries query + filters so the UI can keep search bar and filter badge visible.
//   ▸ Search debounce: `QueryChanged` must NOT call `loadPage()` directly.
//     Instead: update state.query immediately (so the text field never flickers),
//     cancel any pending `searchJob`, then schedule `loadPage(1)` after 500 ms:
//         searchJob?.cancel()
//         searchJob = viewModelScope.launch { delay(500); loadPage(1) }
//   ▸ `Refresh` (pull-to-refresh / back from form) calls `loadPage(1)` directly — no debounce.
//   ▸ `FiltersApplied` stores filters in the private var, updates current state's
//     filters field, then calls `loadPage(1)`.
//   ▸ `DeleteConfirmed` receives the whole item (not just id) — extract id inside the handler.
//
// PAGINATION
//   ▸ Success state carries: `page`, `perPage`, `totalItems`, `lastPage`, `isLoadingMore`.
//     Derived `hasMore: Boolean get() = page < lastPage`.
//   ▸ Events: `LoadNextPage`, `PageChanged(page)`, `RowsPerPageChanged(perPage)`.
//   ▸ `loadPage(page)` is the single entry point for fetching — used by refresh,
//     search, filter, and manual page changes.
//   ▸ `loadNextPage()` appends results to existing items (infinite scroll). Guards
//     against double-loading: `if (isLoadingMore || !hasMore) return`.
//   ▸ Mobile (Android/iOS): infinite scroll via a `load_more` sentinel item that
//     triggers `LoadNextPage` when composed.
//   ▸ Web/Desktop: manual pagination via `TablePaginationFooter` with next/prev/rowsPerPage.
//
// LIST SCREEN — ListContainer
//   ▸ Use `ListContainer<T>(...)` from `com.tisto.kmp.helper.ui.component`.
//     It handles: Scaffold, Toolbar, search/filter, pull-to-refresh, pagination,
//     infinite scroll, loading/error/empty states, delete confirmation dialog.
//   ▸ Caller provides only: `columns`, `mobileRow`, `filterOptions`, strings, `itemKey`.
//   ▸ `columns` callback signature: `(isPicker: Boolean, onEdit: (T) -> Unit, onDelete: (T) -> Unit) -> List<ListColumn<T>>`
//   ▸ Supports picker mode via `onPick: ((T) -> Unit)?` — show `titlePicker` instead of `title`,
//     hide action column in columns when `isPicker = true`.
//   ▸ `backIcon` defaults to `Icons.AutoMirrored.Filled.ArrowBack`; use `AppIcon.IcMenuSolar`
//     for features launched from the POS drawer.
//   ▸ Do NOT manually write Scaffold, Toolbar, SearchFilterRow, pull-to-refresh, pagination,
//     or empty state in the Screen — ListContainer handles all of it.
//
// PULL-TO-REFRESH (handled by ListContainer)
//   ▸ ListContainer wraps list body with `PullToRefreshBox` on mobile (`!PlatformType.isWeb && !PlatformType.isJvm`).
//     On web/desktop, use a plain `Box` — pull gesture doesn't work there.
//   ▸ Web gets a `RefreshButton` in `SearchFilterRow` instead.
//
// FILTER
//   ▸ `filters: List<FilterItem>` lives in both `Success` and `Empty` states (generic).
//   ▸ `FiltersApplied(filters: List<FilterItem>)` event (generic).
//   ▸ Define a private `featureFilterOptions()` function returning `List<FilterGroup>` —
//     typically one SORT group + optionally a STATUS/TYPE group.
//     Pass it to `ListContainer(filterOptions = featureFilterOptions())`.
//   ▸ ViewModel converts filters in `buildSearchRequest()`:
//     SORT filter → `orderBy` / `orderType` params.
//     FILTER items → `simpleQuery` entries (e.g. `isActive = "false"`).
//
// VIEWMODEL — FORM
//   ▸ Takes constructor param `initialItem: <Name>?`. Null = Create, non-null = Edit.
//   ▸ Derive `mode: FormMode = if (initialItem == null) Create else Edit(initialItem.id)`.
//   ▸ Pre-populate UiState fields from `initialItem` in the MutableStateFlow constructor.
//   ▸ `init { if (initialItem != null) refreshInBackground(initialItem.id) }` — silently
//     fetch latest data from API. Only update fields the user hasn't touched yet:
//         if (current.name == initialItem?.name.orEmpty()) item.name else current.name
//   ▸ Single `MutableStateFlow<FormUiState>` — same pattern as List VM. No `combine`,
//     no `stateIn`. Field values, `isLoading`, `isSubmitting`, per-field errors all
//     live on UiState. Mutate with `_uiState.update { copy(...) }`.
//   ▸ Inner `private data class Validation(...)` with `val hasErrors: Boolean` returned
//     from a pure `validate(state): Validation` function.
//   ▸ `performSubmission(action)` scaffold flips `isSubmitting` on/off via try/finally.
//     Use for submit AND delete. No catch block — onResource owns that.
//   ▸ Clearing field errors: on NameChanged, also set `nameError = null`.
//   ▸ Success message flows through `NavigateBack(message)` — do NOT call `showSuccess()`
//     before `NavigateBack`. The form screen is disposed before the snackbar appears.
//     The message is forwarded to the list screen via `pendingMessage`.
//
// ROUTE / SCREEN SPLIT
//   ▸ Every screen has both. Route = stateful; Screen = pure.
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
//   ▸ List Route also takes `refreshToken`, `pendingMessage`, `onMessageShown`:
//       `LaunchedEffect(refreshToken) { if (refreshToken > 0) viewModel.onEvent(ListEvent.Refresh) }`.
//       `LaunchedEffect(pendingMessage) { if (pendingMessage != null) { snackbar.showSnackbar(pendingMessage); onMessageShown() } }`.
//     This shows form success toasts on the list screen (form navigates back instantly).
//   ▸ Form Route takes `formKey: Int` and uses `"${item?.id}_$formKey"` as the VM key.
//     This ensures a fresh ViewModel each time the form is opened — prevents stale
//     data when creating again or re-editing the same item after save.
//   ▸ Form Route passes `initialItem` via `parameters = { parametersOf(item) }`.
//
// UI COMPONENTS (use these — never raw Material3 equivalents in feature screens)
//   ▸ `ListContainer<T>(...)` — reusable list scaffold. Handles Scaffold, Toolbar,
//     search/filter, pull-to-refresh, pagination, infinite scroll, empty/loading/error states,
//     delete confirmation. Caller only provides: columns, mobileRow, filterOptions, strings.
//   ▸ `ListColumn<T>(key, title, weight, cell)` — column descriptor for list/tablet tables.
//   ▸ `ListHeader(columns)` — tablet column header row.
//   ▸ `ListRow(item, columns, onClick)` — tablet data row with divider.
//   ▸ `ListMobileRow(imageUrl, text, secondary, onClick)` — mobile compact row.
//   ▸ `ListActions(onEdit, onDelete)` — standardized edit/delete icon buttons.
//   ▸ `DeleteConfirmationDialog(showDialog, onDismiss, onConfirm, itemName)` — delete confirm.
//   ▸ `FormContainer(forceTitle, screenConfig, item, onBack, onSave, onDelete)` — form wrapper
//     with back/save/delete wired; drop field composables into its trailing lambda.
//   ▸ `ScaffoldBox(snackbarHostState, isLoadingProcess)` — Scaffold + loading overlay.
//   ▸ `CustomTextField(value, onValueChange, label, style, isError, supportingText)` — general text input.
//   ▸ `CurrencyTextField(value, onValueChange, label, prefix, suffix, maxLength)` — currency / numeric input
//     with thousands formatting ("10.000,9"). Use for prices, discounts, amounts — anything numeric.
//   ▸ `SearchTextField(query, onQueryChange, onClear, label)` — search input with clear icon.
//   ▸ `SwitchCard(checked, onCheckedChange, text)` — labeled switch row.
//   ▸ `CardImagePicker(imageUrl, onPicker)` — image picker tile.
//   ▸ `BackHandler { onBack() }` — hardware back support; place at top of Screen.
//   ▸ `PullToRefreshBox(isRefreshing, onRefresh)` — Material3 pull-to-refresh (mobile only).
//   ▸ `TablePaginationFooter(rowsPerPage, totalItems, currentPage, ...)` — web/desktop pagination.
//   ▸ `GeneralFilterBottomSheet(options, preselected, onClose, onApply)` — filter chip sheet.
//   ▸ `FilterButton(count, onClick)` — opens filter sheet; badge shows active filter count.
//   ▸ `RefreshButton(onClick)` — manual refresh for web (no pull gesture).
//   ▸ `Toolbar(title, screenConfig, onBack, onAdd, backIcon)` — toolbar with "Tambah" button.
//   ▸ `SearchFilterRow(screenConfig, searchQuery, onSearchChange)` — search + filter bar.
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
//   ▸ `ShowMessage` → `snackbar.showSnackbar(AppSnackbarVisuals(message, type))`.
//     Map MessageType → SnackbarType (Success, Error, Warning, Info).
//   ▸ `NavigateToForm(item)` → `onNavigateToForm(effect.item)`.
//   ▸ `NavigateBack(message)` → `onDone(effect.message)`. The message (e.g. "Data tersimpan")
//     is passed to the parent Route, then forwarded to the list screen as `pendingMessage`
//     so the success toast shows on the list — not the form that's about to be disposed.
//   ▸ EffectHandler is always `LaunchedEffect(Unit) { effects.collect { … } }`.
//   ▸ Form ViewModel sends the success message through `NavigateBack(message)` —
//     do NOT call `showSuccess()` before `NavigateBack`, the form screen is disposed
//     before the snackbar appears.
//
// WRAPPER ROUTE (top-level)
//   ▸ Crossfade over a `remember` (NOT rememberSaveable) `stage: Stage` sealed interface.
//   ▸ Stage is a non-Parcelable sealed interface — rememberSaveable crashes with
//     "MutableState containing List cannot be saved using the current SaveableStateRegistry"
//     because Bundle serialization fails on custom sealed types. `remember` is correct here:
//     navigation stage does not need to survive process death.
//   ▸ `refreshToken` via `remember { mutableIntStateOf(0) }` — increment on form `onDone`.
//   ▸ `formKey` via `remember { mutableIntStateOf(0) }` — increment each time navigating
//     to form. Passed to Form Route to generate a unique VM key.
//   ▸ `pendingMessage` via `remember { mutableStateOf<String?>(null) }` — captures the
//     success message from form `onDone(message)`, forwarded to list as `pendingMessage`.
//     Cleared via `onMessageShown = { pendingMessage = null }`.
//   ▸ `private sealed interface Stage { data object List; data class Form(val item: <Name>?) }`.
//   ▸ No nav library. No NavHost.
//
// KOIN MODULE
//   ▸ One module file per feature, declared `val <name>Module = module { ... }`.
//   ▸ `singleOf(::<Name>Api)`, `singleOf(::<Name>Repository)`.
//   ▸ `viewModelOf(::<Name>ListViewModel)`.
//   ▸ Form VM: `viewModel { params -> <Name>FormViewModel(repo = get(), initialItem = params.getOrNull()) }`.
//   ▸ Register the module in `app/.../core/di/ListModules.kt`.
//
// STRINGS
//   ▸ `internal object <Name>Strings` — all user-facing strings in one file.
//   ▸ User-facing copy is Indonesian: "Kembali", "Cari...", "Tambah", "Simpan",
//     "Menyimpan...", "Belum ada data", "Gagal memuat data", "Gagal menyimpan",
//     "Gagal menghapus", "Data tersimpan", "Data berhasil dihapus", "Nama wajib diisi".
//   ▸ Include: titleList, titlePicker, titleCreate, titleEdit, search, empty,
//     label fields, err fields, loadFailed, saveFailed, deleteFailed, saved, deleted.
//
// NAMING & PACKAGING
//   ▸ Feature package name is lowerCamelCase (e.g. `categoryNew`).
//   ▸ Types are PascalCase with feature prefix: `CategoryListViewModel`, `CategoryFormEvent`.
//   ▸ Koin module file is lowerCamelCase: `composeProductModule.kt`, `exampleModule.kt`.
//
// WHAT YOU MUST NOT DO
//   ✗ Do not declare per-feature `UiState` or `Event` sealed interfaces for the list —
//     use generic `ListUiState<T>` and `ListEvent<T>` from `ListContainer.kt`.
//     Only the `Effect` is feature-specific.
//   ✗ Do not manually build Scaffold, Toolbar, SearchFilterRow, pull-to-refresh,
//     pagination, or empty body in list screens — use `ListContainer`.
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
//   ✗ Do not use raw `OutlinedTextField` / `TextField` in forms — use `CustomTextField`
//     for text, `CurrencyTextField` for currency/numbers, `SearchTextField` for search.
//   ✗ Do not use raw `Switch` in a `Row` — use `SwitchCard`.
//   ✗ Do not use `Scaffold` + `TopAppBar` + `FloatingActionButton` in list/form screens —
//     use `ListContainer` for list, `ScaffoldBox` + `FormContainer` for form.
//   ✗ Do not derive `ScreenConfig` from `LocalConfiguration` inside Screen —
//     always receive it as a parameter from `ZenentaTheme { screenConfig -> }` in Route.
//   ✗ Do not use raw `@Preview(widthDp=..., heightDp=...)` — use `@MobilePreview` / `@TabletPreview`.
//   ✗ Do not use `loadExisting(itemId)` in form VM — use `initialItem` + `refreshInBackground`.
//     Pre-fill from list data instantly, then silently refresh from API.
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
import com.tisto.kmp.helper.utils.model.FilterItem
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
//     viewModel { params -> ExampleFormViewModel(repo = get(), initialItem = params.getOrNull()) }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 5B — STRINGS  (presentation/ExampleStrings.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// internal object ExampleStrings {
//     const val titleList = "Example"
//     const val titlePicker = "Pilih Example"
//     const val titleCreate = "Tambah Example"
//     const val titleEdit = "Edit Example"
//
//     const val back = "Kembali"
//     const val add = "Tambah"
//     const val edit = "Edit"
//     const val delete = "Hapus"
//     const val save = "Simpan"
//     const val saving = "Menyimpan..."
//     const val search = "Cari..."
//     const val empty = "Belum ada data"
//
//     const val labelName = "Nama"
//     const val labelDescription = "Deskripsi"
//     const val labelActive = "Aktif"
//     const val columnUpdate = "Update"
//
//     const val errName = "Nama wajib diisi"
//
//     const val loadFailed = "Gagal memuat data"
//     const val saveFailed = "Gagal menyimpan"
//     const val deleteFailed = "Gagal menghapus"
//     const val saved = "Data tersimpan"
//     const val deleted = "Data berhasil dihapus"
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 6 — LIST CONTRACT  (presentation/list/ExampleListContract.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// IMPORTANT: UiState and Event are now GENERIC — use ListUiState<T> + ListEvent<T>
// from com.tisto.kmp.helper.ui.component. Only the Effect is feature-specific.

// import com.tisto.kmp.helper.network.MessageType

sealed interface ExampleListEffect {
    data class ShowMessage(val message: String, val type: MessageType = MessageType.Info) : ExampleListEffect
    data class NavigateToForm(val item: Example?) : ExampleListEffect
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
// import com.tisto.kmp.helper.ui.component.ListEvent
// import com.tisto.kmp.helper.ui.component.ListUiState
// import com.tisto.kmp.helper.utils.model.FilterItem
// import com.tisto.kmp.helper.utils.model.FilterType
// import kotlinx.coroutines.Job
// import kotlinx.coroutines.delay
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.launch
//
// // ── List Effect ──────────────────────────────────────────────────────────
//
// sealed interface ExampleListEffect {
//     data class ShowMessage(val message: String, val type: MessageType = MessageType.Info) : ExampleListEffect
//     data class NavigateToForm(val item: Example?) : ExampleListEffect
// }
//
// // ── List ViewModel ───────────────────────────────────────────────────────
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
//                 if (current is ListUiState.Success) {
//                     _uiState.value = current.copy(query = event.query)
//                 }
//                 searchJob?.cancel()
//                 searchJob = viewModelScope.launch { delay(500); loadPage(1) }
//             }
//             is ListEvent.FiltersApplied -> {
//                 filters = event.filters
//                 val current = _uiState.value
//                 if (current is ListUiState.Success) {
//                     _uiState.value = current.copy(filters = event.filters)
//                 }
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
//         val search = buildSearchRequest(page)
//         repo.get(search).launchResourcePaged(
//             fallbackError = ExampleStrings.loadFailed,
//             onError = { _uiState.value = ListUiState.Error(it) },
//         ) { result ->
//             val items = result.data
//             if (items.isEmpty() && page == 1) {
//                 _uiState.value = ListUiState.Empty(query = query, filters = filters)
//             } else {
//                 _uiState.value = ListUiState.Success(
//                     items = items, query = query, filters = filters,
//                     page = result.currentPage ?: page,
//                     perPage = result.perPage ?: perPage,
//                     totalItems = result.total ?: items.size,
//                     lastPage = result.lastPage,
//                 )
//             }
//         }
//     }
//
//     private fun loadNextPage() {
//         val current = _uiState.value
//         if (current !is ListUiState.Success) return
//         if (current.isLoadingMore || !current.hasMore) return
//         val nextPage = current.page + 1
//         _uiState.value = current.copy(isLoadingMore = true)
//         val search = buildSearchRequest(nextPage)
//         repo.get(search).launchResourcePaged(
//             fallbackError = ExampleStrings.loadFailed,
//             onError = { _uiState.value = current.copy(isLoadingMore = false); showError(it) },
//         ) { result ->
//             val prev = _uiState.value as? ListUiState.Success ?: return@launchResourcePaged
//             _uiState.value = prev.copy(
//                 items = prev.items + result.data,
//                 page = result.currentPage ?: nextPage,
//                 perPage = result.perPage ?: perPage,
//                 totalItems = result.total ?: prev.totalItems,
//                 lastPage = result.lastPage,
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

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 8 — LIST SCREEN  (presentation/list/ExampleListScreen.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// NOTE: The Screen composable now uses ListContainer<T> which handles ALL the boilerplate
// (Scaffold, Toolbar, search, filter, pull-to-refresh, pagination, empty/loading/error).
// The caller only provides: columns, mobileRow, filterOptions, and strings.
//
// import androidx.compose.foundation.layout.*
// import androidx.compose.material3.*
// import androidx.compose.runtime.*
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import com.tisto.kmp.helper.ui.component.*
// import com.tisto.kmp.helper.ui.ext.*
// import com.tisto.kmp.helper.utils.ext.def
// import com.tisto.kmp.helper.utils.ext.formatDate
// import com.tisto.kmp.helper.utils.model.FilterGroup
// import com.tisto.kmp.helper.utils.model.FilterItem
// import com.tisto.kmp.helper.utils.model.FilterType
// import com.zenenta.pos.core.ui.ui.icon.AppIcon
// import com.zenenta.pos.core.ui.ui.icon.myicon.IcMenuSolar
// import com.zenenta.pos.core.ui.ui.theme.ZenentaTheme
// import kotlinx.coroutines.flow.Flow
//
// // ── Route (stateful) ───────────────────────────────────────���─────────────
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
//
//     LaunchedEffect(refreshToken) {
//         if (refreshToken > 0) viewModel.onEvent(ListEvent.Refresh)
//     }
//     LaunchedEffect(pendingMessage) {
//         if (pendingMessage != null) { snackbar.showSnackbar(pendingMessage); onMessageShown() }
//     }
//
//     ZenentaTheme { screenConfig ->
//         ExampleListScreen(
//             screenConfig = screenConfig,
//             state = state,
//             snackbar = snackbar,
//             onEvent = viewModel::onEvent,
//             onBack = onBack,
//             onPick = onPick,
//         )
//     }
// }
//
// @Composable
// private fun ExampleListEffectHandler(
//     effects: Flow<ExampleListEffect>,
//     snackbar: SnackbarHostState,
//     onNavigateToForm: (Example?) -> Unit,
// ) {
//     LaunchedEffect(Unit) {
//         effects.collect { effect ->
//             when (effect) {
//                 is ExampleListEffect.ShowMessage -> snackbar.showSnackbar(effect.message)
//                 is ExampleListEffect.NavigateToForm -> onNavigateToForm(effect.item)
//             }
//         }
//     }
// }
//
// // ── Screen (stateless) — uses ListContainer ──────────────────────────────
//
// @Composable
// fun ExampleListScreen(
//     screenConfig: ScreenConfig = ScreenConfig(),
//     state: ListUiState<Example>,
//     snackbar: SnackbarHostState,
//     onEvent: (ListEvent<Example>) -> Unit,
//     onBack: () -> Unit,
//     onPick: ((Example) -> Unit)? = null,
// ) {
//     ListContainer(
//         screenConfig = screenConfig,
//         state = state,
//         snackbar = snackbar,
//         title = ExampleStrings.titleList,
//         titlePicker = ExampleStrings.titlePicker,
//         emptyText = ExampleStrings.empty,
//         searchHint = ExampleStrings.search,
//         filterOptions = exampleFilterOptions(),
//         backIcon = AppIcon.IcMenuSolar,
//         onEvent = onEvent,
//         onBack = onBack,
//         onPick = onPick,
//         deleteItemName = { it.name },
//         itemKey = { it.id },
//         columns = ::exampleColumns,
//         mobileRow = { item, onClick ->
//             ListMobileRow(
//                 imageUrl = item.image,
//                 text = item.name.def(),
//                 secondary = item.description?.takeIf { it.isNotBlank() },
//                 onClick = onClick,
//             )
//         },
//     )
// }
//
// // ── Columns ──────────────────────────────────────────────────────────────
//
// private fun exampleColumns(
//     isPicker: Boolean,
//     onEdit: (Example) -> Unit,
//     onDelete: (Example) -> Unit,
// ): List<ListColumn<Example>> = buildList {
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
// // ── Filter options (customize per feature) ───────────────────────────────
//
// private fun exampleFilterOptions() = listOf(
//     FilterGroup(
//         title = "Urutkan", type = FilterType.SORT,
//         options = listOf(
//             FilterItem("Nama: A-Z", "asc", "name", FilterType.SORT),
//             FilterItem("Nama: Z-A", "desc", "name", FilterType.SORT),
//             FilterItem("Terbaru", "desc", "createdAt", FilterType.SORT),
//             FilterItem("Terlama", "asc", "createdAt", FilterType.SORT),
//         ),
//     ),
// )
//
// // ── Previews ──────────────────────────────────────────────────────────────
//
// @Composable
// private fun ScreenContentPreview(screenConfig: ScreenConfig = ScreenConfig()) {
//     ZenentaTheme {
//         ExampleListScreen(
//             screenConfig = screenConfig,
//             state = ListUiState.Success(
//                 items = listOf(
//                     Example(id = "1", name = "Item Satu", updatedAt = "2025-12-22T04:12:09.000Z"),
//                     Example(id = "2", name = "Item Dua", updatedAt = "2025-12-22T04:12:09.000Z"),
//                     Example(id = "3", name = "Item Tiga", updatedAt = "2025-12-22T04:12:09.000Z"),
//                 ),
//                 totalItems = 25, lastPage = 3,
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
// private fun PreviewExampleList() { ScreenContentPreview() }
//
// @TabletPreview
// @Composable
// private fun TabletPreviewExampleList() { ScreenContentPreview(ScreenConfig(700.dp)) }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 9 — FORM CONTRACT  (presentation/form/ExampleFormContract.kt)
// ══════════════════════════════════════════════════════════════════════════════════════���═══
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
//     val imageUrl: String? = null,
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

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 10 — FORM VIEWMODEL  (presentation/form/ExampleFormViewModel.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Takes `initialItem: Example?` — pre-fills from list data, then refreshes in background.
// Single MutableStateFlow<UiState>. `performSubmission` uses raw `viewModelScope.launch`
// because it needs try/finally around `isSubmitting`. That's the ONE pattern that stays
// manual; everything else is a one-liner via `.launchResource { }`.
//
// import androidx.lifecycle.viewModelScope
// import com.tisto.kmp.helper.network.base.FeatureViewModel
// import com.tisto.kmp.helper.network.utils.onResource
// import com.tisto.kmp.helper.network.MessageType
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.flow.update
// import kotlinx.coroutines.launch
//
// class ExampleFormViewModel(
//     private val repo: ExampleRepository,
//     private val initialItem: Example?,
// ) : FeatureViewModel<ExampleFormEffect>() {
//
//     private val mode: ExampleFormMode =
//         if (initialItem == null) ExampleFormMode.Create else ExampleFormMode.Edit(initialItem.id)
//
//     private val _uiState = MutableStateFlow(
//         ExampleFormUiState(
//             mode = mode,
//             name = initialItem?.name.orEmpty(),
//             description = initialItem?.description.orEmpty(),
//             imageUrl = initialItem?.image,
//             isActive = initialItem?.isActive ?: true,
//         )
//     )
//     val uiState: StateFlow<ExampleFormUiState> = _uiState.asStateFlow()
//
//     override fun showMessage(msg: String, type: MessageType) {
//         sendEffect(ExampleFormEffect.ShowMessage(msg, type))
//     }
//
//     init { if (initialItem != null) refreshInBackground(initialItem.id) }
//
//     fun onEvent(event: ExampleFormEvent) {
//         when (event) {
//             is ExampleFormEvent.NameChanged ->
//                 _uiState.update { it.copy(name = event.value, nameError = null) }
//             is ExampleFormEvent.DescriptionChanged ->
//                 _uiState.update { it.copy(description = event.value) }
//             is ExampleFormEvent.ImagePicked ->
//                 _uiState.update { it.copy(pickedImage = event.value) }
//             is ExampleFormEvent.ActiveChanged ->
//                 _uiState.update { it.copy(isActive = event.value) }
//             ExampleFormEvent.Submit -> submit()
//             ExampleFormEvent.Delete -> delete()
//         }
//     }
//
//     private fun refreshInBackground(id: String) {
//         viewModelScope.launch {
//             try {
//                 repo.getOne(id).onResource(
//                     fallbackError = ExampleStrings.loadFailed,
//                     onError = { /* silent — we already have list data */ },
//                 ) { item ->
//                     val current = _uiState.value
//                     // Only update fields the user hasn't touched yet
//                     _uiState.update {
//                         it.copy(
//                             name = if (current.name == initialItem?.name.orEmpty()) item.name else current.name,
//                             description = if (current.description == initialItem?.description.orEmpty()) item.description.orEmpty() else current.description,
//                             imageUrl = if (current.pickedImage == null) item.image else current.imageUrl,
//                             isActive = if (current.isActive == initialItem?.isActive) item.isActive else current.isActive,
//                         )
//                     }
//                 }
//             } catch (_: Exception) {
//                 // silent — list data is good enough
//             }
//         }
//     }
//
//     private fun submit() {
//         val state = _uiState.value
//         if (state.name.isBlank()) {
//             _uiState.update { it.copy(nameError = ExampleStrings.errName) }
//             return
//         }
//         val request = ExampleRequest(
//             id = (mode as? ExampleFormMode.Edit)?.itemId,
//             name = state.name.trim(),
//             description = state.description.trim().takeIf { it.isNotEmpty() },
//             image = state.imageUrl,
//             isActive = state.isActive,
//             pickedImage = state.pickedImage,
//         )
//         performSubmission {
//             val flow = if (mode is ExampleFormMode.Create) repo.create(request) else repo.update(request)
//             flow.onResource(fallbackError = ExampleStrings.saveFailed, onError = ::showError) {
//                 sendEffect(ExampleFormEffect.NavigateBack(ExampleStrings.saved))
//             }
//         }
//     }
//
//     private fun delete() {
//         val editMode = mode as? ExampleFormMode.Edit ?: return
//         performSubmission {
//             repo.delete(editMode.itemId).onResource(fallbackError = ExampleStrings.deleteFailed, onError = ::showError) {
//                 sendEffect(ExampleFormEffect.NavigateBack(ExampleStrings.deleted))
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
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 11 — FORM SCREEN  (presentation/form/ExampleFormScreen.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import androidx.compose.foundation.layout.*
// import androidx.compose.material3.SnackbarHostState
// import androidx.compose.runtime.*
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import com.tisto.kmp.helper.network.MessageType
// import com.tisto.kmp.helper.ui.component.AppSnackbarVisuals
// import com.tisto.kmp.helper.ui.component.CardImagePicker
// import com.tisto.kmp.helper.ui.component.CustomTextField
// import com.tisto.kmp.helper.ui.component.FormContainer
// import com.tisto.kmp.helper.ui.component.ScaffoldBox
// import com.tisto.kmp.helper.ui.component.TextFieldStyle
// import com.tisto.kmp.helper.ui.ext.BackHandler
// import com.tisto.kmp.helper.ui.ext.MobilePreview
// import com.tisto.kmp.helper.ui.ext.ScreenConfig
// import com.tisto.kmp.helper.ui.ext.TabletPreview
// import com.tisto.kmp.helper.ui.ext.safeKoinViewModel
// import com.tisto.kmp.helper.ui.theme.Spacing
// import com.tisto.kmp.helper.utils.SnackbarType
// import com.zenenta.pos.core.ui.ui.theme.ZenentaTheme
// import kotlinx.coroutines.flow.Flow
// import org.koin.core.parameter.parametersOf
//
// @Composable
// fun ExampleFormRoute(
//     item: Example?,
//     formKey: Int = 0,
//     onDone: (message: String?) -> Unit,
// ) {
//     val viewModel: ExampleFormViewModel = safeKoinViewModel(
//         key = "${item?.id}_$formKey",
//         parameters = { parametersOf(item) },
//     ) ?: return
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
//             onBack = { onDone(null) },
//         )
//     }
// }
//
// @Composable
// private fun ExampleFormEffectHandler(
//     effects: Flow<ExampleFormEffect>,
//     snackbar: SnackbarHostState,
//     onDone: (message: String?) -> Unit,
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
//                 is ExampleFormEffect.NavigateBack -> onDone(effect.message)
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
//     ) {
//         FormContainer(
//             forceTitle = if (state.mode is ExampleFormMode.Edit) ExampleStrings.titleEdit else ExampleStrings.titleCreate,
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
//             Spacer(Modifier.height(if (screenConfig.isMobile) Spacing.normal else Spacing.large))
//
//             CardImagePicker(
//                 modifier = Modifier.width(120.dp).height(120.dp),
//                 imageUrl = state.imageUrl,
//                 onPicker = { onEvent(ExampleFormEvent.ImagePicked(it)) },
//             )
//
//             Spacer(Modifier.height(Spacing.large))
//
//             CustomTextField(
//                 value = state.name,
//                 onValueChange = { onEvent(ExampleFormEvent.NameChanged(it)) },
//                 label = ExampleStrings.labelName,
//                 style = TextFieldStyle.OUTLINED,
//                 strokeWidth = 1.dp,
//                 isError = state.nameError != null,
//                 supportingText = state.nameError,
//                 modifier = Modifier.fillMaxWidth(),
//                 singleLine = true,
//             )
//
//             Spacer(Modifier.height(Spacing.normal))
//
//             CustomTextField(
//                 value = state.description,
//                 onValueChange = { onEvent(ExampleFormEvent.DescriptionChanged(it)) },
//                 label = ExampleStrings.labelDescription,
//                 style = TextFieldStyle.OUTLINED,
//                 strokeWidth = 1.dp,
//                 modifier = Modifier.fillMaxWidth(),
//             )
//         }
//     }
// }
//
// // ── Previews ──────────────────────────────────────────────────────────────
//
// @MobilePreview
// @Composable
// private fun PreviewExampleFormPhone() {
//     ExampleFormScreen(
//         state = ExampleFormUiState(
//             mode = ExampleFormMode.Edit("1"),
//             name = "Contoh Item",
//             description = "Deskripsi contoh item",
//             isActive = true,
//         ),
//         snackbar = SnackbarHostState(),
//         screenConfig = ScreenConfig(maxWidth = 360.dp),
//         onEvent = {},
//         onBack = {},
//     )
// }
//
// @TabletPreview
// @Composable
// private fun PreviewExampleFormTablet() {
//     ExampleFormScreen(
//         state = ExampleFormUiState(
//             mode = ExampleFormMode.Edit("1"),
//             name = "Contoh Item",
//             description = "Deskripsi contoh item",
//             isActive = true,
//         ),
//         snackbar = SnackbarHostState(),
//         screenConfig = ScreenConfig(maxWidth = 840.dp),
//         onEvent = {},
//         onBack = {},
//     )
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 12 — WRAPPER ROUTE  (presentation/ExampleRoute.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
//
// import androidx.compose.animation.Crossfade
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableIntStateOf
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.setValue
// import androidx.compose.ui.Modifier
//
// @Composable
// fun ExampleRoute(
//     onBack: () -> Unit = {},
//     onPick: ((Example) -> Unit)? = null,
// ) {
//     var stage by remember { mutableStateOf<Stage>(Stage.List) }
//     var refreshToken by remember { mutableIntStateOf(0) }
//     var formKey by remember { mutableIntStateOf(0) }
//     var pendingMessage by remember { mutableStateOf<String?>(null) }
//
//     Crossfade(targetState = stage, modifier = Modifier.fillMaxSize()) { current ->
//         when (current) {
//             Stage.List -> ExampleListRoute(
//                 onNavigateToForm = { item -> formKey++; stage = Stage.Form(item) },
//                 onBack = onBack,
//                 onPick = onPick,
//                 refreshToken = refreshToken,
//                 pendingMessage = pendingMessage,
//                 onMessageShown = { pendingMessage = null },
//             )
//             is Stage.Form -> ExampleFormRoute(
//                 item = current.item,
//                 formKey = formKey,
//                 onDone = { message ->
//                     pendingMessage = message
//                     refreshToken++
//                     stage = Stage.List
//                 },
//             )
//         }
//     }
// }
//
// private sealed interface Stage {
//     data object List : Stage
//     data class Form(val item: Example?) : Stage
// }
