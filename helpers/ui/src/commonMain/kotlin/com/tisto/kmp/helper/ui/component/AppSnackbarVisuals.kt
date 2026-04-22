package com.tisto.kmp.helper.ui.component

import com.tisto.kmp.helper.network.MessageType
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.utils.SnackbarType

/** Tambahan 'type' di visuals biar SnackbarHost bisa ganti warna/icon */
class AppSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = true,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val type: SnackbarType = SnackbarType.INFO,
) : SnackbarVisuals

@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(hostState, modifier) { data ->
        val visuals = data.visuals as? AppSnackbarVisuals
        val type = visuals?.type ?: SnackbarType.INFO

        val (bg, fg, icon) = when (type) {
            SnackbarType.SUCCESS -> Triple(
                Color(0xFF00A405),
                Color.White,
                Icons.Outlined.CheckCircle
            )

            SnackbarType.ERROR -> Triple(
                MaterialTheme.colorScheme.error,
                Color.White,
                Icons.Outlined.Warning
            )

            SnackbarType.WARNING -> Triple(Color(0xFFF57C00), Color.White, Icons.Outlined.Warning)
            SnackbarType.INFO -> Triple(
                MaterialTheme.colorScheme.primary,
                Color.White,
                Icons.Outlined.Info
            )
        }

        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = bg,
            contentColor = fg,
            action = {
                data.visuals.actionLabel?.let {
                    TextButton(onClick = { data.performAction() }) { Text(it, color = fg) }
                }
            },
            dismissAction = if (data.visuals.withDismissAction) {
                {
                    IconButton(onClick = { data.dismiss() }) {
                        Icon(Icons.Outlined.Close, contentDescription = "Dismiss", tint = fg)
                    }
                }
            } else null
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = fg)
                Spacer(Modifier.width(8.dp))
                Text(text = data.visuals.message, color = fg)
            }
        }
    }
}

suspend fun SnackbarHostState.showSuccess(
    msg: String,
    action: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) =
    showSnackbar(
        AppSnackbarVisuals(
            msg,
            actionLabel = action,
            type = SnackbarType.SUCCESS,
            duration = duration
        )
    )

suspend fun SnackbarHostState.showError(
    msg: String,
    action: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) =
    showSnackbar(
        AppSnackbarVisuals(
            msg,
            actionLabel = action,
            type = SnackbarType.ERROR,
            duration = duration
        )
    )

suspend fun SnackbarHostState.showWarning(
    msg: String,
    action: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) =
    showSnackbar(
        AppSnackbarVisuals(
            msg,
            actionLabel = action,
            type = SnackbarType.WARNING,
            duration = duration
        )
    )

suspend fun SnackbarHostState.showInfo(
    msg: String,
    action: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) =
    showSnackbar(
        AppSnackbarVisuals(
            msg,
            actionLabel = action,
            type = SnackbarType.INFO,
            duration = duration
        )
    )

suspend fun SnackbarHostState.showSuccess(msg: String) =
    showSnackbar(AppSnackbarVisuals(msg, type = SnackbarType.SUCCESS))

suspend fun SnackbarHostState.showError(msg: String) =
    showSnackbar(AppSnackbarVisuals(msg, type = SnackbarType.ERROR))

fun MessageType.toSnackbarType(): SnackbarType = when (this) {
    MessageType.Success -> SnackbarType.SUCCESS
    MessageType.Error   -> SnackbarType.ERROR
    MessageType.Warning -> SnackbarType.WARNING
    MessageType.Info    -> SnackbarType.INFO
}

suspend fun SnackbarHostState.showMessage(message: String, type: MessageType) =
    showSnackbar(AppSnackbarVisuals(message = message, type = type.toSnackbarType()))

