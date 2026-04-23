package com.tisto.kmp.helper.ui.icon

import androidx.compose.ui.graphics.vector.ImageVector
import com.tisto.kmp.helper.ui.icon.myicon.IcCameraSolar
import com.tisto.kmp.helper.ui.icon.myicon.IcLoading
import kotlin.collections.List as ____KtList

public object MyIcon

private var __AllIcons: ____KtList<ImageVector>? = null

public val MyIcon.AllIcons: ____KtList<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf(
            IcCameraSolar,
            IcLoading
        )
        return __AllIcons!!
    }
