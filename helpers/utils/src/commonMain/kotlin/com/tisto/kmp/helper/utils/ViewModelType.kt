package com.tisto.kmp.helper.utils

enum class SnackbarType { SUCCESS, ERROR, WARNING, INFO }

object ScreenTypes {
    const val list = "list"
    const val form = "create"
}

object SuccessTypes {
    const val create = "create"
    const val update = "update"
    const val delete = "delete"
}

sealed interface UiEvent {
    data class Error(val message: String) : UiEvent
    data class Success(val message: String) : UiEvent
    data class Navigate(val route: String) : UiEvent
    data class Action(val action: String) : UiEvent
    data class Snackbar(val message: String, val type: SnackbarType = SnackbarType.SUCCESS) :
        UiEvent
}

interface BaseState<R, I, SELF : BaseState<R, I, SELF>> {
    val items: List<I>
    val request: R?
    val item: I?

    fun copies(
        items: List<I> = this.items,
        request: R? = this.request,
        item: I? = this.item
    ): SELF
}

sealed interface UiEffect {
    data class Toast(
        val message: String,
        val type: SnackbarType = SnackbarType.INFO // default value
    ) : UiEffect
}

class StateHandler<STATE, R, I>(
    private val getState: () -> STATE?,
    private val setState: (STATE.() -> STATE) -> Unit
) where STATE : BaseState<R, I, STATE> {

    fun getItems(): List<I> = getState()?.items ?: emptyList()

    fun getRequest(): R? = getState()?.request

    fun getItem(): I? = getState()?.item

    fun updateItems(update: List<I>.() -> List<I>) {
        setState { copies(items = items.update()) }
    }

    fun updateItem(update: I.() -> I?) {
        setState { copies(item = item?.update()) }
    }

    fun updateRequest(update: R.() -> R) {
        setState { copies(request = request?.update()) }
    }

}
