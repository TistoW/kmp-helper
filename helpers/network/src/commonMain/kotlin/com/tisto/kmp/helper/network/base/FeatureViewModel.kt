package com.tisto.kmp.helper.network.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tisto.kmp.helper.network.MessageType
import com.tisto.kmp.helper.network.utils.Resource
import com.tisto.kmp.helper.network.utils.onResource
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Minimal base for MVI/UDF feature ViewModels. Solves two repetitions only:
 *
 *  1. Effect channel boilerplate (Channel + receiveAsFlow + trySend wrappers).
 *  2. `viewModelScope.launch { repo.x().onResource(...) }` → `.launchResource { }`.
 *
 * Everything else stays in the subclass: UiState (sealed / data class),
 * Event sealed interface, any `combine`/`stateIn` for Form, etc. No `BaseUiState<T>`,
 * no pagination wrapper, no scroll state baggage.
 *
 * Contract for toast shortcuts: override `showMessage(msg, type)` once to emit
 * the feature's own `Effect.ShowMessage`. `showSuccess` / `showError` delegate to it.
 *
 *     class ExampleListViewModel(repo: ExampleRepository) :
 *         FeatureViewModel<ExampleListEffect>() {
 *
 *         override fun showMessage(msg: String, type: MessageType) {
 *             sendEffect(ExampleListEffect.ShowMessage(msg, type))
 *         }
 *
 *         private fun delete(id: String) {
 *             repo.delete(id).launchResource(fallbackError = "Gagal menghapus") {
 *                 showSuccess("Data berhasil dihapus"); refresh()
 *             }
 *         }
 *     }
 */
abstract class FeatureViewModel<EFFECT> : ViewModel() {

    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val effect: Flow<EFFECT> = _effect.receiveAsFlow()

    protected fun sendEffect(effect: EFFECT) = _effect.trySend(effect)

    /** Override once per feature to route toast through the feature's Effect.ShowMessage. */
    protected open fun showMessage(msg: String, type: MessageType = MessageType.Info) = Unit
    protected fun showSuccess(msg: String) = showMessage(msg, MessageType.Success)
    protected fun showError(msg: String) = showMessage(msg, MessageType.Error)

    /**
     * Fold `viewModelScope.launch { onResource(...) }` into one call.
     * Default `onError` routes to `showError` — override via the param when
     * a call site needs inline state mutation (e.g. setting UiState.Error).
     */
    protected fun <T> Flow<Resource<T>>.launchResource(
        fallbackError: String = "Terjadi kesalahan",
        onError: (String) -> Unit = ::showError,
        onSuccess: (T) -> Unit,
    ): Job = viewModelScope.launch {
        onResource(fallbackError, onError, onSuccess)
    }
}
