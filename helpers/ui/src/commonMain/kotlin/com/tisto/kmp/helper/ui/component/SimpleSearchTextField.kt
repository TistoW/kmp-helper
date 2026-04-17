package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.icon.myicon.IcSearch
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance

@Composable
fun PosSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Cari",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(Radius.box))
            .background(Colors.White)
            .padding(horizontal = 12.dp), // inner text padding
        contentAlignment = Alignment.CenterStart
    ) {

        Row {

            Icon(
                imageVector = MyIcon.IcSearch,
                contentDescription = "item.id",
                tint = Colors.Gray3,
                modifier = Modifier.size(20.dp) // biar lebih proporsional
            )

            Spacer(Modifier.width(Spacing.box))

            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        style = TextAppearance.body2()
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextAppearance.body2(),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.width(Spacing.box))

            if (value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "item.id",
                    tint = Colors.Gray3,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onValueChange("")
                        }
                )
            }
        }


    }
}

@Preview
@Composable
private fun PosSearchTextFieldPreview() {
    PosSearchTextField(
        value = "",
        onValueChange = {},
        placeholder = "Cari produk"
    )
}