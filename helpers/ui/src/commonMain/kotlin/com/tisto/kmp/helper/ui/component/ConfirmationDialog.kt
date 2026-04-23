package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.ui.ext.MobilePreview
import com.tisto.kmp.helper.ui.theme.HelperTheme

// ========================================
// 1. BASIC CONFIRMATION DIALOG
// ========================================

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Konfirmasi",
    cancelText: String = "Batal",
    icon: ImageVector? = null,
    iconTint: Color = Color.Black
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = icon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF666666)
                    )
                ) {
                    Text(cancelText)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

// ========================================
// 2. CUSTOM STYLED CONFIRMATION DIALOG
// ========================================

@Composable
fun CustomConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit = {},
    onCancel: (() -> Unit)? = null,
    onConfirm: () -> Unit = {},
    title: String,
    message: String,
    confirmText: String = "Ya, Lanjutkan",
    cancelText: String = "Cancel",
    icon: ImageVector = Icons.Default.Info,
    iconBackgroundColor: Color = Color(0xFFE6F9F8),
    iconTint: Color = Color(0xFF0ABAB5)
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.tiny),
                shape = RoundedCornerShape(Radius.box),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icon
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(iconBackgroundColor, RoundedCornerShape(32.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Message
                        Text(
                            text = message,
                            fontSize = 14.sp,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        // Buttons
//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//
//                        SimpleButton(
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(48.dp),
//                            backgroundColor = Colors.White,
//                            strokeWidth = 0.5.dp,
//                            strokeColor = Colors.Gray4,
//                            text = cancelText,
//                            textColor = Colors.Gray2,
//                            onClick = {
//                                onDismiss()
//                            }
//                        )
//
//                        Spacer(modifier = Modifier.width(Spacing.box))
//
//
//                        SimpleButton(
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(48.dp),
//                            text = confirmText,
//                            onClick = {
//                                onConfirm()
//                                onDismiss()
//                            }
//                        )
//                    }
                    }

                    Box(
                        modifier = Modifier
                            .background(color = if (onCancel == null) Colors.White else Colors.Gray5)
                            .fillMaxWidth()
                            .clickable(onClick = onConfirm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmText,
                            style = TextAppearance.body2(),
                            modifier = Modifier.padding(vertical = Spacing.normal),
                            color = Colors.ColorPrimary
                        )
                    }

                    SimpleHorizontalDivider()

                    if (onCancel != null) {
                        Box(
                            modifier = Modifier
                                .background(color = Colors.White)
                                .fillMaxWidth()
                                .clickable(onClick = onCancel),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cancelText,
                                style = TextAppearance.body2(),
                                modifier = Modifier.padding(vertical = Spacing.normal),
                                color = Colors.Gray1
                            )
                        }

                        SimpleHorizontalDivider()

                    }

                    Box(
                        modifier = Modifier
                            .background(color = Colors.Gray5)
                            .fillMaxWidth()
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = Spacing.normal)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Icon",
                                tint = Colors.Gray2,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}

// ========================================
// 3. DELETE CONFIRMATION DIALOG (Danger)
// ========================================

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    itemName: String = "item",
    title: String = "Hapus Data?",
    message: String = "Apakah Anda yakin ingin menghapus $itemName? Tindakan ini tidak dapat dibatalkan.",
    confirmText: String = "Hapus Data?",
    cancelText: String = "Batal",
) {
    CustomConfirmationDialog(
        showDialog = showDialog,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = title,
        message = message,
        confirmText = confirmText,
        cancelText = cancelText,
        icon = Icons.Default.Delete,
        iconBackgroundColor = Color(0xFFFFEBEE),
        iconTint = Color(0xFFF44336),
    )
}

// ========================================
// 4. SUCCESS DIALOG
// ========================================

