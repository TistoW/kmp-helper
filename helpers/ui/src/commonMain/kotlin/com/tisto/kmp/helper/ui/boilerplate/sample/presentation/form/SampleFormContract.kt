package com.tisto.kmp.helper.ui.boilerplate.sample.presentation.form

import com.tisto.kmp.helper.network.MessageType
import com.tisto.kmp.helper.utils.model.PickedImage

// ══════════════════════════════════════════════════════════════════════════
// FORM CONTRACT
//
// Mode: Create (initialItem == null) vs Edit(itemId).
// UiState: single data class dengan semua field + form state.
// canSubmit: derived property — tidak perlu manual track.
// Event: per-field Changed + Submit + Delete.
// Effect: ShowMessage + NavigateBack(message).
//
// PENTING: NavigateBack membawa message. JANGAN panggil showSuccess()
// sebelum NavigateBack — form screen sudah di-dispose sebelum snackbar muncul.
// Message di-forward ke list screen via pendingMessage.
// ══════════════════════════════════════════════════════════════════════════

// ── Mode ────────────────────────────────────────────────────────────────

internal sealed interface SampleFormMode {
    data object Create : SampleFormMode
    data class Edit(val itemId: String) : SampleFormMode
}

// ── UiState ─────────────────────────────────────────────────────────────

internal data class SampleFormUiState(
    val mode: SampleFormMode = SampleFormMode.Create,
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val imageUrl: String? = null,
    val pickedImage: PickedImage? = null,
    val isActive: Boolean = true,
    val nameError: String? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
) {
    val canSubmit: Boolean get() = !isSubmitting && !isLoading && name.isNotBlank()
}

// ── Event ───────────────────────────────────────────────────────────────

internal sealed interface SampleFormEvent {
    data class NameChanged(val value: String) : SampleFormEvent
    data class DescriptionChanged(val value: String) : SampleFormEvent
    data class PriceChanged(val value: String) : SampleFormEvent
    data class ImagePicked(val value: PickedImage?) : SampleFormEvent
    data class ActiveChanged(val value: Boolean) : SampleFormEvent
    data object Submit : SampleFormEvent
    data object Delete : SampleFormEvent
}

// ── Effect ──────────────────────────────────────────────────────────────

internal sealed interface SampleFormEffect {
    data class ShowMessage(
        val message: String,
        val type: MessageType = MessageType.Info,
    ) : SampleFormEffect

    data class NavigateBack(val message: String? = null) : SampleFormEffect
}
