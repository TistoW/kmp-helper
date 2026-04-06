package com.tisto.kmp.helper.android.base

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.tisto.kmp.helper.utils.MenuEventBus
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init

abstract class BaseComposeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(requireActivity())
    }

    fun setContent(content: @Composable (() -> Unit)): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                content()
            }
        }
    }

    fun backToDashboard() {
        MenuEventBus.trigger.tryEmit(Unit)
    }
}