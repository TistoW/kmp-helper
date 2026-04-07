package com.tisto.kmp.helper.android.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tisto.kmp.helper.utils.SnackbarType
import com.tisto.kmp.helper.utils.UiEffect
import com.tisto.kmp.helper.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//abstract class BaseRetrofitViewModel<STATE> : ViewModel() {
//
//    protected abstract fun initialState(): STATE
//
//    private val _event = MutableSharedFlow<UiEvent>()
//    val event = _event.asSharedFlow()
//
//    protected val _uiState = MutableStateFlow(BaseUiState(data = initialState()))
//    val uiState: StateFlow<BaseUiState<STATE>> = _uiState.asStateFlow()
//
//    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
//    val effect: Flow<UiEffect> = _effect.receiveAsFlow()
//
//    protected fun sendToast(message: String, type: SnackbarType = SnackbarType.INFO) {
//        viewModelScope.launch { _effect.send(UiEffect.Toast(message, type)) }
//    }
//
//    protected fun emitError(message: String) {
//        viewModelScope.launch {
//            _event.emit(UiEvent.Error(message))
//        }
//    }
//
//    protected fun emitSuccess(message: String) {
//        viewModelScope.launch {
//            _event.emit(UiEvent.Success(message))
//        }
//    }
//
//    protected fun emitAction(message: String) {
//        viewModelScope.launch {
//            _event.emit(UiEvent.Action(message))
//        }
//    }
//
//    protected fun emitSnackBar(message: String, type: SnackbarType) {
//        viewModelScope.launch {
//            _event.emit(UiEvent.Snackbar(message, type))
//        }
//    }
//
//    fun toastSuccess(message: String) {
//        sendToast(message, SnackbarType.SUCCESS)
//    }
//
//    fun toastError(message: String) {
//        sendToast(message, SnackbarType.ERROR)
//    }
//
//    fun toastInfo(message: String) {
//        sendToast(message, SnackbarType.INFO)
//    }
//
//    fun toastWaring(message: String) {
//        sendToast(message, SnackbarType.WARNING)
//    }
//
//    fun updateUiState(
//        reducer: BaseUiState<STATE>.() -> BaseUiState<STATE>
//    ) {
//        _uiState.update { it.reducer() }
//    }
//
//    fun updateState(
//        reducer: STATE.() -> STATE
//    ) {
//        _uiState.update {
//            it.copy(data = it.data?.reducer())
//        }
//    }
//
//    protected fun setLoadingProcess(value: Boolean) {
//        updateUiState { copy(isLoadingProcess = value) }
//    }
//
//    protected fun setLoading(value: Boolean) {
//        _uiState.update { it.copy(isLoading = value) }
//    }
//
//    fun navigate(screen: String) {
//        _uiState.update { it.copy(screen = screen) }
//    }
//
//    protected fun <T> Flow<ResourceRetrofit<T>>.collectResource(
//        isLoading: Boolean = true,
//        toastError: Boolean = true,
//        onError: (String) -> Unit = {},
//        onComplete: () -> Unit = {},
//        onSuccessAllRes: (ResourceRetrofit<T>) -> Unit = {},
//        onSuccess: (T) -> Unit = {},
//    ) {
//        viewModelScope.launch {
//            try {
//                this@collectResource
//                    .catch { e -> onError(e.message ?: "Unknown Error") }
//                    .collect { res ->
//                        when (res.state) {
//                            State.LOADING -> if (isLoading) setLoading(true)
//                            State.SUCCESS -> {
//                                setLoading(false)
//                                setLoadingProcess(false)
//                                onSuccessAllRes(res)
//                                res.body?.let { onSuccess(it) } ?: onError("Data is null")
//                            }
//
//                            State.ERROR -> {
//                                setLoading(false)
//                                setLoadingProcess(false)
//                                val message = res.message
//                                if (toastError) toastError(message)
//                                onError(message)
//                            }
//                        }
//                    }
//            } catch (e: Exception) {
//                setLoading(false)
//                setLoadingProcess(false)
//                val message = e.message ?: "Unknown error"
//                if (toastError) toastError(message)
//                onError(e.message ?: "Exception error")
//            } finally {
//                onComplete()
//            }
//        }
//    }
//
//    fun <T> onLoaded(page: Int, currentItems: List<T>, items: ResourceRetrofit<List<T>>) {
//        val mItems = items.body ?: listOf()
//        updateUiState {
//            copy(
//                page = page,
//                hasMore = mItems.size >= pageLimit,
//                totalPage = items.lastPage,
//                totalSize = items.total,
//                loadingSize = mItems.size,
//                loadedCount = currentItems.size + mItems.size
//            )
//        }
//    }
//
//}
