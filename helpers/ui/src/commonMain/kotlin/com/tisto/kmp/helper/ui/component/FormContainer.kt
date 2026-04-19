package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.utils.ext.shorten
import com.tisto.kmp.helper.ui.ext.title

sealed interface FormContent {
    data class Column(val content: @Composable ColumnScope.() -> Unit) : FormContent
    data class Form(val content: @Composable FormScopeImpl.() -> Unit) : FormContent
}

@Composable
fun <ITEM> FormContainer(
    title: String = "Title",
    forceTitle: String? = null,
    screenConfig: ScreenConfig = ScreenConfig(),
    isFormValid: Boolean = true,
    horizontalPadding: Float? = null,
    item: ITEM? = null,
    selectedItemName: String? = "item ini",
    isLoadingProcess: Boolean = false,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
    saveText: String = "Simpan",
    deleteText: String = "Hapus",
    backText: String = "Kembali",
    scrollState: ScrollState = rememberScrollState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    formScope: (@Composable FormScopeImpl.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val isMobile = screenConfig.isMobile

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White),
    ) {
        Toolbar(
            title = if (!forceTitle.isNullOrEmpty()) forceTitle else title.title(item != null),
            onBack = if (!isMobile) null else onBack,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imePadding()
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(screenConfig.getHorizontalPaddingFormWeight(horizontalPadding))
                    .padding(bottom = Spacing.normal)
                    .then(
                        if (isMobile) Modifier.padding(horizontal = Spacing.normal)
                        else Modifier
                            .padding(top = Spacing.huge)
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(Radius.medium),
                                ambientColor = Color.Black.copy(alpha = 0.10f),
                                clip = false,
                            )
                    ),
                shape = RoundedCornerShape(Radius.medium),
                color = Color.White,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (!isMobile) Modifier.padding(horizontal = Spacing.medium) else Modifier),
                ) {
                    // Form fields
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = verticalArrangement,
                        horizontalAlignment = horizontalAlignment,
                    ) {
                        if (formScope != null) {
                            FormScope { formScope() }
                        } else {
                            content()
                        }
                    }

                    // Bottom buttons
                    FormButtonBar(
                        isMobile = isMobile,
                        isEdit = item != null,
                        isFormValid = isFormValid,
                        isLoadingProcess = isLoadingProcess,
                        saveText = saveText,
                        deleteText = deleteText,
                        backText = backText,
                        onBack = onBack,
                        onSave = onSave,
                        onDelete = { showDeleteDialog = true },
                    )
                }
            }
        }

        DeleteConfirmationDialog(
            showDialog = showDeleteDialog,
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            itemName = selectedItemName.shorten(),
        )
    }
}

@Composable
private fun FormButtonBar(
    isMobile: Boolean,
    isEdit: Boolean,
    isFormValid: Boolean,
    isLoadingProcess: Boolean,
    saveText: String,
    deleteText: String,
    backText: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isMobile) Spacing.normal else Spacing.medium),
    ) {
        if (!isMobile) {
            ButtonNormal(
                text = backText,
                onClick = onBack,
                isLoading = isLoadingProcess,
                backgroundColor = Color.Black,
                horizontalContentPadding = Spacing.normal,
                modifier = Modifier.widthIn(min = 100.dp)
            )
        }

        Row(
            modifier = if (isMobile) Modifier.fillMaxWidth()
            else Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (isEdit) {
                ButtonNormal(
                    text = deleteText,
                    onClick = onDelete,
                    isLoading = isLoadingProcess,
                    strokeWidth = 1.dp,
                    strokeColor = Color.Black,
                    textColor = Colors.Black,
                    horizontalContentPadding = Spacing.normal,
                    modifier = if (isMobile) Modifier.weight(1f) else Modifier.widthIn(min = 100.dp),
                )
            }

            ButtonNormal(
                text = saveText,
                onClick = onSave,
                backgroundColor = Color.Black,
                horizontalContentPadding = Spacing.normal,
                isLoading = isLoadingProcess,
                enabled = isFormValid,
                modifier = if (isMobile) Modifier.weight(1f) else Modifier.widthIn(min = 100.dp),
            )
        }
    }
}


@Composable
private fun FormScreenContentPreview(
    screenConfig: ScreenConfig = ScreenConfig(),
) {
    FormContainer(
        screenConfig = screenConfig,
        item = ExampleModel(),
        content = {
            Column {
                Spacer(modifier = Modifier.height(Spacing.large))
                CustomTextField(
                    value = "",
                    onValueChange = { },
                    label = "Nama",
                    style = TextFieldStyle.OUTLINED,
                    strokeWidth = 1.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    )
}

@TabletPreview
@Composable
private fun TabletPreviewsForm() {
    HelperTheme {
        FormScreenContentPreview(ScreenConfig(700.dp))
    }
}

@MobilePreview
@Composable
private fun MobilePreviewsForm() {
    HelperTheme {
        FormScreenContentPreview()
    }
}
