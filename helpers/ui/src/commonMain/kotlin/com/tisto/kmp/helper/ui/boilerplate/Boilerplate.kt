@file:Suppress("unused", "RedundantVisibilityModifier")

package com.tisto.kmp.helper.ui.boilerplate

// ======================================================================================
// MVI / UDF BOILERPLATE — Copy-paste starter for any new feature.
//
// HOW TO USE:
//   1. Copy this file into your feature module.
//   2. Find-and-replace "Example" with your feature name (e.g. "Invoice").
//   3. Split into separate files per the layout below.
//   4. Delete the sections you don't need (e.g. form if list-only).
//
// TARGET FILE LAYOUT:
//
//   feature/<name>/
//   ├── domain/model/
//   │   ├── <Name>.kt                    ← domain model (non-nullable, no framework)
//   │   └── <Name>Draft.kt              ← form input type (id nullable = create)
//   ├── data/
//   │   ├── model/<Name>Dto.kt           ← wire DTO + toDomain()
//   │   ├── request/<Name>Request.kt     ← outbound DTO + fromDraft()
//   │   ├── service/<Name>Api.kt         ← raw Ktor, 1:1 endpoints
//   │   ├── repository/<Name>Repository.kt ← concrete class, SSOT via cache
//   │   └── di/<name>Module.kt           ← per-feature Koin module
//   └── presentation/<name>/
//       ├── <Name>Route.kt               ← wrapper Route with Crossfade nav
//       ├── list/
//       │   ├── <Name>ListContract.kt    ← UiState / Event / Effect sealed types
//       │   ├── <Name>ListViewModel.kt   ← plain ViewModel, no base class
//       │   └── <Name>ListScreen.kt      ← Route (stateful) + Screen (stateless)
//       └── form/
//           ├── <Name>FormContract.kt
//           ├── <Name>FormViewModel.kt   ← takes nullable itemId via parametersOf()
//           └── <Name>FormScreen.kt
//
// RULES (non-negotiable):
//   - No BaseViewModel / BaseState / StateHandler from helper module.
//   - No repository interface. No UseCase layer for passthrough CRUD.
//   - Route/Screen split on EVERY screen. Screen is pure (state, onEvent) -> Unit.
//   - Single MutableStateFlow<UiState> per ViewModel. Direct .value assignment. No combine().
//   - Form VM: separate flows for fields/isLoading/isSubmitting combined via combine()
//     (forms have many small fields — combine 3 flows is still readable).
//   - try/catch for async. State transitions are explicit (Loading → Success/Error).
//   - trySend on Channel.BUFFERED for effects. Never send() (suspending).
//   - Single onEvent() entry point per ViewModel.
//   - Repository exposes suspend funs, not Flows. No internal reactive cache.
// ======================================================================================

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 0: SHARED — MessageType enum (used by all Effect.ShowMessage)
// ══════════════════════════════════════════════════════════════════════════════════════════

enum class MessageType { Success, Error, Warning, Info }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 1: DOMAIN MODEL
// ══════════════════════════════════════════════════════════════════════════════════════════

// Domain model — what UI and business logic work with.
// Non-nullable, no framework deps, no serialization annotations.
// The mapper in data layer provides defaults for missing fields.
data class Example(
    val id: String,
    val name: String,
    // TODO: add your domain fields here
)

// Draft — the shape passed into save(). id == null means CREATE, otherwise UPDATE.
// Separate type from Example so you never accidentally pass a half-filled form object
// as if it were a real persisted entity.
data class ExampleDraft(
    val id: String?,
    val name: String,
    // TODO: add your draft fields here
)

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 2: DATA LAYER — DTO + Request + Api + Repository + Koin module
// ══════════════════════════════════════════════════════════════════════════════════════════

// Wire DTO — what comes back from the API. Nullable fields, @Serializable.
// Keep a toDomain() extension so the mapping is next to the DTO definition.
@Serializable
data class ExampleDto(
    val id: String? = null,
    val name: String? = null,
    // TODO: add your DTO fields here
) {
    fun toDomain(): Example = Example(
        id = id.orEmpty(),
        name = name.orEmpty(),
    )
}

