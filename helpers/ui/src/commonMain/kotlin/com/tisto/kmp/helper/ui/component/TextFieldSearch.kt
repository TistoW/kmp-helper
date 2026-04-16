package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.icon.myicon.IcSearch
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Cari...",
    leadingIcon: ImageVector = MyIcon.IcSearch,
    strokeColor: Color = Colors.Gray4,
    focusedStrokeColor: Color = Colors.Gray3,
    strokeWidth: Dp = 0.5.dp,
    cornerRadius: Dp = Radius.normal,
    onClear: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = strokeWidth,
                color = if (isFocused) focusedStrokeColor else strokeColor,
                shape = RoundedCornerShape(cornerRadius),
            ),
        singleLine = true,
        textStyle = TextAppearance.body1(),
        cursorBrush = SolidColor(focusedStrokeColor),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke() }),
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Colors.Gray3,
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                ) {
                    if (value.isEmpty()) {
                        Text(hint, style = TextAppearance.body1().copy(color = Colors.Gray3))
                    }
                    innerTextField()
                }
                if (onClear != null) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .then(
                                if (value.isNotEmpty()) Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onClear,
                                ) else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (value.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                modifier = Modifier.size(18.dp),
                                tint = Colors.Gray3,
                            )
                        }
                    }
                }
            }
        },
    )
}

// ── Example / Preview ────────────────────────────────────────────────────────

@Composable
private fun SearchTextFieldExample() {
    var query by remember { mutableStateOf("Budi") }
    var queryNoClose by remember { mutableStateOf("") }
    var queryFilled by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.normal),
        verticalArrangement = Arrangement.spacedBy(Spacing.normal),
    ) {
        // 1. Default — outlined, with clear button
        Text("1. Default (outlined + clear)", style = TextAppearance.body2())
        SearchTextField(
            value = query,
            onValueChange = { query = it },
            onClear = { query = "" },
        )

        // 2. No clear button
        Text("2. No clear button", style = TextAppearance.body2())
        SearchTextField(
            value = queryNoClose,
            onValueChange = { queryNoClose = it },
        )

        // 3. Custom radius + stroke color
        Text("3. Custom radius + stroke color", style = TextAppearance.body2())
        SearchTextField(
            value = queryFilled,
            onValueChange = { queryFilled = it },
            cornerRadius = Radius.extra,
            strokeColor = Colors.Gray3,
            focusedStrokeColor = Colors.Secondary,
            onClear = { queryFilled = "" },
        )
    }
}

@MobilePreview
@Composable
private fun SearchTextFieldMobilePreview() {
    HelperTheme { SearchTextFieldExample() }
}

@TabletPreview
@Composable
private fun SearchTextFieldTabletPreview() {
    HelperTheme { SearchTextFieldExample() }
}
