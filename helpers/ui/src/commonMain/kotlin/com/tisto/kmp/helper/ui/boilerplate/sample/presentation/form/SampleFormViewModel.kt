package com.tisto.kmp.helper.ui.boilerplate.sample.presentation.form

import com.tisto.kmp.helper.network.MessageType
import com.tisto.kmp.helper.network.base.FeatureViewModel
import com.tisto.kmp.helper.ui.boilerplate.sample.data.SampleRepository
import com.tisto.kmp.helper.ui.boilerplate.sample.data.model.Sample
import com.tisto.kmp.helper.ui.boilerplate.sample.data.request.SampleRequest
import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.SampleStrings
import com.tisto.kmp.helper.utils.ext.def
import com.tisto.kmp.helper.utils.ext.digitsOnly
import com.tisto.kmp.helper.utils.ext.formatNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ══════════════════════════════════════════════════════════════════════════
// FORM VIEWMODEL
//
// Constructor param: initialItem. null = Create, non-null = Edit(initialItem.id).
// Pre-populate UiState dari initialItem di MutableStateFlow constructor.
// init { if (initialItem != null) refreshInBackground(initialItem.id) }
//
// Key patterns:
//   - Single MutableStateFlow<FormUiState>. Tanpa combine/stateIn/flatMapLatest.
//   - setSubmitting(Boolean) helper — panggil sebelum .launchResource().
//   - refreshInBackground: silent fetch, hanya update fields yang belum diubah user.
//   - On NameChanged: clear nameError = null.
//   - Success: sendEffect(NavigateBack(message)), BUKAN showSuccess() sebelum NavigateBack.
// ══════════════════════════════════════════════════════════════════════════

internal class SampleFormViewModel(
    private val repo: SampleRepository,
    private val initialItem: Sample?,
) : FeatureViewModel<SampleFormEffect>() {

    private val mode: SampleFormMode =
        if (initialItem == null) SampleFormMode.Create
        else SampleFormMode.Edit(initialItem.id)

    private val _uiState = MutableStateFlow(
        SampleFormUiState(
            mode = mode,
            name = initialItem?.name.orEmpty(),
            description = initialItem?.description.orEmpty(),
            imageUrl = initialItem?.image,
            isActive = initialItem?.isActive ?: true,
        )
    )
    val uiState: StateFlow<SampleFormUiState> = _uiState.asStateFlow()

    // ── setSubmitting helper ────────────────────────────────────────────
    // Panggil sebelum .launchResource(), reset di onError dan onSuccess.
    // Tanpa performSubmission wrapper, tanpa try/finally.

    private fun setSubmitting(isSubmitting: Boolean) {
        _uiState.update { it.copy(isSubmitting = isSubmitting) }
    }

    override fun showMessage(msg: String, type: MessageType) {
        sendEffect(SampleFormEffect.ShowMessage(msg, type))
    }

    init {
        if (initialItem != null) refreshInBackground(initialItem.id)
    }

    // ── Event handler ───────────────────────────────────────────────────

    fun onEvent(event: SampleFormEvent) {
        when (event) {
            is SampleFormEvent.NameChanged -> _uiState.update {
                it.copy(name = event.value, nameError = null) // clear error saat user ketik
            }

            is SampleFormEvent.DescriptionChanged -> _uiState.update {
                it.copy(description = event.value)
            }

            is SampleFormEvent.PriceChanged -> _uiState.update {
                it.copy(price = event.value.digitsOnly())
            }

            is SampleFormEvent.ImagePicked -> _uiState.update {
                it.copy(pickedImage = event.value)
            }

            is SampleFormEvent.ActiveChanged -> _uiState.update {
                it.copy(isActive = event.value)
            }

            SampleFormEvent.Submit -> submit()
            SampleFormEvent.Delete -> delete()
        }
    }

    // ── Refresh in background ───────────────────────────────────────────
    // Fetch fresh data, hanya update fields yang belum diubah user.
    // Tanpa viewModelScope.launch, tanpa try/catch — launchResource handles both.

    private fun refreshInBackground(id: String) {
        repo.getOne(id).launchResource(
            fallbackError = SampleStrings.loadFailed,
            onError = { /* silent — kita sudah punya data dari list */ },
        ) { item ->
            val current = _uiState.value
            _uiState.update {
                it.copy(
                    name = if (current.name == initialItem?.name.orEmpty()) item.name else current.name,
                    description = if (current.description == initialItem?.description.orEmpty()) item.description.orEmpty() else current.description,
                    price = if (current.price == (initialItem?.let { formatNumber(it.price.def()) }
                            .orEmpty())) formatNumber(item.price.def()) else current.price,
                    imageUrl = if (current.pickedImage == null) item.image else current.imageUrl,
                    isActive = if (current.isActive == initialItem?.isActive) item.isActive else current.isActive,
                )
            }
        }
    }

    // ── Submit ──────────────────────────────────────────────────────────

    private fun submit() {
        val state = _uiState.value

        // Validasi
        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = SampleStrings.errName) }
            return
        }

        setSubmitting(true)

        val request = SampleRequest(
            id = (mode as? SampleFormMode.Edit)?.itemId,
            name = state.name.trim(),
            description = state.description.trim().takeIf { it.isNotEmpty() },
            image = state.imageUrl,
            isActive = state.isActive,
            pickedImage = state.pickedImage,
        )

        val flow = if (mode is SampleFormMode.Create) repo.create(request) else repo.update(request)

        flow.launchResource(
            fallbackError = SampleStrings.saveFailed,
            onError = { setSubmitting(false); showError(it) },
        ) {
            setSubmitting(false)
            // PENTING: NavigateBack membawa message, BUKAN showSuccess() dulu
            sendEffect(SampleFormEffect.NavigateBack(SampleStrings.saved))
        }
    }

    // ── Delete ──────────────────────────────────────────────────────────

    private fun delete() {
        val editMode = mode as? SampleFormMode.Edit ?: return
        setSubmitting(true)

        repo.delete(editMode.itemId).launchResource(
            fallbackError = SampleStrings.deleteFailed,
            onError = { setSubmitting(false); showError(it) },
        ) {
            setSubmitting(false)
            sendEffect(SampleFormEffect.NavigateBack(SampleStrings.deleted))
        }
    }
}
