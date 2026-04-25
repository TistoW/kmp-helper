package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.ScreenConfig
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing


@Composable
fun CustomSurface(
    modifier: Modifier = Modifier,
    screenConfig: ScreenConfig = ScreenConfig(),
    horizontalPadding: Float? = null,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable () -> Unit,
) {
    val isMobile = screenConfig.isMobile
    Box(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
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
            content = content
        )
    }

}