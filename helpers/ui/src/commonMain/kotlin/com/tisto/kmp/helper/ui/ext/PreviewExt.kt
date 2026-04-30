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
    name = "Mobile Landscape",
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape",
)
annotation class MobileLandscapePreview

@Preview(
    showBackground = true,
    name = "Desktop Full HD",
    device = Devices.DESKTOP,
)
annotation class DesktopPreview
