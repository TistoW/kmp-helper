package com.tisto.kmp.helper.network.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun <REQ> ObserveUiEffectRetrofit(
//    vm: BaseRetrofitViewModel<REQ>,
) {
//    val uiState by vm.uiState.collectAsStateWithLifecycle()

//    LaunchedEffect(vm) {
//        vm.effect.collect { e ->
//            when (e) {
//                is UiEffect.Toast -> {
//                    when (e.type) {
//                        SnackbarType.SUCCESS -> uiState.snackbarHostState.showSuccess(e.message)
//                        SnackbarType.ERROR -> uiState.snackbarHostState.showError(e.message)
//                        SnackbarType.WARNING -> uiState.snackbarHostState.showWarning(e.message)
//                        SnackbarType.INFO -> uiState.snackbarHostState.showInfo(e.message)
//                    }
//                }
//            }
//        }
//    }
}
