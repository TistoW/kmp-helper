package com.tisto.kmp.helper.ui.boilerplate.sample.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tisto.kmp.helper.ui.boilerplate.sample.data.model.Sample
import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.form.SampleFormRoute
import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.list.SampleListRoute

// ══════════════════════════════════════════════════════════════════════════
// WRAPPER ROUTE
//
// Crossfade over a remember (BUKAN rememberSaveable) Stage sealed interface.
// Kenapa remember, bukan rememberSaveable: Stage adalah non-Parcelable sealed
//   interface — rememberSaveable crash karena Bundle serialization gagal.
//
// refreshToken: increment saat form onDone → trigger list refresh.
// formKey: increment setiap navigasi ke form → fresh ViewModel.
// pendingMessage: form success message → forwarded ke list screen.
//
// Tidak pakai nav library. Tidak pakai NavHost.
// ══════════════════════════════════════════════════════════════════════════

@Composable
internal fun SampleRoute(
    onBack: () -> Unit = {},
    onPick: ((Sample) -> Unit)? = null,
) {
    var stage by remember { mutableStateOf<Stage>(Stage.List) }
    var refreshToken by remember { mutableIntStateOf(0) }
    var formKey by remember { mutableIntStateOf(0) }
    var pendingMessage by remember { mutableStateOf<String?>(null) }

    Crossfade(
        targetState = stage,
        modifier = Modifier.fillMaxSize(),
    ) { current ->
        when (current) {
            Stage.List -> SampleListRoute(
                onNavigateToForm = { item ->
                    formKey++
                    stage = Stage.Form(item)
                },
                onBack = onBack,
                onPick = onPick,
                refreshToken = refreshToken,
                pendingMessage = pendingMessage,
                onMessageShown = { pendingMessage = null },
            )

            is Stage.Form -> SampleFormRoute(
                item = current.item,
                formKey = formKey,
                onDone = { message ->
                    pendingMessage = message
                    refreshToken++
                    stage = Stage.List
                },
            )
        }
    }
}

// ── Stage sealed interface ──────────────────────────────────────────────

private sealed interface Stage {
    data object List : Stage
    data class Form(val item: Sample?) : Stage
}
