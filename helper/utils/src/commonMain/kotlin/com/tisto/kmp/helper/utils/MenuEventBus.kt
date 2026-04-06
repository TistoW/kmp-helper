package com.tisto.kmp.helper.utils

import kotlinx.coroutines.flow.MutableSharedFlow

object MenuEventBus {
    val trigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
}