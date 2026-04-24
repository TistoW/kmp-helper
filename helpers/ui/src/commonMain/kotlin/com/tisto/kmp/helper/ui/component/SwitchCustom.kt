package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.HelperTheme

@Composable
fun SwitchCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    text: String = "",
    subText: String = "",
    backgroundColor: Color = Colors.Gray5,
    checkedColor: Color = Color.Black,
    uncheckedColor: Color = Colors.Gray4,
    styleText: TextStyle = TextAppearance.body2Bold(),
    styleSubText: TextStyle = TextAppearance.body2(),
    enabled: Boolean = true,
    contentHorizontalPadding : Dp = Spacing.normal,
    contentVerticalPadding : Dp = 0.dp,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Radius.normal),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = contentHorizontalPadding, vertical = contentVerticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = text,
                    style = styleText, // Body2 equivalent
                )

                if (subText.isNotEmpty()) {
                    Text(
                        text = subText,
                        style = styleSubText, // Body2 equivalent
                    )
                }
            }

            SwitchCustom(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                size = 0.8f,
                checkedColor = checkedColor,
                uncheckedColor = uncheckedColor
            )
        }
    }
}

@Composable
fun SwitchCustom(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Float = 1f, // 1f = default, 0.8f = smaller, 1.2f = bigger
    checkedColor: Color = Color.Black,
    checkedThumbColor: Color = MaterialTheme.colorScheme.onPrimary,
    uncheckedColor: Color = Colors.Gray4,
    uncheckedThumbColor: Color = MaterialTheme.colorScheme.outline,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.scale(size),
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedTrackColor = checkedColor,
            checkedThumbColor = checkedThumbColor,
            checkedBorderColor = Color.Transparent,
            uncheckedTrackColor = uncheckedColor,
            uncheckedThumbColor = uncheckedThumbColor,
            uncheckedBorderColor = Color.Transparent,
            disabledCheckedTrackColor = checkedColor.copy(alpha = 0.5f),
            disabledUncheckedTrackColor = uncheckedColor.copy(alpha = 0.5f),
        )
    )
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "SwitchCustom — All variants")
@Composable
private fun PreviewSwitchCustomAllVariants() {
    HelperTheme {
        Column(
            modifier = Modifier.padding(Spacing.normal),
            verticalArrangement = Arrangement.spacedBy(Spacing.small),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("Checked", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = true, onCheckedChange = {})
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("Unchecked", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = false, onCheckedChange = {})
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("Checked + disabled", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = true, onCheckedChange = {}, enabled = false)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("Unchecked + disabled", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = false, onCheckedChange = {}, enabled = false)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("size = 0.8f", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = true, onCheckedChange = {}, size = 0.8f)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("size = 1.2f", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = true, onCheckedChange = {}, size = 1.2f)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("Custom green", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = true, onCheckedChange = {}, checkedColor = Color(0xFF4CAF50))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.normal)) {
                Text("Custom red", style = TextAppearance.body2(), modifier = Modifier.weight(1f))
                SwitchCustom(checked = true, onCheckedChange = {}, checkedColor = Color(0xFFF44336))
            }
        }
    }
}

@Preview(showBackground = true, name = "SwitchCard — All variants")
@Composable
private fun PreviewSwitchCardAllVariants() {
    HelperTheme {
        Column(
            modifier = Modifier.padding(Spacing.normal),
            verticalArrangement = Arrangement.spacedBy(Spacing.small),
        ) {
            SwitchCard(checked = true, onCheckedChange = {}, text = "Aktifkan Notifikasi")
            SwitchCard(checked = false, onCheckedChange = {}, text = "Mode Gelap")
            SwitchCard(
                checked = true, onCheckedChange = {},
                text = "Cetak Otomatis",
                subText = "Struk akan dicetak setelah transaksi",
            )
            SwitchCard(
                checked = false, onCheckedChange = {},
                text = "Diskon Member",
                subText = "Terapkan diskon untuk pelanggan terdaftar",
            )
            SwitchCard(
                checked = true, onCheckedChange = {},
                text = "Fitur Premium",
                subText = "Tersedia di paket berbayar",
                enabled = false,
            )
            SwitchCard(checked = false, onCheckedChange = {}, text = "Sinkronisasi Cloud", enabled = false)
            SwitchCard(
                checked = true, onCheckedChange = {},
                text = "Status Toko",
                subText = "Toko sedang buka",
                checkedColor = Color(0xFF4CAF50),
            )
        }
    }
}