package com.tisto.kmp.helper.ui.theme

import androidx.compose.ui.unit.dp

object Radius {
    val small = 4.dp
    val box = 6.dp
    val normal = 8.dp
    val medium = 12.dp
    val large = 16.dp
    val extra = 20.dp
}

object Spacing {
    val tiny = 4.dp
    val small = 8.dp
    val box = 12.dp
    val normal = 16.dp    // default
    val medium = 20.dp
    val large = 24.dp
    val extraLarge = 32.dp
    val huge = 48.dp
    val extraHuge = 54.dp
}

object Heights {
    val small = 27.dp   // 35 - 8
    val normal = 35.dp  // baseline baru
    val medium = 43.dp  // 35 + 8
    val large = 51.dp   // 35 + 16
    val extra = 59.dp   // 35 + 24
}

object Padding {
    val tiny = Spacing.tiny
    val small = Spacing.small
    val box = Spacing.box
    val normal = Spacing.normal
    val medium = Spacing.medium
    val large = Spacing.large
    val extra = Spacing.extraLarge
    val extraLarge = Spacing.huge
}

object Margin {
    val tiny = Spacing.tiny
    val small = Spacing.small
    val box = Spacing.box
    val normal = Spacing.normal
    val medium = Spacing.medium
    val large = Spacing.large
    val extra = Spacing.extraLarge
    val extraLarge = Spacing.huge
}

object Dimens {
    // Height
    val smallHeight = Heights.small
    val normalHeight = Heights.normal
    val mediumHeight = Heights.medium
    val largeHeight = Heights.large
    val extraHeight = Heights.extra

    // Corner radius
    val cornerRadiusSmall = Radius.small
    val cornerRadiusNormal = Radius.normal
    val cornerRadiusMedium = Radius.medium
    val cornerRadiusLarge = Radius.large
    val cornerRadiusExtra = Radius.extra

    // Margin
    val marginTiny = Spacing.tiny
    val marginSmall = Spacing.small
    val marginBox = Spacing.box
    val marginNormal = Spacing.normal
    val marginMedium = Spacing.medium
    val marginLarge = Spacing.large
    val marginExtra = Spacing.extraLarge
    val marginExtraLarge = Spacing.huge

    // Padding
    val paddingTiny = Spacing.tiny
    val paddingSmall = Spacing.small
    val paddingBox = Spacing.box
    val paddingNormal = Spacing.normal
    val paddingMedium = Spacing.medium
    val paddingExtra = Spacing.extraLarge
    val paddingExtraLarge = Spacing.huge
}