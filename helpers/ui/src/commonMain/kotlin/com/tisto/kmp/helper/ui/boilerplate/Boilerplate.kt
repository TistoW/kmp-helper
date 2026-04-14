@file:Suppress("unused", "RedundantVisibilityModifier")

package com.tisto.kmp.helper.ui.boilerplate

// ══════════════════════════════════════════════════════════════════════════════════════════
// MVI / UDF BOILERPLATE — The rulebook for building new features in this codebase.
//
// READ THIS FIRST. Then open `BoilerplateExample.kt` (same package) for the full
// concrete reference with every section filled out, mirroring the canonical
// Category feature shipped at:
//
//   data → compose/feature/product/.../data/
//     ├── model/Category.kt
//     ├── request/CategoryRequest.kt
//     ├── service/CategoryApi.kt               ← Ktor HttpClient
//     ├── repository/CategoryRepository.kt     ← apiCall() → Flow<Resource<T>>
//     └── di/composeProductModule.kt
//   ui   → compose/feature/product/.../presentation/categoryNew/
//     ├── CategoryRoute.kt                     ← Crossfade wrapper
//     ├── list/{Contract,ViewModel,Screen}.kt
//     └── form/{Contract,ViewModel,Screen}.kt
//
// When the user asks for a new feature, produce something that is indistinguishable
// from Category in shape. Any deviation from the rules below is a bug.
//
// ──────────────────────────────────────────────────────────────────────────────────────────
// HOW TO USE
//
//   1. Copy sections from `BoilerplateExample.kt` into your feature module.
//   2. Find-and-replace "Example" → your feature name (e.g. "Invoice").
//   3. Split into files per the target layout below.
//   4. Add your Koin module to app/.../core/di/ListModules.kt.
//
// ──────────────────────────────────────────────────────────────────────────────────────────
// TARGET FILE LAYOUT
//
//   feature/<name>/
//   ├── data/
//   │   ├── model/<Name>.kt                   ← @Serializable domain model (doubles as DTO)
//   │   ├── request/<Name>Request.kt          ← outbound DTO, @Transient PickedImage
//   │   ├── service/<Name>Api.kt              ← Ktor HttpClient, BaseResponse<T>
//   │   ├── repository/<Name>Repository.kt    ← apiCall() wrappers, Flow<Resource<T>>
//   │   └── di/<name>Module.kt                ← Koin module: Api + Repo + both VMs
//   └── presentation/<name>/
//       ├── <Name>Route.kt                    ← Crossfade wrapper (List ↔ Form)
//       ├── list/
//       │   ├── <Name>ListContract.kt         ← UiState / Event / Effect (sealed)
//       │   ├── <Name>ListViewModel.kt        ← plain ViewModel, single MutableStateFlow
//       │   └── <Name>ListScreen.kt           ← Route (stateful) + Screen (stateless)
//       └── form/
//           ├── <Name>FormContract.kt
//           ├── <Name>FormViewModel.kt        ← combine(fields, isLoading, isSubmitting)
//           └── <Name>FormScreen.kt
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
//   ▸ No try/catch here. No caching. No SSOT. No Flow orchestration.
//   ▸ No UseCase layer for passthrough CRUD.
//
// DOMAIN MODEL vs DTO
//   ▸ One type only: `@Serializable data class <Name>(...)` in data/model/.
//   ▸ No separate `<Name>Dto` with `.toDomain()`. No `<Name>Draft`.
//   ▸ `<Name>Request` is separate — outbound body with `@Transient val pickedImage`.
//
// VIEWMODEL — GENERAL
//   ▸ Plain `androidx.lifecycle.ViewModel`. NEVER use helper's BaseViewModel /
//     BaseState / StateHandler / collectResource DSL.
//   ▸ Single `onEvent(event)` entry point per VM.
//   ▸ Effects: `Channel<Effect>(Channel.BUFFERED)` + `receiveAsFlow()`. Always `trySend`,
//     never `send()` (suspending).
//   ▸ Provide message shortcuts inside the VM:
//       private fun showMessage(msg) / showSuccess(msg) / showError(msg)
//   ▸ Repo calls ALWAYS go through `Flow<Resource<T>>.onResource(...)`
//     (from `com.tisto.kmp.helper.network.utils.onResource`). This bundles
//     try/catch + when(Success/Error/Loading) into success + error callbacks:
//
//         repo.get(search).onResource(
//             fallbackError = "Gagal memuat data",
//             onError = { _uiState.value = ExampleListUiState.Error(it) },
//             onSuccess = { items -> ... },
//         )
//
//     NEVER write `.collect { when (resource) { Resource.Success -> ... } }`
//     manually. NEVER wrap a `.onResource` call in your own try/catch — it
//     already handles it. If a success callback needs many lines, still use
//     onResource; branch logic inside the lambda.
//
// VIEWMODEL — LIST
//   ▸ Single `MutableStateFlow<UiState>`. Direct `.value =` assignment. No `combine()`.
//     No `flatMapLatest`. No derived LoadPhase.
//   ▸ `query` is a plain `private var` — only user events mutate it; reload is explicit.
//   ▸ On refresh: if current state is Success, set `isRefreshing = true`; else set Loading.
//   ▸ Initial load: `init { refresh() }`.
//   ▸ Search payload: `SearchRequest(page = 1, perpage = 100, simpleQuery = buildList { ... })`
//     — always `add(Search("isActive", "true"))`; add `Search("name", query)` when non-blank.
//   ▸ Empty result list → `UiState.Empty` (not `Success(items = emptyList())`).
//
// VIEWMODEL — FORM
//   ▸ Takes constructor param `itemId: String?`. Null = Create, non-null = Edit.
//   ▸ Derive `mode: FormMode = if (itemId == null) Create else Edit(itemId)`.
//   ▸ THREE source flows: `fieldsFlow`, `isLoadingFlow`, `isSubmittingFlow`. Combine
//     into uiState via `combine(...)` + `.stateIn(scope, WhileSubscribed(5_000), initial)`.
//   ▸ Inner `private data class FormFields(...)` holds field values + per-field errors.
//   ▸ Inner `private data class Validation(...)` with `val hasErrors: Boolean` returned
//     from a pure `validate(fields): Validation` function.
//   ▸ `performSubmission(action)` scaffold flips `isSubmittingFlow` on/off via
//     try/finally. Use for submit AND delete. No `fallbackError` param needed —
//     onResource owns error reporting. No catch block — onResource owns that too.
//   ▸ Clearing field errors: on NameChanged, also set `nameError = null`.
//   ▸ Load existing in `init { if (itemId != null) loadExisting(itemId) }`.
//
// ROUTE / SCREEN SPLIT
//   ▸ Every screen has both. Route = stateful; Screen = pure `(state, snackbar, onEvent, onBack) -> Unit`.
//   ▸ Route responsibilities:
//       – resolve VM via `safeKoinViewModel()` — NOT `koinViewModel()`. Always `?: return`.
//       – own `SnackbarHostState`.
//       – collect effects in a private `@Composable EffectHandler(...)`.
//       – pass `viewModel::onEvent` to Screen.
//   ▸ List Route also takes `refreshToken: Int = 0` and does
//       `LaunchedEffect(refreshToken) { if (refreshToken > 0) viewModel.onEvent(Refresh) }`.
//   ▸ Form Route passes `itemId.toString()` into `safeKoinViewModel(itemId.toString())`
//     as the VM key, so Create ("null") and Edit ("<id>") get distinct instances.
//
// EFFECT HANDLING
//   ▸ `ShowMessage` → `snackbar.showSnackbar(effect.message)`.
//   ▸ `NavigateToForm(id)` → `onNavigateToForm(effect.itemId)`.
//   ▸ `NavigateBack` → `onDone()` or equivalent caller.
//   ▸ EffectHandler is always `LaunchedEffect(Unit) { effects.collect { … } }`.
//
// WRAPPER ROUTE (top-level)
//   ▸ Crossfade over a `rememberSaveable` `stage: Stage` sealed interface.
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
//   ✗ Do not combine flows in the List VM — it has a single MutableStateFlow.
//   ✗ Do not hoist `isLoading` / `isSubmitting` into `FormFields`; they are their
//     own flows so `performSubmission` touches them in one place.
//   ✗ Do not use `koinViewModel()` — always `safeKoinViewModel()`.
//   ✗ Do not split Example model into `<Name>Dto` + `<Name>` + `toDomain()`.
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

// ══════════════════════════════════════════════════════════════════════════════════════════
// SHARED UI HELPERS — used by both list and form screens across features.
// Copy these small helpers alongside screen code or import them if exposed.
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
