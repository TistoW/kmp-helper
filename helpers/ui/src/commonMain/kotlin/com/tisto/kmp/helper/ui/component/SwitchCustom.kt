package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.layout.Arrangement
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

@Composable
fun SwitchCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String = "",
    modifier: Modifier = Modifier,
    backgroundColor: Color = Colors.Gray5,
    checkedColor: Color = Color.Black,
    uncheckedColor: Color = Colors.Gray4,
    styleText: TextStyle = TextAppearance.body2Bold(),
    enabled: Boolean = true,
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
                .padding(horizontal = Spacing.normal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = styleText, // Body2 equivalent
                modifier = Modifier.weight(1f)
            )
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