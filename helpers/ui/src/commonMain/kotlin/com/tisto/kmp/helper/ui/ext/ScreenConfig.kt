package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ========== ScreenConfig.kt ==========
data class ScreenConfig(
    val maxWidth: Dp = 500.dp
) {
    val deviceType: DeviceType
        get() = when {
            maxWidth < 600.dp -> DeviceType.Mobile
            maxWidth < 1601.dp -> DeviceType.Tablet
            else -> DeviceType.Desktop
        }

    // ✅ Overlapping logic
    val isMobile get() = maxWidth < 600.dp
    val isTablet get() = maxWidth >= 600.dp           // TRUE untuk Tablet & Desktop
    val isDesktop get() = maxWidth >= 1601.dp         // TRUE untuk Desktop saja
    val isNotMobile get() = !isMobile


    // Bonus: Ekslusif checks
    val isMobileOnly get() = deviceType == DeviceType.Mobile
    val isTabletOnly get() = deviceType == DeviceType.Tablet
    val isDesktopOnly get() = deviceType == DeviceType.Desktop

    fun getHorizontalPaddingListWeight(weight: Float? = null): Float {
        return weight ?: if (isMobileOnly) 1f
        else if (isTabletOnly) 0.9f
        else if (isDesktopOnly) 0.7f
        else 1f
    }
    fun getHorizontalPaddingFormWeight(weight: Float? = null): Float {
        return weight ?: if (isMobileOnly) 1f
        else if (isTabletOnly) 0.6f
        else if (isDesktopOnly) 0.5f
        else 1f
    }
    fun getVerticalPadding(weight: Float? = null): Float {
        return weight ?: if (isMobileOnly) 1f
        else if (isTabletOnly) 0.8f
        else if (isDesktopOnly) 0.7f
        else 1f
    }
}

enum class DeviceType { Mobile, Tablet, Desktop }

val LocalScreenConfig = compositionLocalOf<ScreenConfig> {
    error("No ScreenConfig provided")
}