// Outbound request DTO — what we send to the API on create/update.
@Serializable
data class ExampleRequest(
    val id: String? = null,
    val name: String? = null,
    // TODO: add your request fields here
) {
    companion object {
        fun fromDraft(draft: ExampleDraft): ExampleRequest = ExampleRequest(
            id = draft.id,
            name = draft.name,
        )
    }
}

// ─── API SERVICE ────────────────────────────────────────────────────────────────────────
// Raw HTTP only. 1:1 with backend endpoints. Returns BaseResponse<T> unchanged.
// Repository is responsible for unwrapping and mapping to domain.
//
// class ExampleApi(private val client: HttpClient) {
//
//     suspend fun create(body: ExampleRequest): BaseResponse<ExampleDto> =
//         client.postMethod(url = "example", body = body)
//
//     suspend fun get(search: SearchRequest? = null): BaseResponse<List<ExampleDto>> =
//         client.getMethod(url = "example", query = search?.convertToQuery())
//
//     suspend fun getOne(id: String): BaseResponse<ExampleDto> =
//         client.getMethod(url = "example/$id")
//
//     suspend fun update(body: ExampleRequest): BaseResponse<ExampleDto> =
//         client.putMethod(url = "example/${body.id}", body = body)
//
//     suspend fun delete(id: String): BaseResponse<ExampleDto> =
//         client.deleteMethod(url = "example/$id")
// }

// ─── REPOSITORY ─────────────────────────────────────────────────────────────────────────
// Plain suspend functions. No reactive cache, no Flow. ViewModel calls these directly
// and sets its own MutableStateFlow. Simple to read, simple to debug.
// No interface. Presentation imports this concrete class directly.
// Error handling: this layer THROWS on failure. Upper layers catch.
//
// class ExampleRepository(private val api: ExampleApi) {
//
//     suspend fun getAll(query: String = ""): List<Example> {
//         val response = api.get(SearchRequest(page = 1, perpage = 100, simpleQuery = buildList {
//             if (query.isNotBlank()) add(Search("name", query))
//         }))
//         return response.requireList().map { it.toDomain() }
//     }
//
//     suspend fun getOne(id: String): Example =
//         api.getOne(id).requireData().toDomain()
//
//     suspend fun save(draft: ExampleDraft): Example {
//         val request = ExampleRequest.fromDraft(draft)
//         val saved = if (draft.id == null) api.create(request) else api.update(request)
//         return saved.requireData().toDomain()
//     }
//
//     suspend fun delete(id: String) {
//         api.delete(id).requireSuccess()
//     }
// }
//
// // Small helpers to unwrap BaseResponse once.
// private fun <T> BaseResponse<T>.requireData(): T {
//     requireSuccess(); return data ?: error("Empty response body: $message")
// }
// private fun <T> BaseResponse<List<T>>.requireList(): List<T> {
//     requireSuccess(); return data.orEmpty()
// }
// private fun BaseResponse<*>.requireSuccess() {
//     if (!isSuccess) error(message.ifBlank { "Request failed" })
// }

// ─── KOIN MODULE ────────────────────────────────────────────────────────────────────────
// One Koin module per feature. Form VM takes runtime param via parametersOf().
//
// val exampleModule = module {
//     singleOf(::ExampleApi)
//     singleOf(::ExampleRepository)
//     viewModelOf(::ExampleListViewModel)
//     viewModel { params -> ExampleFormViewModel(repo = get(), itemId = params.getOrNull()) }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 3: LIST — Contract + ViewModel + Screen
// ══════════════════════════════════════════════════════════════════════════════════════════

// ─── LIST CONTRACT ──────────────────────────────────────────────────────────────────────
// Three sealed hierarchies per screen: UiState, Event, Effect.
// Exhaustive states make illegal states unrepresentable.

