@file:Suppress("unused", "RedundantVisibilityModifier")

package com.tisto.kmp.helper.ui.boilerplate

// ══════════════════════════════════════════════════════════════════════════════════════════
// BOILERPLATE EXAMPLE — Full concrete reference for the MVI/UDF pattern.
//
// This file is COMPLETE — every section is filled out, no TODOs, no placeholders.
// It mirrors the canonical Category feature 1:1. Read this alongside Boilerplate.kt
// (which holds the rules) when generating a new feature so output matches what
// already ships in `compose/feature/product/.../categoryNew/`.
//
// Canonical reference in the codebase:
//   data      → compose/feature/product/src/main/java/com/zenenta/pos/feature/product/data/
//                 ├── model/Category.kt
//                 ├── request/CategoryRequest.kt
//                 ├── service/CategoryApi.kt
//                 ├── repository/CategoryRepository.kt
//                 └── di/composeProductModule.kt
//   ui        → compose/feature/product/src/main/java/com/zenenta/pos/feature/product/presentation/categoryNew/
//                 ├── CategoryRoute.kt
//                 ├── list/{Contract,ViewModel,Screen}.kt
//                 └── form/{Contract,ViewModel,Screen}.kt
//
// Because this file sits in the :helpers:ui KMP module, it cannot import Ktor,
// Koin, or :helpers:network apiCall/Resource types. Data-layer and VM/Screen
// sections therefore live inside comment blocks so the module still compiles.
// The code inside those blocks is real, production-shaped, and meant to be
// copied verbatim into a feature module — only find-and-replace "Example".
// ══════════════════════════════════════════════════════════════════════════════════════════

