package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Heights
import com.tisto.kmp.helper.ui.theme.Padding
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.utils.ext.def

@Composable
fun Toolbar(
    title: String,
    modifier: Modifier = Modifier,
    screenConfig: ScreenConfig = ScreenConfig(),
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onDelete: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    addText: String = "Tambah",
    saveText: String = "Simpan",
    deleteText: String = "Hapus",
    isEnabledDelete: Boolean = true,
    isEnabledSave: Boolean = true,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Colors.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onBack != null) {
                        Modifier
                            .padding(end = if (screenConfig.isMobile) Spacing.normal else Spacing.large)
                            .padding(start = if (screenConfig.isMobile) Spacing.tiny else Spacing.normal)
                    } else {
                        Modifier.padding(horizontal = if (screenConfig.isMobile) Spacing.normal else Spacing.large)
                    }
                )
                .padding(vertical = Spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = backIcon,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(Spacing.tiny))
            } else {
                Spacer(
                    modifier = Modifier
                        .width(Spacing.box)
                        .height(40.dp)
                )
            }

            Text(
                text = title,
                style = TextAppearance.title2Bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            if (onDelete != null) {
                ButtonNormal(
                    style = ButtonStyle.Outlined,
                    text = deleteText,
                    strokeColor = Colors.Black,
                    horizontalContentPadding = Spacing.normal,
//                    imageVector = Icons.Default.DeleteOutline,
                    onClick = onDelete,
                    modifier = Modifier
                        .height(Heights.normal)
                        .defaultMinSize(minWidth = 90.dp),
                    isLoading = isLoading,
                    enabled = isEnabledDelete
                )

                if (onAdd != null || onSave != null) Spacer(modifier = Modifier.width(Spacing.box))
            }

            if (onAdd != null) {
                ButtonNormal(
                    text = addText,
                    backgroundColor = Colors.Black,
                    horizontalContentPadding = Spacing.normal,
//                    imageVector = Icons.Default.Add,
                    onClick = onAdd,
                    modifier = Modifier
                        .height(Heights.normal)
                        .defaultMinSize(minWidth = 90.dp)
                )
            }

            if (onSave != null) {
                ButtonNormal(
                    text = saveText,
                    backgroundColor = Colors.Black,
                    horizontalContentPadding = Spacing.normal,
//                    imageVector = Icons.Default.Add,
                    onClick = onSave,
                    modifier = Modifier
                        .height(Heights.normal)
                        .defaultMinSize(minWidth = 90.dp),
                    isLoading = isLoading,
                    enabled = isEnabledSave
                )
            }
        }

        SimpleHorizontalDivider(modifier = Modifier)
    }
}

@Composable
fun Toolbars(
    modifier: Modifier = Modifier,
    title: String? = null,
    screenConfig: ScreenConfig = ScreenConfig(),
    backIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack, // ImageVector.vectorResource(id = uiDrawable.ic_arrow_back_black_24dp)
    saveText: String? = null,
    deleteText: String? = null,
    onBack: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    isEnabledDelete: Boolean = true,
    isLoadingDelete: Boolean = false,
    onSave: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    isEnabledSave: Boolean = true,
    isLoadingSave: Boolean = false,
) {

    Toolbar(
        title = title.def(),
        screenConfig = screenConfig,
        backIcon = backIcon ?: Icons.AutoMirrored.Filled.ArrowBack,
        onDelete = onDelete,
        onBack = onBack,
        onAdd = onAdd,
        onSave = onSave,
        saveText = saveText ?: "Simpan",
        deleteText = deleteText ?: "Hapus",
        isEnabledDelete = isEnabledDelete,
        isEnabledSave = isEnabledSave,
        isLoading = isLoadingSave || isLoadingDelete,
        modifier = modifier
    )
}


@MobilePreview
@Composable
fun MobileToolbarPreviews() {
    HelperTheme {
        Column {
            Toolbars()
            HorizontalDivider()
            Toolbar("Title")
            HorizontalDivider()
            Toolbar("Title", onBack = {

            })
            HorizontalDivider()
            Toolbar("Title", onAdd = {

            })
            HorizontalDivider()
            Toolbar("Title", onAdd = {

            }, onBack = {})
        }

    }
}

@TabletPreview
@Composable
fun TabletToolbarPreviews() {
    HelperTheme {
        Column() {
            Toolbars(
                onSave = {},
                onDelete = {},
            )
            HorizontalDivider()
            Toolbar("Title", onAdd = {

            }, onBack = {})
            HorizontalDivider()
            Toolbar("Title", onAdd = {

            }, onBack = {})
            HorizontalDivider()
            Toolbar("Title", onAdd = {

            }, onBack = {}, onDelete = {})
        }

    }

}
