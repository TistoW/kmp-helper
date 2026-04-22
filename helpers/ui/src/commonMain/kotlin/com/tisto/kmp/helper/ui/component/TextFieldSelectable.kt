package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.ext.TabletPreview
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance

// ── SelectableTextField ──────────────────────────────────────────────────────
//
// Read-only text field that acts as a tap target. Clicking anywhere on the
// field (including the trailing chevron) fires [onClick] — no external
// Modifier.clickable needed.
//
// Use for fields that open a bottom sheet, dialog, or picker on tap.
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableTextField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    prefix: String = "",
    suffix: String = "",
    supportingText: String? = null,
    textStyle: TextStyle = TextAppearance.body1(),
    labelStyle: TextStyle = TextAppearance.body2(),
    placeholderStyle: TextStyle = TextAppearance.body1(),
    prefixStyle: TextStyle = TextAppearance.body1(),
    suffixStyle: TextStyle = TextAppearance.body1(),
    supportingTextStyle: TextStyle = TextAppearance.body2(),
    enabled: Boolean = true,
    isError: Boolean = false,
    cornerRadius: Dp = Radius.box,
    strokeColor: Color = Colors.Gray2,
    strokeWidth: Dp = 0.5.dp,
    trailingIcon: ImageVector = Icons.Default.KeyboardArrowRight,
    trailingIconSize: Dp = 20.dp,
    trailingIconTint: Color = Colors.Gray2,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            textStyle = textStyle,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                label = if (label.isNotEmpty()) {
                    { Text(text = label, style = labelStyle) }
                } else null,
                placeholder = if (placeholder.isNotEmpty()) {
                    { Text(text = placeholder, style = placeholderStyle) }
                } else null,
                prefix = if (prefix.isNotEmpty()) {
                    { Text(text = prefix, style = prefixStyle) }
                } else null,
                suffix = if (suffix.isNotEmpty()) {
                    { Text(text = suffix, style = suffixStyle) }
                } else null,
                supportingText = supportingText?.let { msg ->
                    { Text(text = msg, style = supportingTextStyle) }
                },
                trailingIcon = {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(trailingIconSize),
                        tint = trailingIconTint,
                    )
                },
                contentPadding = PaddingValues(
                    top = Spacing.box,
                    bottom = Spacing.box,
                    start = Spacing.box,
                    end = Spacing.box,
                ),
                interactionSource = interactionSource,
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = enabled,
                        isError = isError,
                        shape = RoundedCornerShape(cornerRadius),
                        interactionSource = interactionSource,
                        focusedBorderThickness = strokeWidth,
                        unfocusedBorderThickness = strokeWidth,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = strokeColor,
                            unfocusedBorderColor = strokeColor,
                            disabledBorderColor = strokeColor.copy(alpha = 0.5f),
                        ),
                    )
                },
            )
        }

        // Full-surface transparent overlay — captures all taps including the icon area
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { onClick() }
        )
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Composable
private fun SelectableTextFieldExample() {
    var selected by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.normal),
        verticalArrangement = Arrangement.spacedBy(Spacing.normal),
    ) {
        Text("1. Kosong (placeholder)", style = TextAppearance.body2())
        SelectableTextField(
            value = selected,
            label = "Kategori",
            placeholder = "Pilih kategori",
            onClick = { selected = "Makanan" },
        )

        Text("2. Sudah dipilih", style = TextAppearance.body2())
        SelectableTextField(
            value = "Elektronik",
            label = "Kategori",
            onClick = {},
        )

        Text("3. Dengan prefix & suffix", style = TextAppearance.body2())
        SelectableTextField(
            value = "10 hari",
            label = "Durasi",
            prefix = "~",
            suffix = "kerja",
            onClick = {},
        )

        Text("4. Error + supporting text", style = TextAppearance.body2())
        SelectableTextField(
            value = "",
            label = "Metode Bayar",
            placeholder = "Wajib dipilih",
            isError = true,
            supportingText = "Pilih metode pembayaran terlebih dahulu",
            onClick = {},
        )

        Text("5. Disabled", style = TextAppearance.body2())
        SelectableTextField(
            value = "Tidak bisa diubah",
            label = "Status",
            enabled = false,
            onClick = {},
        )
    }
}

@MobilePreview
@Composable
private fun SelectableTextFieldMobilePreview() {
    HelperTheme { SelectableTextFieldExample() }
}

@TabletPreview
@Composable
private fun SelectableTextFieldTabletPreview() {
    HelperTheme { SelectableTextFieldExample() }
}
