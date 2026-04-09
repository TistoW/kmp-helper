package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter

fun isMobilePhone(): Boolean = false // for real device android

@Composable
fun String.title(isDataAvailable: Boolean): String {
    val title = "${
//        if (isDataAvailable) "${stringResource(R.string.detail)} "
//        else "${stringResource(R.string.tambah)} "
        if (isDataAvailable) "Detail "
        else "Tambah "
    }$this"
    return title
}

@Composable
fun ImageVector.toPainter(): Painter {
    return rememberVectorPainter(image = this)
}