package com.tisto.kmp.helper.utils.model

import androidx.compose.runtime.Composable
import com.benasher44.uuid.uuid4
import org.jetbrains.compose.resources.DrawableResource

data class Navigation(
    var name: String = "",
    var id: String = uuid4().toString(),
    var icon: DrawableResource? = null,
    var navItem: List<NavItem> = emptyList(),
    var isLock: Boolean = false,
)

data class NavItem(
    var name: String = "",
    var route: String = "",
    var id: String = uuid4().toString(),
    var icon: DrawableResource? = null,
    var subNavItems: List<NavItem> = emptyList(),
    var isLock: Boolean = false,
    var isAvailable: Boolean = false,
    var screen: (@Composable () -> Unit)? = null,
)