import com.tisto.kmp.helper.ui.MessageType
import kotlinx.serialization.Serializable

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 1 — DOMAIN MODEL  (data/model/Example.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Domain model doubles as the wire DTO (no separate ExampleDto). Use default
// values + nullability so partial JSON responses parse cleanly.

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
// Outbound body for create/update. `pickedImage` is @Transient — it's not
// JSON-serialised; helper-network's postMethod/putMethod picks it up to send
// a multipart payload when present.
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
//     @Transient val pickedImage: PickedImage? = null, // ✅ tidak ikut serialized
// )

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 3 — API SERVICE  (data/service/ExampleApi.kt)   [Ktor — MANDATORY]
// ══════════════════════════════════════════════════════════════════════════════════════════
// Ktor HttpClient only. One method per endpoint. Return BaseResponse<T>
// unchanged — unwrapping happens via apiCall() in the repository.
// postMethod / putMethod accept `pickedImage` for multipart uploads.
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
//     // CREATE
//     suspend fun create(body: ExampleRequest?): BaseResponse<Example> = client.postMethod(
//         url = "$v2/example",
//         body = body,
//         pickedImage = body?.pickedImage,
//     )
//
//     // READ (list + query)
//     suspend fun get(search: SearchRequest? = null): BaseResponse<List<Example>> = client.getMethod(
//         url = "$v2/example",
//         query = search?.convertToQuery(),
//     )
//
//     // READ (one)
//     suspend fun getOne(id: String): BaseResponse<Example> = client.getMethod(
//         url = "$v2/example/$id",
//     )
//
//     // UPDATE
//     suspend fun update(body: ExampleRequest?): BaseResponse<Example> = client.putMethod(
//         url = "$v2/example/${body?.id}",
//         body = body,
//         pickedImage = body?.pickedImage,
//     )
//
//     // DELETE
//     suspend fun delete(id: String): BaseResponse<Example> = client.deleteMethod(
//         url = "$v2/example/$id",
//     )
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 4 — REPOSITORY  (data/repository/ExampleRepository.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Thin wrapper — each method delegates to Api via `apiCall(...)`, which returns
// Flow<Resource<T>> (Loading → Success | Error). No interfaces. No try/catch
// here; Resource.Error carries failures to the VM.
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
// SECTION 5 — KOIN MODULE  (data/di/exampleModule.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// One module per feature. Api + Repo as singletons. Both VMs registered.
// Form VM takes itemId as a runtime parameter (nullable — null = Create).
//
// After creating the module, add it to app/.../core/di/ListModules.kt:
//     val modules = listOf( ..., exampleModule, ... )
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
// SECTION 6 — LIST CONTRACT  (presentation/example/list/ExampleListContract.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════

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

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 7 — LIST VIEWMODEL  (presentation/example/list/ExampleListViewModel.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Single MutableStateFlow. Direct .value assignment. `query` is a plain var
// because the only trigger is user action. No combine(). No flatMapLatest.
//
// Repo calls go through the `.onResource(...)` terminal operator (helper-network)
// which bundles try/catch + when(Success/Error/Loading) into success + error
// callbacks. NEVER write `.collect { when (r) { Success/Error/Loading ... } }`
// manually — always use onResource.
//
// import androidx.lifecycle.ViewModel
// import androidx.lifecycle.viewModelScope
// import com.tisto.kmp.helper.network.model.Search
// import com.tisto.kmp.helper.network.model.SearchRequest
// import com.tisto.kmp.helper.network.utils.onResource
// import com.tisto.kmp.helper.ui.MessageType
// import kotlinx.coroutines.channels.Channel
// import kotlinx.coroutines.flow.Flow
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.flow.receiveAsFlow
// import kotlinx.coroutines.launch
//
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
//     private fun showError(msg: String)   = effectChannel.trySend(ExampleListEffect.ShowMessage(msg, MessageType.Error))
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
//             _uiState.value = if (current is ExampleListUiState.Success) current.copy(isRefreshing = true)
//                 else ExampleListUiState.Loading
//
//             val search = SearchRequest(
//                 page = 1,
//                 perpage = 100,
//                 simpleQuery = buildList {
//                     add(Search("isActive", "true"))
//                     if (query.isNotBlank()) add(Search("name", query))
//                 },
//             )
//             repo.get(search).onResource(
//                 fallbackError = "Gagal memuat data",
//                 onError = { _uiState.value = ExampleListUiState.Error(it) },
//                 onSuccess = { items ->
//                     _uiState.value = if (items.isEmpty()) ExampleListUiState.Empty
//                         else ExampleListUiState.Success(items = items, query = query)
//                 },
//             )
//         }
//     }
//
//     private fun delete(id: String) {
//         viewModelScope.launch {
//             repo.delete(id).onResource(
//                 fallbackError = "Gagal menghapus",
//                 onError = ::showError,
//                 onSuccess = { showSuccess("Data berhasil dihapus"); refresh() },
//             )
//         }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 8 — LIST SCREEN  (presentation/example/list/ExampleListScreen.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Route (stateful) = resolves VM via safeKoinViewModel, owns snackbar state,
// collects effects. Screen (stateless) = pure (state, snackbar, onEvent, onBack).
// refreshToken is driven by the wrapper Route; when > 0 we dispatch Refresh.
//
// import androidx.compose.foundation.clickable
// import androidx.compose.foundation.layout.*
// import androidx.compose.foundation.lazy.LazyColumn
// import androidx.compose.foundation.lazy.items
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.automirrored.filled.ArrowBack
// import androidx.compose.material.icons.filled.Add
// import androidx.compose.material.icons.filled.Delete
// import androidx.compose.material.icons.filled.Edit
// import androidx.compose.material3.*
// import androidx.compose.runtime.*
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import com.tisto.kmp.helper.ui.component.CustomImageView
// import com.tisto.kmp.helper.ui.ext.safeKoinViewModel
// import com.tisto.kmp.helper.utils.ext.def
// import com.tisto.kmp.helper.utils.ext.formatDate
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
//         topBar = {
//             TopAppBar(
//                 title = { Text("Example") },
//                 navigationIcon = {
//                     IconButton(onClick = onBack) {
//                         Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
//                     }
//                 },
//             )
//         },
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
//             items(state.items, key = { it.id.def() }) { item ->
//                 ExampleCard(
//                     item = item,
//                     onEdit = { onEvent(ExampleListEvent.EditClicked(item.id)) },
//                     onDelete = { onEvent(ExampleListEvent.DeleteConfirmed(item.id)) },
//                 )
//             }
//         }
//     }
// }
//
// @Composable
// private fun ExampleCard(
//     item: Example,
//     onEdit: () -> Unit,
//     onDelete: () -> Unit,
// ) {
//     Card(modifier = Modifier.fillMaxWidth().clickable { onEdit() }) {
//         Row(
//             modifier = Modifier.fillMaxWidth().padding(12.dp),
//             verticalAlignment = Alignment.CenterVertically,
//         ) {
//             CustomImageView(imageUrl = item.image, size = 50.dp)
//             Spacer(Modifier.width(12.dp))
//             Column(modifier = Modifier.weight(1f)) {
//                 Text(item.name.def(), style = MaterialTheme.typography.bodyLarge)
//                 item.updatedAt?.formatDate("dd MMM yyyy")?.let {
//                     Text(
//                         it,
//                         style = MaterialTheme.typography.bodySmall,
//                         color = MaterialTheme.colorScheme.onSurfaceVariant,
//                     )
//                 }
//             }
//             IconButton(onClick = onEdit) {
//                 Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(20.dp))
//             }
//             IconButton(onClick = onDelete) {
//                 Icon(Icons.Default.Delete, contentDescription = "Hapus", modifier = Modifier.size(20.dp))
//             }
//         }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 9 — FORM CONTRACT  (presentation/example/form/ExampleFormContract.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Flat data-class UiState (not sealed). Forms have many small independent
// fields — a sealed hierarchy would explode into near-identical states.
// `mode` distinguishes Create vs Edit; `image` is server URL, `pickedImage`
// is a newly-picked local file awaiting upload.
//
// import com.tisto.kmp.helper.ui.MessageType
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
// SECTION 10 — FORM VIEWMODEL  (presentation/example/form/ExampleFormViewModel.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Three source flows combined via combine(): fieldsFlow, isLoadingFlow,
// isSubmittingFlow. performSubmission() is the shared scaffold for any
// mutating action — it flips isSubmittingFlow on/off in exactly one place
// and turns thrown exceptions into snackbars. Inner FormFields + Validation
// classes keep field state and validation results private to the VM.
//
// import androidx.lifecycle.ViewModel
// import androidx.lifecycle.viewModelScope
// import com.tisto.kmp.helper.network.utils.onResource
// import com.tisto.kmp.helper.ui.MessageType
// import com.tisto.kmp.helper.utils.ext.def
// import com.tisto.kmp.helper.utils.model.PickedImage
// import kotlinx.coroutines.channels.Channel
// import kotlinx.coroutines.flow.Flow
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.SharingStarted
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.combine
// import kotlinx.coroutines.flow.receiveAsFlow
// import kotlinx.coroutines.flow.stateIn
// import kotlinx.coroutines.flow.update
// import kotlinx.coroutines.launch
//
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
//             description = fields.description,
//             image = fields.image,
//             pickedImage = fields.pickedImage,
//             isActive = fields.isActive,
//             nameError = fields.nameError,
//             isLoading = loading,
//             isSubmitting = submitting,
//         )
//     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ExampleFormUiState(mode = mode))
//
//     private val effectChannel = Channel<ExampleFormEffect>(Channel.BUFFERED)
//     val effect: Flow<ExampleFormEffect> = effectChannel.receiveAsFlow()
//
//     // ── message shortcuts ──
//     private fun showSuccess(msg: String) = effectChannel.trySend(ExampleFormEffect.ShowMessage(msg, MessageType.Success))
//     private fun showError(msg: String)   = effectChannel.trySend(ExampleFormEffect.ShowMessage(msg, MessageType.Error))
//
//     init { if (itemId != null) loadExisting(itemId) }
//
//     fun onEvent(event: ExampleFormEvent) {
//         when (event) {
//             is ExampleFormEvent.NameChanged ->
//                 fieldsFlow.update { it.copy(name = event.value, nameError = null) }
//             is ExampleFormEvent.DescriptionChanged ->
//                 fieldsFlow.update { it.copy(description = event.value) }
//             is ExampleFormEvent.ImagePicked ->
//                 fieldsFlow.update { it.copy(pickedImage = event.image) }
//             is ExampleFormEvent.ActiveChanged ->
//                 fieldsFlow.update { it.copy(isActive = event.value) }
//             ExampleFormEvent.Submit -> submit()
//             ExampleFormEvent.Delete -> delete()
//         }
//     }
//
//     private fun loadExisting(id: String) {
//         viewModelScope.launch {
//             isLoadingFlow.value = true
//             repo.getOne(id).onResource(
//                 fallbackError = "Gagal memuat data",
//                 onError = ::showError,
//                 onSuccess = { item ->
//                     fieldsFlow.value = FormFields(
//                         name = item.name.def(),
//                         description = item.description.def(),
//                         image = item.image,
//                         isActive = item.isActive,
//                     )
//                 },
//             )
//             isLoadingFlow.value = false
//         }
//     }
//
//     private fun submit() {
//         val fields = fieldsFlow.value
//         val validation = validate(fields)
//         if (validation.hasErrors) {
//             fieldsFlow.value = fields.copy(nameError = validation.name)
//             return
//         }
//         val request = ExampleRequest(
//             id = (mode as? ExampleFormMode.Edit)?.itemId,
//             name = fields.name.trim(),
//             description = fields.description.trim().ifBlank { null },
//             image = fields.image,
//             isActive = fields.isActive,
//             pickedImage = fields.pickedImage,
//         )
//         performSubmission {
//             val flow = if (mode is ExampleFormMode.Create) repo.create(request) else repo.update(request)
//             flow.onResource(
//                 fallbackError = "Gagal menyimpan",
//                 onError = ::showError,
//                 onSuccess = {
//                     showSuccess("Data tersimpan")
//                     effectChannel.trySend(ExampleFormEffect.NavigateBack)
//                 },
//             )
//         }
//     }
//
//     private fun delete() {
//         val editMode = mode as? ExampleFormMode.Edit ?: return
//         performSubmission {
//             repo.delete(editMode.itemId).onResource(
//                 fallbackError = "Gagal menghapus",
//                 onError = ::showError,
//                 onSuccess = {
//                     showSuccess("Data berhasil dihapus")
//                     effectChannel.trySend(ExampleFormEffect.NavigateBack)
//                 },
//             )
//         }
//     }
//
//     // Shared scaffold for any mutating action. onResource already handles
//     // try/catch + error reporting, so this just owns isSubmittingFlow bookkeeping.
//     private fun performSubmission(action: suspend () -> Unit) {
//         viewModelScope.launch {
//             isSubmittingFlow.value = true
//             try { action() } finally { isSubmittingFlow.value = false }
//         }
//     }
//
//     // Pure validation function. Easy to unit-test.
//     private fun validate(fields: FormFields): Validation = Validation(
//         name = if (fields.name.isBlank()) "Nama wajib diisi" else null,
//     )
//
//     private data class FormFields(
//         val name: String = "",
//         val description: String = "",
//         val image: String? = null,
//         val pickedImage: PickedImage? = null,
//         val isActive: Boolean = true,
//         val nameError: String? = null,
//     )
//
//     private data class Validation(val name: String?) {
//         val hasErrors: Boolean = listOf(name).any { it != null }
//     }
// }

// ══════════════════════════════════════════════════════════════════════════════════════════
// SECTION 11 — FORM SCREEN  (presentation/example/form/ExampleFormScreen.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// safeKoinViewModel(itemId.toString()) — the stringified itemId is the VM key
// so Create ("null") and Edit ("<id>") get distinct VM instances.
//
// import androidx.compose.foundation.layout.*
// import androidx.compose.foundation.rememberScrollState
// import androidx.compose.foundation.verticalScroll
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.automirrored.filled.ArrowBack
// import androidx.compose.material.icons.filled.Delete
// import androidx.compose.material3.*
// import androidx.compose.runtime.*
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import com.tisto.kmp.helper.ui.component.CardImagePicker
// import com.tisto.kmp.helper.ui.ext.safeKoinViewModel
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
//                 title = { Text(if (state.mode is ExampleFormMode.Edit) "Edit Example" else "Tambah Example") },
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
//                 modifier = Modifier
//                     .fillMaxSize()
//                     .padding(padding)
//                     .padding(16.dp)
//                     .verticalScroll(rememberScrollState()),
//                 horizontalAlignment = Alignment.CenterHorizontally,
//                 verticalArrangement = Arrangement.spacedBy(12.dp),
//             ) {
//                 CardImagePicker(
//                     modifier = Modifier.width(120.dp).height(120.dp),
//                     imageUrl = state.image,
//                     onPicker = { onEvent(ExampleFormEvent.ImagePicked(it)) },
//                 )
//
//                 OutlinedTextField(
//                     value = state.name,
//                     onValueChange = { onEvent(ExampleFormEvent.NameChanged(it)) },
//                     label = { Text("Nama") },
//                     isError = state.nameError != null,
//                     supportingText = state.nameError?.let { { Text(it) } },
//                     modifier = Modifier.fillMaxWidth(),
//                     singleLine = true,
//                 )
//
//                 OutlinedTextField(
//                     value = state.description,
//                     onValueChange = { onEvent(ExampleFormEvent.DescriptionChanged(it)) },
//                     label = { Text("Deskripsi") },
//                     minLines = 3,
//                     modifier = Modifier.fillMaxWidth(),
//                 )
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
// SECTION 12 — WRAPPER ROUTE  (presentation/example/ExampleRoute.kt)
// ══════════════════════════════════════════════════════════════════════════════════════════
// Crossfade-based nav (no nav library). `refreshToken` is incremented when
// the form exits so the list's LaunchedEffect(refreshToken) dispatches a
// Refresh event.
//
// import androidx.compose.animation.Crossfade
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableIntStateOf
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.saveable.rememberSaveable
// import androidx.compose.runtime.setValue
//
// @Composable
// fun ExampleRoute(onBack: () -> Unit = {}) {
//     var stage by rememberSaveable { mutableStateOf<Stage>(Stage.List) }
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
