@file:Suppress("unused")

package com.tisto.kmp.helper.ui.boilerplate.sample.presentation.form

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tisto.kmp.helper.ui.boilerplate.sample.data.model.Sample
import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.SampleStrings
import com.tisto.kmp.helper.ui.component.AppSnackbarVisuals
import com.tisto.kmp.helper.ui.component.CardImagePicker
import com.tisto.kmp.helper.ui.component.CurrencyTextField
import com.tisto.kmp.helper.ui.component.CustomTextField
import com.tisto.kmp.helper.ui.component.FormContainer
import com.tisto.kmp.helper.ui.component.ScaffoldBox
import com.tisto.kmp.helper.ui.component.SwitchCard
import com.tisto.kmp.helper.ui.component.TextFieldStyle
import com.tisto.kmp.helper.ui.component.toSnackbarType
import com.tisto.kmp.helper.ui.ext.BackHandler
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.ext.safeKoinViewModel
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Spacing
import kotlinx.coroutines.flow.Flow
import org.koin.core.parameter.parametersOf

// ══════════════════════════════════════════════════════════════════════════
// FORM SCREEN
//
// Route = stateful: safeKoinViewModel dengan key dan parameters.
// Screen = pure @Composable: terima data + callbacks.
//
// Form Route takes formKey, uses "${item?.id}_$formKey" sebagai VM key.
// Ini memastikan fresh ViewModel setiap kali form dibuka — prevent stale data.
//
// Gunakan ScaffoldBox + FormContainer — handles Scaffold, Toolbar,
//   save/delete buttons, scroll, loading state.
// ══════════════════════════════════════════════════════════════════════════

// ── Route (stateful) ────────────────────────────────────────────────────

@Composable
internal fun SampleFormRoute(
    item: Sample?,
    formKey: Int = 0,
    onDone: (message: String?) -> Unit,
) {
    val viewModel: SampleFormViewModel = safeKoinViewModel(
        key = "${item?.id}_$formKey",
        parameters = { parametersOf(item) },
    ) ?: return

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    SampleFormEffectHandler(viewModel.effect, snackbar, onDone)

    // Di project gunakan ZenentaTheme { screenConfig -> ... }
    HelperTheme {
        SampleFormScreen(
            state = state,
            snackbar = snackbar,
            onEvent = viewModel::onEvent,
            onBack = { onDone(null) },
        )
    }
}

// ── Effect Handler ──────────────────────────────────────────────────────

@Composable
private fun SampleFormEffectHandler(
    effects: Flow<SampleFormEffect>,
    snackbar: SnackbarHostState,
    onDone: (message: String?) -> Unit,
) {
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is SampleFormEffect.ShowMessage -> snackbar.showSnackbar(
                    AppSnackbarVisuals(
                        message = effect.message,
                        type = effect.type.toSnackbarType(),
                    )
                )

                is SampleFormEffect.NavigateBack -> onDone(effect.message)
            }
        }
    }
}

// ── Screen (pure) ───────────────────────────────────────────────────────

@Composable
internal fun SampleFormScreen(
    state: SampleFormUiState,
    snackbar: SnackbarHostState,
    screenConfig: ScreenConfig = ScreenConfig(),
    onEvent: (SampleFormEvent) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler { onBack() }

    ScaffoldBox(snackbarHostState = snackbar) {
        FormContainer(
            forceTitle = if (state.mode is SampleFormMode.Edit) {
                SampleStrings.titleEdit
            } else {
                SampleStrings.titleCreate
            },
            screenConfig = screenConfig,
            item = (state.mode as? SampleFormMode.Edit)?.itemId,
            selectedItemName = state.name,
            isFormValid = state.canSubmit,
            isLoadingProcess = state.isSubmitting,
            onBack = onBack,
            onSave = { onEvent(SampleFormEvent.Submit) },
            onDelete = { onEvent(SampleFormEvent.Delete) },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(if (screenConfig.isMobile) Spacing.normal else Spacing.large))

            // Image picker
            CardImagePicker(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp),
                imageUrl = state.imageUrl,
                onPicker = { onEvent(SampleFormEvent.ImagePicked(it)) },
            )

            Spacer(Modifier.height(Spacing.large))

            // Name field
            CustomTextField(
                value = state.name,
                onValueChange = { onEvent(SampleFormEvent.NameChanged(it)) },
                label = SampleStrings.labelName,
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 1.dp,
                isError = state.nameError != null,
                supportingText = state.nameError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(Modifier.height(Spacing.normal))

            // Description field
            CustomTextField(
                value = state.description,
                onValueChange = { onEvent(SampleFormEvent.DescriptionChanged(it)) },
                label = SampleStrings.labelDescription,
                style = TextFieldStyle.OUTLINED,
                strokeWidth = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(Spacing.normal))

            // Description field
            CurrencyTextField(
                value = state.price,
                onValueChange = { onEvent(SampleFormEvent.DescriptionChanged(it)) },
                label = SampleStrings.labelDescription,
                strokeWidth = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(Spacing.normal))

            // Active switch
            SwitchCard(
                text = SampleStrings.labelActive,
                checked = state.isActive,
                onCheckedChange = { onEvent(SampleFormEvent.ActiveChanged(it)) },
            )
        }
    }
}

// ── Previews ────────────────────────────────────────────────────────────

@MobilePreview
@Composable
private fun PreviewSampleFormPhone() {
    SampleFormScreen(
        state = SampleFormUiState(
            mode = SampleFormMode.Edit("1"),
            name = "Contoh Item",
            description = "Deskripsi contoh item",
        ),
        snackbar = SnackbarHostState(),
        screenConfig = ScreenConfig(maxWidth = 360.dp),
        onEvent = {},
        onBack = {},
    )
}

@TabletPreview
@Composable
private fun PreviewSampleFormTablet() {
    SampleFormScreen(
        state = SampleFormUiState(
            mode = SampleFormMode.Edit("1"),
            name = "Contoh Item",
            description = "Deskripsi contoh item",
        ),
        snackbar = SnackbarHostState(),
        screenConfig = ScreenConfig(maxWidth = 840.dp),
        onEvent = {},
        onBack = {},
    )
}
