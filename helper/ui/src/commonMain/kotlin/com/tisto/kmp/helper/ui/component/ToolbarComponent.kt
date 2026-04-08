package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.ComposeHelperTheme
import com.tisto.kmp.helper.ui.theme.Heights
import com.tisto.kmp.helper.ui.theme.Padding
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview

@Composable
fun Toolbars(
    title: String? = null,
    modifier: Modifier = Modifier,
    iconBack: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack, // ImageVector.vectorResource(id = uiDrawable.ic_arrow_back_black_24dp)
    iconMore: ImageVector = Icons.AutoMirrored.Filled.More,
    saveText: String? = null,
    deleteText: String? = null,
    isHideBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    onMore: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    isEnabledDelete: Boolean = true,
    isLoadingDelete: Boolean = false,
    onSave: (() -> Unit)? = null,
    isEnabledSave: Boolean = true,
    isLoadingSave: Boolean = false,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Colors.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = Padding.small, end = Padding.box),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null && !isHideBack) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = iconBack
                            ?: Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(Spacing.box))
            }
            Text(
                text = title ?: "Title",
                style = TextAppearance.body1Bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Padding.small)
                    .align(Alignment.CenterVertically)
            )

            if (onDelete != null) {
                ButtonNormal(
                    text = deleteText ?: "Hapus",
                    onClick = onDelete,
                    isLoading = isLoadingDelete,
                    enabled = isEnabledDelete,
                    modifier = Modifier.height(Heights.normal)
                        .padding(end = Padding.box)
                        .defaultMinSize(minWidth = 90.dp),
                    strokeWidth = 1.dp,
                    textColor = Colors.ColorPrimary
                )
            }

            if (onSave != null) {
                ButtonNormal(
                    text = saveText ?: "Simpan",
                    onClick = onSave,
                    isLoading = isLoadingSave,
                    enabled = isEnabledSave,
                    modifier = Modifier.height(Heights.normal)
                        .defaultMinSize(minWidth = 90.dp)
                )
            }

            if (onMore != null) {
                IconButton(onClick = onMore) {
                    Icon(
                        imageVector = iconMore, contentDescription = "Add"
                    )
                }
            }

        }

        SimpleHorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
    }
}


@MobilePreview
@Composable
fun MobileToolbarPreviews() {
    ComposeHelperTheme {
        Toolbars()
    }
}

@TabletPreview
@Composable
fun TabletToolbarPreviews() {
    ComposeHelperTheme {
        Toolbars(
            onSave = {},
            onDelete = {},
        )
    }

}
