package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.HelperTheme
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.utils.model.PickedImage
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Stable
class ImagePickerState internal constructor(
    private val scope: CoroutineScope,
) {
    var picked: PickedImage? by mutableStateOf(null)
        private set

    var bitmap: ImageBitmap? by mutableStateOf(null)
        private set

    var errorMessage: String? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    fun clear() {
        picked = null
        bitmap = null
        errorMessage = null
        isLoading = false
    }

    fun pick() {
        scope.launch {
            errorMessage = null
            isLoading = true

            try {
                val file: PlatformFile? = FileKit.openFilePicker(
                    type = FileKitType.File(
                        extensions = listOf("jpg", "jpeg", "png", "gif", "webp", "bmp")
                    )
                )
                if (file == null) {
                    // cancel
                    isLoading = false
                    return@launch
                }

                val result = PickedImage(
                    file = file,
                    name = file.name,
                    mimeType = file.mimeType()?.toString()
                )

                picked = result

                // decode for preview
                val bytes = file.readBytes()
                bitmap = bytes.decodeToImageBitmap()
            } catch (t: Throwable) {
                errorMessage = t.message ?: "Unknown error"
                bitmap = null
                picked = null
            } finally {
                isLoading = false
            }
        }
    }
}

@Composable
fun rememberImagePickerState(): ImagePickerState {
    val scope = rememberCoroutineScope()
    return remember(scope) { ImagePickerState(scope) }
}

/**
 * Widget siap pakai: pick + tampilkan preview (Image()).
 */
@Composable
fun ImagePickerScreen() {
    val picker = rememberImagePickerState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = picker::pick) {
            Text("Pick Image")
        }

        if (picker.isLoading) {
            CircularProgressIndicator()
        }

        picker.errorMessage?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }

        picker.bitmap?.let { bmp ->
            Image(
                bitmap = bmp,
                contentDescription = picker.picked?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )
        }

        picker.picked?.let {
            Text("File: ${it.name} (${it.mimeType ?: "unknown"})")
        }

        OutlinedButton(onClick = picker::clear) {
            Text("Clear")
        }
    }
}

@MobilePreview
@Composable
fun ImagePickerScreenPreview() {
    HelperTheme {
        ImagePickerScreen()
    }
}

