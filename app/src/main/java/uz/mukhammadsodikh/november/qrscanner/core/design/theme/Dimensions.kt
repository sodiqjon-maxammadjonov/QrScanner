package uz.mukhammadsodikh.november.qrscanner.core.design.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    // Spacing
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 24.dp,
    val spaceExtraLarge: Dp = 32.dp,

    // Component Sizes
    val buttonHeight: Dp = 56.dp,
    val buttonHeightSmall: Dp = 44.dp,
    val iconSize: Dp = 24.dp,
    val iconSizeLarge: Dp = 48.dp,

    // Border & Corner
    val borderWidth: Dp = 1.dp,
    val cornerRadius: Dp = 16.dp,
    val cornerRadiusLarge: Dp = 24.dp,
    val cornerRadiusSmall: Dp = 12.dp,

    // Card
    val cardElevation: Dp = 0.dp,
    val glassBlur: Dp = 20.dp,

    // Scanner Specific
    val scannerFrameSize: Dp = 280.dp,
    val scannerCornerLength: Dp = 40.dp,
    val scannerCornerWidth: Dp = 4.dp
)

@Composable
fun rememberDimensions(): Dimensions {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    return when {
        // Tablet va iPad (600dp+)
        screenWidth >= 600 -> Dimensions(
            spaceExtraSmall = 6.dp,
            spaceSmall = 12.dp,
            spaceMedium = 20.dp,
            spaceLarge = 32.dp,
            spaceExtraLarge = 48.dp,
            buttonHeight = 64.dp,
            buttonHeightSmall = 52.dp,
            iconSize = 28.dp,
            iconSizeLarge = 64.dp,
            cornerRadius = 20.dp,
            cornerRadiusLarge = 28.dp,
            cornerRadiusSmall = 16.dp,
            scannerFrameSize = 400.dp,
            scannerCornerLength = 60.dp,
            scannerCornerWidth = 5.dp
        )
        // Katta telefon (360dp+)
        screenWidth >= 360 -> Dimensions()
        // Kichik telefon
        else -> Dimensions(
            spaceExtraSmall = 3.dp,
            spaceSmall = 6.dp,
            spaceMedium = 12.dp,
            spaceLarge = 18.dp,
            spaceExtraLarge = 24.dp,
            buttonHeight = 48.dp,
            buttonHeightSmall = 40.dp,
            iconSize = 20.dp,
            iconSizeLarge = 40.dp,
            cornerRadius = 14.dp,
            cornerRadiusLarge = 20.dp,
            cornerRadiusSmall = 10.dp,
            scannerFrameSize = 240.dp,
            scannerCornerLength = 32.dp,
            scannerCornerWidth = 3.dp
        )
    }
}

val LocalSpacing = staticCompositionLocalOf { Dimensions() }