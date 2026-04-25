package com.tisto.kmp.helper.ui.icon

import androidx.compose.ui.graphics.vector.ImageVector
import com.tisto.kmp.helper.ui.icon.myicon.IcCameraSolar
import com.tisto.kmp.helper.ui.icon.myicon.IcEyeBrokenSolar
import com.tisto.kmp.helper.ui.icon.myicon.IcLoading
import com.tisto.kmp.helper.ui.icon.myicon.IcPenSolar
import com.tisto.kmp.helper.ui.icon.myicon.IcPenBrokenSolar
import com.tisto.kmp.helper.ui.icon.myicon.IcTrashSolar
import com.tisto.kmp.helper.ui.icon.myicon.IcTrashBrokenSolar
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
            IcEyeBrokenSolar,
            IcLoading,
            IcPenSolar,
            IcPenBrokenSolar,
            IcTrashSolar,
            IcTrashBrokenSolar
        )
        return __AllIcons!!
    }
