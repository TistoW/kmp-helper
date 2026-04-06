package com.tisto.kmp.helper.android.utils

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableSharedFlow

object FragmentEventBus {
    val fragmentEvents = MutableSharedFlow<Fragment>(extraBufferCapacity = 1)
}