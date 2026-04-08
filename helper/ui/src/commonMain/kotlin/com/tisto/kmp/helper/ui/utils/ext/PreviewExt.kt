package com.tisto.kmp.helper.ui.utils.ext

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, device = PHONE)
annotation class MobilePreview


@Preview(
    showBackground = true,
    name = "Tablet",
    device = Devices.TABLET,
)
annotation class TabletPreview