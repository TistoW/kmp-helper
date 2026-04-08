package com.tisto.kmp.helper.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tisto.kmp.helper.network.base.BaseViewModel
import com.tisto.kmp.helper.ui.component.showError
import com.tisto.kmp.helper.ui.component.showInfo
import com.tisto.kmp.helper.ui.component.showSuccess
import com.tisto.kmp.helper.ui.component.showWarning
import com.tisto.kmp.helper.utils.SnackbarType
import com.tisto.kmp.helper.utils.UiEffect

@Composable
fun <REQ> ObserveUiEffect(
    vm: BaseViewModel<REQ>,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.effect.collect { e ->
            when (e) {
                is UiEffect.Toast -> {
                    when (e.type) {
                        SnackbarType.SUCCESS -> uiState.snackbarHostState.showSuccess(e.message)
                        SnackbarType.ERROR -> uiState.snackbarHostState.showError(e.message)
                        SnackbarType.WARNING -> uiState.snackbarHostState.showWarning(e.message)
                        SnackbarType.INFO -> uiState.snackbarHostState.showInfo(e.message)
                    }
                }
            }
        }
    }
}