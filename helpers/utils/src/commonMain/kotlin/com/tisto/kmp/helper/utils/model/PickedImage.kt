package com.tisto.kmp.helper.utils.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes

@Immutable
data class PickedImage(
    val file: PlatformFile,
    val name: String,
    val mimeType: String?,
){
    suspend fun getBitmap(): ImageBitmap {
        val bytes = file.readBytes()
        return bytes.decodeToImageBitmap()
    }
}