package com.tisto.kmp.helper.ui.ext

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    name = "Tablet",
    device = Devices.TABLET,
)
annotation class TabletPreview

@Preview(
    showBackground = true,
    name = "Mobile",
    device = Devices.PHONE,
)
annotation class MobilePreview

@Preview(
    showBackground = true,
    name = "Desktop Full HD",
    device = Devices.DESKTOP,
)
annotation class DesktopPreview
