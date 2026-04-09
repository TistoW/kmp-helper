package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinInternalApi
import org.koin.mp.KoinPlatformTools

/**
 * Safe wrapper around [koinViewModel] that returns null when the Koin scope is closed
 * or Koin is not started. This prevents ClosedScopeException crashes during
 * recomposition when the scope has been torn down (e.g., during Fragment/Activity
 * lifecycle transitions).
 *
 * Usage:
 * ```
 * val viewModel: MyViewModel = safeKoinViewModel() ?: return
 * ```
 */
@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : ViewModel> safeKoinViewModel(key: String? = null): T? {
    val koin = KoinPlatformTools.defaultContext().getOrNull() ?: return null
    if (koin.scopeRegistry.rootScope.closed) return null
    return koinViewModel<T>(key = key)
}
