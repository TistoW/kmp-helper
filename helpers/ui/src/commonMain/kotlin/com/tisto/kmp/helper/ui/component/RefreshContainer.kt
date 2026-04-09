package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tisto.kmp.helper.utils.PlatformType
import com.tisto.kmp.helper.utils.ext.isMobilePhone
import com.tisto.kmp.helper.utils.platform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshContainer(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    contentAlignment: Alignment = Alignment.TopCenter,
    onRefresh: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    if (PlatformType.isAndroid) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            contentAlignment = contentAlignment,
            modifier = modifier.fillMaxSize(),
            content = content
        )
    } else {
        Box(
            modifier.fillMaxSize(),
            contentAlignment = contentAlignment
        ) {
            content()
        }
    }

//    PullToRefreshBox(
//        isRefreshing = isRefreshing,
//        onRefresh = onRefresh,
//        contentAlignment = contentAlignment,
//        modifier = modifier.fillMaxSize(),
//        content = content
//    )
}
