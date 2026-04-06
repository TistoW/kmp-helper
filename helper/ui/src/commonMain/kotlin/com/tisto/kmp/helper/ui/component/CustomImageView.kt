package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.utils.ext.MobilePreview
import com.tisto.kmp.helper.utils.ext.def
import com.tisto.kmp.helper.ui.utils.ext.toPainter
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.icon.myicon.IcLoading
import com.tisto.kmp.helper.ui.utils.ext.colorFromText

enum class AvatarShape {
    CIRCLE,
    SQUARE
}

// ====================================
// 1. Updated CustomImageView — tambah zoomable parameter
// ====================================

@Composable
fun CustomImageView(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    image: Painter? = null,
    name: String? = null,
    size: Dp? = null,
    width: Dp? = null,
    height: Dp? = null,
    placeholder: Painter? = null,
    error: Painter? = null,
    shape: AvatarShape = AvatarShape.SQUARE,
    contentScale: ContentScale = ContentScale.Crop,
    cornerRadius: Dp = 4.dp,
    zoomable: Boolean = false, // ✅ NEW
) {
    val tempName = name.def("")
    var isError by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var showZoomViewer by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    var actualHeight by remember { mutableStateOf(50.dp) }

    val initials = remember(tempName) {
        tempName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercaseChar() }
            .joinToString("")
    }
    val avatarShape = when (shape) {
        AvatarShape.CIRCLE -> CircleShape
        AvatarShape.SQUARE -> RoundedCornerShape(cornerRadius)
    }

    val sizeModifier = when {
        size != null -> Modifier.size(size)
        width != null && height != null -> Modifier.width(width).height(height)
        width != null -> Modifier.width(width)
        height != null -> Modifier.height(height)
        else -> Modifier
    }

    val referenceSize = size ?: height ?: actualHeight

    Box(
        modifier = modifier
            .then(sizeModifier)
            .clip(avatarShape)
            .onSizeChanged { intSize ->
                with(density) {
                    actualHeight = intSize.height.toDp()
                }
            }
            .then(
                if (zoomable && isSuccess) {
                    Modifier.clickable { showZoomViewer = true }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (image != null && imageUrl == null) {
            Image(
                painter = image,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            if (imageUrl.isNullOrBlank() || isError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (initials.isNotEmpty()) colorFromText(tempName) else Colors.Gray5),
                    contentAlignment = Alignment.Center
                ) {
                    if (initials.isNotEmpty()) {
                        Text(
                            text = initials,
                            fontSize = referenceSize.value.sp * 0.4f,
                            fontWeight = FontWeight.Bold,
                            color = Colors.Gray3
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(referenceSize * 0.5f),
                            tint = Colors.Gray4
                        )
                    }
                }
            } else {
                AsyncImage(
                    model = imageUrl,
                    placeholder = placeholder ?: MyIcon.IcLoading.toPainter(),
                    error = error,
                    contentDescription = null,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize(),
                    onError = { isError = true },
                    onSuccess = { isSuccess = true }
                )
                if (!isSuccess) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Colors.Gray5),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = MyIcon.IcLoading,
                            contentDescription = null,
                            modifier = Modifier.size(referenceSize * 0.5f),
                            tint = Colors.Gray4
                        )
                    }
                }
            }
        }
    }

    // ✅ Fullscreen zoom viewer
    if (showZoomViewer && !imageUrl.isNullOrBlank()) {
        ZoomableImageViewer(
            imageUrl = imageUrl,
            onDismiss = { showZoomViewer = false }
        )
    }
}


// ====================================
// 2. ZoomableImageViewer — fullscreen overlay
// ====================================

@Composable
fun ZoomableImageViewer(
    imageUrl: String,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    onDismiss: () -> Unit,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        // Apply zoom
        val newScale = (scale * zoomChange).coerceIn(minScale, maxScale)

        // Apply pan only when zoomed in
        val newOffset = if (newScale > 1f) {
            offset + panChange
        } else {
            Offset.Zero
        }

        scale = newScale
        offset = newOffset
    }

    // Reset zoom when scale goes back to 1
    LaunchedEffect(scale) {
        if (scale <= 1f) {
            offset = Offset.Zero
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            // Toggle zoom: if zoomed in → reset, if normal → zoom 2.5x
                            if (scale > 1.5f) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scale = 2.5f
                            }
                        },
                        onTap = {
                            // Single tap → close if not zoomed
                            if (scale <= 1f) {
                                onDismiss()
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(40.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Zoomable image
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
                    .transformable(state = transformableState)
            )
        }
    }
}

@MobilePreview
@Composable
fun CustomImageViewPreview() {
    CustomImageView(
        imageUrl = null,
    )
}