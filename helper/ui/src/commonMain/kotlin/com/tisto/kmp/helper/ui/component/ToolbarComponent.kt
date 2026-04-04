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
//import com.tisto.helper.core.helper.ui.theme.ComposeHelperTheme
//import com.tisto.helper.core.helper.ui.theme.Heights
//import com.tisto.helper.core.helper.ui.theme.Padding
//import com.tisto.helper.core.helper.ui.theme.TextAppearance
//import com.tisto.helper.core.helper.ui.theme.Colors
//import com.tisto.helper.core.helper.ui.theme.Spacing
//import com.tisto.helper.core.helper.utils.ext.MobilePreview
//import com.tisto.helper.core.helper.utils.ext.TabletPreview

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

}


//@MobilePreview
//@Composable
//fun MobileToolbarPreviews() {
//    ComposeHelperTheme {
//        Toolbars()
//    }
//}
//
//@TabletPreview
//@Composable
//fun TabletToolbarPreviews() {
//    ComposeHelperTheme {
//        Toolbars(
//            onSave = {},
//            onDelete = {},
//        )
//    }
//
//}
