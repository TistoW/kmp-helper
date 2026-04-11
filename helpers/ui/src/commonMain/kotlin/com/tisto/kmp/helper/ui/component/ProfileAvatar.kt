package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tisto.kmp.helper.ui.ext.colorFromText
import com.tisto.kmp.helper.ui.theme.Colors

@Composable
fun ProfileAvatar(
    name: String,
    imageUrl: String?,
    size: Dp
) {
    val initials = remember(name) {
        name
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercaseChar() }
            .joinToString("")
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                colorFromText(
                    name.replace("Partai ", "")
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrBlank()) {
            Text(
                text = initials,
                fontSize = size.value.sp * 0.4f,
                fontWeight = FontWeight.Bold,
                color = Colors.Gray3
            )
        } else {
            // Jika mau pakai image, aktifkan versi Coil di bawah
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}