sealed interface ExampleListUiState {
    data object Loading : ExampleListUiState
    data object Empty : ExampleListUiState
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

// ─── LIST VIEWMODEL ─────────────────────────────────────────────────────────────────────
// Single MutableStateFlow. No combine, no flatMapLatest, no LoadPhase.
// State transitions are explicit: Loading → Success/Empty/Error.
// query is a plain var — no need for a flow when the only trigger is user action.

// NOTE: This is a skeleton. Uncomment and wire to your repository.

// class ExampleListViewModel(
//     private val repo: ExampleRepository,
// ) : ViewModel() {
//
//     private val _uiState = MutableStateFlow<ExampleListUiState>(ExampleListUiState.Loading)
//     val uiState: StateFlow<ExampleListUiState> = _uiState.asStateFlow()
//
//     private val effectChannel = Channel<ExampleListEffect>(Channel.BUFFERED)
//     val effect: Flow<ExampleListEffect> = effectChannel.receiveAsFlow()
//
//     // ── message shortcuts ──
//     private fun showMessage(msg: String) = effectChannel.trySend(ExampleListEffect.ShowMessage(msg))
//     private fun showSuccess(msg: String) = effectChannel.trySend(ExampleListEffect.ShowMessage(msg, MessageType.Success))
//     private fun showError(msg: String) = effectChannel.trySend(ExampleListEffect.ShowMessage(msg, MessageType.Error))
//     private fun showWarning(msg: String) = effectChannel.trySend(ExampleListEffect.ShowMessage(msg, MessageType.Warning))
//
//     private var query = ""
//
//     init { refresh() }
//
//     fun onEvent(event: ExampleListEvent) {
//         when (event) {
//             ExampleListEvent.Refresh -> refresh()
//             is ExampleListEvent.QueryChanged -> { query = event.query; refresh() }
//             ExampleListEvent.CreateClicked -> effectChannel.trySend(ExampleListEffect.NavigateToForm(null))
//             is ExampleListEvent.EditClicked -> effectChannel.trySend(ExampleListEffect.NavigateToForm(event.id))
//             is ExampleListEvent.DeleteConfirmed -> delete(event.id)
//         }
//     }
//
//     private fun refresh() {
//         viewModelScope.launch {
//             val current = _uiState.value
//             if (current is ExampleListUiState.Success) {
//                 _uiState.value = current.copy(isRefreshing = true)
//             } else {
//                 _uiState.value = ExampleListUiState.Loading
//             }
//             try {
//                 val items = repo.getAll(query)
//                 _uiState.value = if (items.isEmpty()) ExampleListUiState.Empty
//                     else ExampleListUiState.Success(items = items, query = query)
//             } catch (t: Throwable) {
//                 _uiState.value = ExampleListUiState.Error(t.message ?: "Gagal memuat data")
//             }
//         }
//     }
//
//     private fun delete(id: String) {
//         viewModelScope.launch {
//             try {
//                 repo.delete(id)
//                 showSuccess("Data dihapus")
//                 refresh()
//             } catch (t: Throwable) {
//                 showError(t.message ?: "Gagal menghapus")
//             }
//         }
//     }
// }

// ─── LIST SCREEN ────────────────────────────────────────────────────────────────────────
// Route = stateful (koinViewModel, collect effects). Screen = stateless (state, onEvent).

// @Composable
// fun ExampleListRoute(
//     onNavigateToForm: (itemId: String?) -> Unit,
//     onBack: () -> Unit,
// ) {
//     val viewModel: ExampleListViewModel = koinViewModel()
//     val state by viewModel.uiState.collectAsStateWithLifecycle()
//     val snackbar = remember { SnackbarHostState() }
//
//     ExampleListEffectHandler(viewModel.effect, snackbar, onNavigateToForm)
//     ExampleListScreen(state = state, snackbar = snackbar, onEvent = viewModel::onEvent, onBack = onBack)
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
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// fun ExampleListScreen(
//     state: ExampleListUiState,
//     snackbar: SnackbarHostState,
//     onEvent: (ExampleListEvent) -> Unit,
//     onBack: () -> Unit,
// ) {
//     Scaffold(
//         topBar = { TopAppBar(title = { Text("Example") }) },
//         snackbarHost = { SnackbarHost(snackbar) },
//         floatingActionButton = {
//             FloatingActionButton(onClick = { onEvent(ExampleListEvent.CreateClicked) }) {
//                 Icon(Icons.Default.Add, contentDescription = "Tambah")
//             }
//         },
//     ) { padding ->
//         when (state) {
//             ExampleListUiState.Loading -> CenteredLoader(padding)
//             is ExampleListUiState.Error -> CenteredMessage(padding, state.message)
//             ExampleListUiState.Empty -> CenteredMessage(padding, "Belum ada data")
//             is ExampleListUiState.Success -> ExampleListBody(padding, state, onEvent)
//         }
//     }
// }
//
// @Composable
// private fun ExampleListBody(
//     padding: PaddingValues,
//     state: ExampleListUiState.Success,
//     onEvent: (ExampleListEvent) -> Unit,
// ) {
//     Column(
//         modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
//         verticalArrangement = Arrangement.spacedBy(12.dp),
//     ) {
//         OutlinedTextField(
//             value = state.query,
//             onValueChange = { onEvent(ExampleListEvent.QueryChanged(it)) },
//             label = { Text("Cari...") },
//             modifier = Modifier.fillMaxWidth(),
//             singleLine = true,
//         )
//         LazyColumn(
//             modifier = Modifier.fillMaxSize(),
//             verticalArrangement = Arrangement.spacedBy(8.dp),
//         ) {
//             items(state.items, key = { it.id }) { item ->
//                 Card(modifier = Modifier.fillMaxWidth()) {
//                     // TODO: render your item here
//                     Text(item.name, modifier = Modifier.padding(16.dp))
//                 }
//             }
//         }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 4: FORM — Contract + ViewModel + Screen
// ══════════════════════════════════════════════════════════════════════════════════════════

// ─── FORM CONTRACT ──────────────────────────────────────────────────────────────────────
// Form UiState is a flat data class (not sealed). Forms have many small independent fields
// — a sealed hierarchy would explode into near-identical states.

sealed interface ExampleFormMode {
    data object Create : ExampleFormMode
    data class Edit(val itemId: String) : ExampleFormMode
}

data class ExampleFormUiState(
    val mode: ExampleFormMode = ExampleFormMode.Create,
    val name: String = "",
    val nameError: String? = null,
    // TODO: add your form fields + per-field errors here
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !isSubmitting && !isLoading && name.isNotBlank()
        // TODO: add your validation conditions
}

sealed interface ExampleFormEvent {
    data class NameChanged(val value: String) : ExampleFormEvent
    // TODO: add your field change events
    data object Submit : ExampleFormEvent
    data object Delete : ExampleFormEvent
}

sealed interface ExampleFormEffect {
    data class ShowMessage(val message: String, val type: MessageType = MessageType.Info) : ExampleFormEffect
    data object NavigateBack : ExampleFormEffect
}

// ─── FORM VIEWMODEL ─────────────────────────────────────────────────────────────────────
// Forms use combine() with 3 flows — still readable because it's just fields + 2 booleans.
// This is simpler than packing isLoading/isSubmitting into the fields data class because
// performSubmission() only touches isSubmittingFlow, not the whole state.
// performSubmission() is the shared scaffold for any mutating action.

// NOTE: This is a skeleton. Uncomment and wire to your repository.

// class ExampleFormViewModel(
//     private val repo: ExampleRepository,
//     private val itemId: String?,
// ) : ViewModel() {
//
//     private val mode: ExampleFormMode =
//         if (itemId == null) ExampleFormMode.Create else ExampleFormMode.Edit(itemId)
//
//     private val fieldsFlow = MutableStateFlow(FormFields())
//     private val isLoadingFlow = MutableStateFlow(false)
//     private val isSubmittingFlow = MutableStateFlow(false)
//
//     val uiState: StateFlow<ExampleFormUiState> = combine(
//         fieldsFlow, isLoadingFlow, isSubmittingFlow,
//     ) { fields, loading, submitting ->
//         ExampleFormUiState(
//             mode = mode,
//             name = fields.name,
//             nameError = fields.nameError,
//             // TODO: map your fields here
//             isLoading = loading,
//             isSubmitting = submitting,
//         )
//     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ExampleFormUiState(mode = mode))
//
//     private val effectChannel = Channel<ExampleFormEffect>(Channel.BUFFERED)
//     val effect: Flow<ExampleFormEffect> = effectChannel.receiveAsFlow()
//
//     // ── message shortcuts ──
//     private fun showMessage(msg: String) = effectChannel.trySend(ExampleFormEffect.ShowMessage(msg))
//     private fun showSuccess(msg: String) = effectChannel.trySend(ExampleFormEffect.ShowMessage(msg, MessageType.Success))
//     private fun showError(msg: String) = effectChannel.trySend(ExampleFormEffect.ShowMessage(msg, MessageType.Error))
//     private fun showWarning(msg: String) = effectChannel.trySend(ExampleFormEffect.ShowMessage(msg, MessageType.Warning))
//
//     init { if (itemId != null) loadExisting(itemId) }
//
//     fun onEvent(event: ExampleFormEvent) {
//         when (event) {
//             is ExampleFormEvent.NameChanged ->
//                 fieldsFlow.update { it.copy(name = event.value, nameError = null) }
//             // TODO: handle your field change events
//             ExampleFormEvent.Submit -> submit()
//             ExampleFormEvent.Delete -> delete()
//         }
//     }
//
//     private fun loadExisting(id: String) {
//         viewModelScope.launch {
//             isLoadingFlow.value = true
//             try {
//                 val item = repo.getOne(id)
//                 fieldsFlow.value = FormFields(name = item.name)
//                 // TODO: populate all fields
//             } catch (t: Throwable) {
//                 showError(t.message ?: "Gagal memuat data")
//             } finally {
//                 isLoadingFlow.value = false
//             }
//         }
//     }
//
//     private fun submit() {
//         val fields = fieldsFlow.value
//         val validation = validate(fields)
//         if (validation.hasErrors) {
//             fieldsFlow.value = fields.copy(nameError = validation.name)
//             // TODO: set all field errors
//             return
//         }
//         performSubmission(fallbackError = "Gagal menyimpan") {
//             val draft = ExampleDraft(
//                 id = (mode as? ExampleFormMode.Edit)?.itemId,
//                 name = fields.name.trim(),
//                 // TODO: map all fields
//             )
//             repo.save(draft)
//             showSuccess("Data tersimpan")
//             effectChannel.trySend(ExampleFormEffect.NavigateBack)
//         }
//     }
//
//     private fun delete() {
//         val editMode = mode as? ExampleFormMode.Edit ?: return
//         performSubmission(fallbackError = "Gagal menghapus") {
//             repo.delete(editMode.itemId)
//             showSuccess("Data dihapus")
//             effectChannel.trySend(ExampleFormEffect.NavigateBack)
//         }
//     }
//
//     // Shared scaffold for any mutating action. Flips isSubmittingFlow on/off in exactly
//     // one place and turns any thrown exception into a snackbar.
//     private fun performSubmission(fallbackError: String, action: suspend () -> Unit) {
//         viewModelScope.launch {
//             isSubmittingFlow.value = true
//             try { action() }
//             catch (t: Throwable) { showError(t.message ?: fallbackError) }
//             finally { isSubmittingFlow.value = false }
//         }
//     }
//
//     // Pure validation function. Easy to unit-test.
//     private fun validate(fields: FormFields): Validation = Validation(
//         name = if (fields.name.isBlank()) "Nama wajib diisi" else null,
//         // TODO: validate your fields
//     )
//
//     private data class FormFields(
//         val name: String = "",
//         val nameError: String? = null,
//         // TODO: add your fields + errors
//     )
//
//     private data class Validation(
//         val name: String?,
//         // TODO: add your validation fields
//     ) {
//         val hasErrors: Boolean = listOf(name).any { it != null }
//     }
// }

// ─── FORM SCREEN ────────────────────────────────────────────────────────────────────────

// @Composable
// fun ExampleFormRoute(
//     itemId: String?,
//     onDone: () -> Unit,
// ) {
//     val viewModel: ExampleFormViewModel = koinViewModel { parametersOf(itemId) }
//     val state by viewModel.uiState.collectAsStateWithLifecycle()
//     val snackbar = remember { SnackbarHostState() }
//
//     ExampleFormEffectHandler(viewModel.effect, snackbar, onDone)
//     ExampleFormScreen(state = state, snackbar = snackbar, onEvent = viewModel::onEvent, onBack = onDone)
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
//                 is ExampleFormEffect.ShowMessage -> snackbar.showSnackbar(effect.message)
//                 ExampleFormEffect.NavigateBack -> onDone()
//             }
//         }
//     }
// }
//
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// fun ExampleFormScreen(
//     state: ExampleFormUiState,
//     snackbar: SnackbarHostState,
//     onEvent: (ExampleFormEvent) -> Unit,
//     onBack: () -> Unit,
// ) {
//     Scaffold(
//         topBar = {
//             TopAppBar(
//                 title = { Text(if (state.mode is ExampleFormMode.Edit) "Edit" else "Baru") },
//                 navigationIcon = {
//                     IconButton(onClick = onBack) {
//                         Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
//                     }
//                 },
//                 actions = {
//                     if (state.mode is ExampleFormMode.Edit) {
//                         IconButton(onClick = { onEvent(ExampleFormEvent.Delete) }) {
//                             Icon(Icons.Default.Delete, contentDescription = "Hapus")
//                         }
//                     }
//                 },
//             )
//         },
//         snackbarHost = { SnackbarHost(snackbar) },
//     ) { padding ->
//         if (state.isLoading) {
//             Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
//                 CircularProgressIndicator()
//             }
//         } else {
//             Column(
//                 modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
//                     .verticalScroll(rememberScrollState()),
//                 verticalArrangement = Arrangement.spacedBy(12.dp),
//             ) {
//                 OutlinedTextField(
//                     value = state.name,
//                     onValueChange = { onEvent(ExampleFormEvent.NameChanged(it)) },
//                     label = { Text("Nama") },
//                     isError = state.nameError != null,
//                     supportingText = { state.nameError?.let { Text(it) } },
//                     modifier = Modifier.fillMaxWidth(),
//                     singleLine = true,
//                 )
//                 // TODO: add your form fields here
//
//                 Button(
//                     onClick = { onEvent(ExampleFormEvent.Submit) },
//                     enabled = state.canSubmit,
//                     modifier = Modifier.fillMaxWidth(),
//                 ) {
//                     Text(if (state.isSubmitting) "Menyimpan..." else "Simpan")
//                 }
//             }
//         }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 5: WRAPPER ROUTE — Crossfade-based navigation (no nav library)
// ══════════════════════════════════════════════════════════════════════════════════════════
// This is the top-level entry point that combines list + form via Crossfade.
// refreshToken is incremented on form exit so the list reloads.

// @Composable
// fun ExampleRoute(onBack: () -> Unit = {}) {
//     var stage by rememberSaveable { mutableStateOf<ExampleStage>(ExampleStage.List) }
//     var refreshToken by remember { mutableStateOf(0) }
//
//     Crossfade(targetState = stage) { current ->
//         when (current) {
//             ExampleStage.List -> ExampleListRoute(
//                 onNavigateToForm = { id -> stage = ExampleStage.Form(id) },
//                 onBack = onBack,
//                 // pass refreshToken to trigger LaunchedEffect(refreshToken) { reload }
//             )
//             is ExampleStage.Form -> ExampleFormRoute(
//                 itemId = current.id,
//                 onDone = { refreshToken++; stage = ExampleStage.List },
//             )
//         }
//     }
// }
//
// private sealed interface ExampleStage {
//     data object List : ExampleStage
//     data class Form(val id: String?) : ExampleStage
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SHARED UI HELPERS — used across list and form screens
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
