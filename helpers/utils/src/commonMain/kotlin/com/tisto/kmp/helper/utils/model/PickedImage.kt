package com.tisto.kmp.helper.utils.model

import androidx.compose.runtime.Immutable
import io.github.vinceglb.filekit.PlatformFile

@Immutable
data class PickedImage(
    val file: PlatformFile,
    val name: String,
    val mimeType: String?,
)