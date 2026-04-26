package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ScaffoldBox(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    isLoadingProcess: Boolean = false,
    enableInnerPadding: Boolean = false,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost ?: {},
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) {
        Box(
            modifier = Modifier.fillMaxSize().then(
                if (enableInnerPadding) Modifier.padding(it) else Modifier
            )
        ) {
            content(it)
            LoadingDialog(isLoadingProcess)
            if (snackbarHostState != null) {
                AppSnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }

}
