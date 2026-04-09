package com.tisto.kmp.helper.utils.android.ext

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableSharedFlow

object FragmentEventBus {
    val fragmentEvents = MutableSharedFlow<Fragment>(extraBufferCapacity = 1)
}