@Composable
fun SuccessDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String = "Berhasil!",
    message: String,
    buttonText: String = "OK"
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Success Icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(32.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(buttonText, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

// ========================================
// 5. WARNING DIALOG
// ========================================

@Composable
fun WarningDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Lanjutkan",
    cancelText: String = "Batal"
) {
    CustomConfirmationDialog(
        showDialog = showDialog,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = title,
        message = message,
        confirmText = confirmText,
        cancelText = cancelText,
        icon = Icons.Default.Warning,
        iconBackgroundColor = Color(0xFFFFF3E0),
        iconTint = Color(0xFFFF9800),
    )
}

// ========================================
// 6. THREE BUTTON DIALOG (Approve/Reject/Cancel)
// ========================================

@Composable
fun ThreeButtonDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onReject: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Setuju",
    rejectText: String = "Tolak",
    cancelText: String = "Tutup",
    icon: ImageVector = Icons.Default.Info,
    iconBackgroundColor: Color = Color(0xFFE6F9F8),
    iconTint: Color = Color(0xFF0ABAB5),
    rejectButtonColor: Color = Color(0xFFF44336)
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(iconBackgroundColor, RoundedCornerShape(32.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Message
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Three Buttons
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Confirm Button (Approve)
                        Button(
                            onClick = {
                                onConfirm()
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = confirmText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Reject Button
                        Button(
                            onClick = {
                                onReject()
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = rejectButtonColor
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = rejectText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Close/Cancel Button
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF666666)
                            )
                        ) {
                            Text(
                                text = cancelText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// ========================================
// 7. THREE BUTTON DIALOG - HORIZONTAL LAYOUT
// ========================================

@Composable
fun ThreeButtonDialogHorizontal(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onReject: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Terima",
    rejectText: String = "Tolak",
    cancelText: String = "Batal",
    icon: ImageVector = Icons.Default.Info,
    iconBackgroundColor: Color = Color(0xFFE6F9F8),
    iconTint: Color = Color(0xFF0ABAB5),
    rejectButtonColor: Color = Color(0xFFF44336)
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(iconBackgroundColor, RoundedCornerShape(32.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Message
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons Row
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Top Row: Confirm and Reject
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Reject Button
                            Button(
                                onClick = {
                                    onReject()
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = rejectButtonColor
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = rejectText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Confirm Button
                            Button(
                                onClick = {
                                    onConfirm()
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = confirmText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Bottom: Close Button
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF666666)
                            )
                        ) {
                            Text(
                                text = cancelText,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ========================================
// 8. APPROVAL DIALOG (Specific Use Case)
// ========================================

@Composable
fun ApprovalDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    itemName: String,
    requestedBy: String = "",
    approvalType: String = "Permintaan"
) {
    ThreeButtonDialog(
        showDialog = showDialog,
        onDismiss = onDismiss,
        onConfirm = onApprove,
        onReject = onReject,
        title = "$approvalType Persetujuan",
        message = if (requestedBy.isNotEmpty()) {
            "$approvalType untuk \"$itemName\" oleh $requestedBy. Pilih tindakan Anda."
        } else {
            "$approvalType untuk \"$itemName\". Pilih tindakan Anda."
        },
        confirmText = "Setujui",
        rejectText = "Tolak",
        cancelText = "Nanti Saja",
        icon = Icons.Default.Info,
        iconBackgroundColor = Color(0xFFE3F2FD),
        iconTint = Color(0xFF2196F3)
    )
}

// ========================================
// EXAMPLE: CARA PENGGUNAAN
// ========================================

@Composable
fun DialogExampleScreen() {
    // State untuk mengontrol dialog
    var showBasicDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showWarningDialog by remember { mutableStateOf(false) }
    var showThreeButtonDialog by remember { mutableStateOf(false) }
    var showThreeButtonHorizontal by remember { mutableStateOf(false) }
    var showApprovalDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Dialog Examples",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Button 1: Basic Dialog
        item {
            Button(
                onClick = { showBasicDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show Basic Dialog")
            }
        }

        // Button 2: Custom Dialog
        item {
            Button(
                onClick = { showCustomDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show Custom Dialog")
            }
        }

        // Button 3: Delete Dialog
        item {
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                )
            ) {
                Text("Show Delete Dialog")
            }
        }

        // Button 4: Success Dialog
        item {
            Button(
                onClick = { showSuccessDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Show Success Dialog")
            }
        }

        // Button 5: Warning Dialog
        item {
            Button(
                onClick = { showWarningDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )
            ) {
                Text("Show Warning Dialog")
            }
        }

        // Button 6: Three Button Dialog (Vertical)
        item {
            Button(
                onClick = { showThreeButtonDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text("Show Three Button Dialog (Vertical)")
            }
        }

        // Button 7: Three Button Dialog (Horizontal)
        item {
            Button(
                onClick = { showThreeButtonHorizontal = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF673AB7)
                )
            ) {
                Text("Show Three Button Dialog (Horizontal)")
            }
        }

        // Button 8: Approval Dialog
        item {
            Button(
                onClick = { showApprovalDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF009688)
                )
            ) {
                Text("Show Approval Dialog")
            }
        }
    }

    // Dialog Components
    ConfirmationDialog(
        showDialog = showBasicDialog,
        onDismiss = { showBasicDialog = false },
        onConfirm = {
            // Handle confirm action
            println("Basic Dialog Confirmed")
        },
        title = "Konfirmasi",
        message = "Apakah Anda yakin ingin melanjutkan?",
        icon = Icons.Default.Info
    )

    CustomConfirmationDialog(
        showDialog = showCustomDialog,
        onDismiss = { showCustomDialog = false },
        onConfirm = {
            // Handle confirm action
            println("Custom Dialog Confirmed")
        },
        title = "Simpan Perubahan?",
        message = "Data yang telah Anda ubah akan disimpan. Apakah Anda yakin?"
    )

    DeleteConfirmationDialog(
        showDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirm = {
            // Handle delete action
            println("Item Deleted")
        },
        itemName = "Semen Gresik"
    )

    SuccessDialog(
        showDialog = showSuccessDialog,
        onDismiss = { showSuccessDialog = false },
        message = "Data berhasil disimpan ke database"
    )

    WarningDialog(
        showDialog = showWarningDialog,
        onDismiss = { showWarningDialog = false },
        onConfirm = {
            // Handle warning confirm
            println("Warning Confirmed")
        },
        title = "Peringatan!",
        message = "Tindakan ini akan mengubah data permanen. Lanjutkan?"
    )

    ThreeButtonDialog(
        showDialog = showThreeButtonDialog,
        onDismiss = { showThreeButtonDialog = false },
        onConfirm = {
            println("Confirmed/Approved")
        },
        onReject = {
            println("Rejected")
        },
        title = "Persetujuan Diperlukan",
        message = "Silakan pilih tindakan untuk permintaan ini"
    )

    ThreeButtonDialogHorizontal(
        showDialog = showThreeButtonHorizontal,
        onDismiss = { showThreeButtonHorizontal = false },
        onConfirm = {
            println("Approved")
        },
        onReject = {
            println("Rejected")
        },
        title = "Konfirmasi Transaksi",
        message = "Apakah Anda ingin menyetujui atau menolak transaksi ini?"
    )

    ApprovalDialog(
        showDialog = showApprovalDialog,
        onDismiss = { showApprovalDialog = false },
        onApprove = {
            println("Request Approved")
        },
        onReject = {
            println("Request Rejected")
        },
        itemName = "Pembelian Semen 100 Zak",
        requestedBy = "Budi Santoso",
        approvalType = "Permintaan"
    )
}

// ========================================
// QUICK USAGE EXAMPLE
// ========================================

@Composable
fun QuickUsageExample() {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { showDialog = true }) {
            Text("Delete Item")
        }

        // Simple one-liner usage
        DeleteConfirmationDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onConfirm = {
                // Your delete logic here
                println("Item deleted!")
            },
            itemName = "Produk A"
        )
    }
}

// ========================================
// PREVIEWS
// ========================================

// ── 1. ConfirmationDialog ────────────────

@MobilePreview
@Composable
private fun PreviewConfirmationDialogWithIcon() {
    HelperTheme {
        ConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            title = "Konfirmasi",
            message = "Apakah Anda yakin ingin melanjutkan tindakan ini?",
            icon = Icons.Default.Info,
        )
    }
}

@MobilePreview
@Composable
private fun PreviewConfirmationDialogNoIcon() {
    HelperTheme {
        ConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            title = "Simpan Data",
            message = "Data Anda akan disimpan secara permanen.",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewConfirmationDialogCustomText() {
    HelperTheme {
        ConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            title = "Keluar Aplikasi",
            message = "Apakah Anda yakin ingin keluar dari aplikasi?",
            confirmText = "Ya, Keluar",
            cancelText = "Tidak",
            icon = Icons.Default.ExitToApp,
            iconTint = Color(0xFFF44336),
        )
    }
}

// ── 2. CustomConfirmationDialog ──────────

@MobilePreview
@Composable
private fun PreviewCustomConfirmationDialogWithCancel() {
    HelperTheme {
        CustomConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            onCancel = {},
            title = "Simpan Perubahan?",
            message = "Data yang telah Anda ubah akan disimpan. Apakah Anda yakin ingin melanjutkan?",
            confirmText = "Ya, Simpan",
            cancelText = "Buang Perubahan",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewCustomConfirmationDialogNoCancel() {
    HelperTheme {
        CustomConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            onCancel = null,
            title = "Cetak Struk?",
            message = "Transaksi berhasil. Apakah Anda ingin mencetak struk sekarang?",
            confirmText = "Ya, Cetak",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewCustomConfirmationDialogCustomColor() {
    HelperTheme {
        CustomConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            onCancel = {},
            title = "Kirim Pesanan?",
            message = "Pesanan akan dikirim ke dapur. Pastikan pesanan sudah benar.",
            confirmText = "Kirim Sekarang",
            cancelText = "Periksa Lagi",
            icon = Icons.Default.Send,
            iconBackgroundColor = Color(0xFFE8F5E9),
            iconTint = Color(0xFF4CAF50),
        )
    }
}

// ── 3. DeleteConfirmationDialog ──────────

@MobilePreview
@Composable
private fun PreviewDeleteConfirmationDialogDefault() {
    HelperTheme {
        DeleteConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            itemName = "Semen Gresik 50 Kg",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewDeleteConfirmationDialogCustomTitle() {
    HelperTheme {
        DeleteConfirmationDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            itemName = "Transaksi #TRX-20250420",
            title = "Batalkan Transaksi?",
            message = "Transaksi #TRX-20250420 akan dibatalkan secara permanen. Tindakan ini tidak dapat dibatalkan.",
            confirmText = "Ya, Batalkan",
        )
    }
}

// ── 4. SuccessDialog ────────────────────

@MobilePreview
@Composable
private fun PreviewSuccessDialogDefault() {
    HelperTheme {
        SuccessDialog(
            showDialog = true,
            onDismiss = {},
            message = "Data berhasil disimpan ke database.",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewSuccessDialogCustom() {
    HelperTheme {
        SuccessDialog(
            showDialog = true,
            onDismiss = {},
            title = "Transaksi Berhasil!",
            message = "Pembayaran sebesar Rp 125.000 telah berhasil diproses.",
            buttonText = "Cetak Struk",
        )
    }
}

// ── 5. WarningDialog ────────────────────

@MobilePreview
@Composable
private fun PreviewWarningDialogDefault() {
    HelperTheme {
        WarningDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            title = "Peringatan!",
            message = "Tindakan ini akan mengubah data secara permanen. Pastikan Anda sudah yakin sebelum melanjutkan.",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewWarningDialogCustomText() {
    HelperTheme {
        WarningDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            title = "Stok Akan Habis",
            message = "Stok produk Nasi Goreng Spesial tersisa 2 porsi. Ingin tetap melanjutkan pesanan?",
            confirmText = "Ya, Lanjutkan",
            cancelText = "Tidak",
        )
    }
}

// ── 6. ThreeButtonDialog (Vertical) ─────

@MobilePreview
@Composable
private fun PreviewThreeButtonDialogDefault() {
    HelperTheme {
        ThreeButtonDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            onReject = {},
            title = "Persetujuan Diperlukan",
            message = "Silakan pilih tindakan untuk permintaan ini.",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewThreeButtonDialogCustom() {
    HelperTheme {
        ThreeButtonDialog(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            onReject = {},
            title = "Status Pengiriman",
            message = "Pesanan #ORD-001 dari Budi Santoso menunggu konfirmasi pengiriman.",
            confirmText = "Siap Kirim",
            rejectText = "Tunda",
            cancelText = "Lihat Nanti",
            icon = Icons.Default.LocalShipping,
            iconBackgroundColor = Color(0xFFE3F2FD),
            iconTint = Color(0xFF2196F3),
        )
    }
}

// ── 7. ThreeButtonDialogHorizontal ──────

@MobilePreview
@Composable
private fun PreviewThreeButtonDialogHorizontalDefault() {
    HelperTheme {
        ThreeButtonDialogHorizontal(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            onReject = {},
            title = "Konfirmasi Transaksi",
            message = "Apakah Anda ingin menyetujui atau menolak transaksi ini?",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewThreeButtonDialogHorizontalCustom() {
    HelperTheme {
        ThreeButtonDialogHorizontal(
            showDialog = true,
            onDismiss = {},
            onConfirm = {},
            onReject = {},
            title = "Verifikasi Pembayaran",
            message = "Pembayaran Rp 250.000 via Transfer Bank dari Andi Wijaya sedang menunggu verifikasi.",
            confirmText = "Terima",
            rejectText = "Tolak",
            cancelText = "Periksa Nanti",
            icon = Icons.Default.Payment,
            iconBackgroundColor = Color(0xFFF3E5F5),
            iconTint = Color(0xFF9C27B0),
        )
    }
}

// ── 8. ApprovalDialog ───────────────────

@MobilePreview
@Composable
private fun PreviewApprovalDialogWithRequester() {
    HelperTheme {
        ApprovalDialog(
            showDialog = true,
            onDismiss = {},
            onApprove = {},
            onReject = {},
            itemName = "Pembelian Semen 100 Zak",
            requestedBy = "Budi Santoso",
            approvalType = "Permintaan",
        )
    }
}

@MobilePreview
@Composable
private fun PreviewApprovalDialogNoRequester() {
    HelperTheme {
        ApprovalDialog(
            showDialog = true,
            onDismiss = {},
            onApprove = {},
            onReject = {},
            itemName = "Diskon Akhir Tahun 15%",
            approvalType = "Pengajuan",
        )
    }
}