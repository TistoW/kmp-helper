package com.tisto.kmp.helper.ui.boilerplate.sample.presentation.list

import com.tisto.kmp.helper.network.MessageType
import com.tisto.kmp.helper.ui.boilerplate.sample.data.model.Sample

// ══════════════════════════════════════════════════════════════════════════
// LIST CONTRACT — UiState/Event generic dari ListUiState<T>/ListEvent<T>.
// Hanya Effect yang feature-specific.
//
// TIDAK membuat per-feature UiState/Event. Import dari:
//   com.tisto.kmp.helper.ui.component.ListUiState
//   com.tisto.kmp.helper.ui.component.ListEvent
// ══════════════════════════════════════════════════════════════════════════

internal sealed interface SampleListEffect {
    data class ShowMessage(
        val message: String,
        val type: MessageType = MessageType.Info,
    ) : SampleListEffect

    data class NavigateToForm(val item: Sample?) : SampleListEffect
}
