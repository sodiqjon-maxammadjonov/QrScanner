package uz.mukhammadsodikh.november.qrscanner.core.design.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ═══════════════════════════════════════════════════════════════
// 📐 RESPONSIVE DIMENSIONS (Phone, Tablet, iPad responsive)
// ═══════════════════════════════════════════════════════════════
data class Dimensions(
    // ─────────── Spacing ───────────
    val spaceExtraSmall: Dp,
    val spaceSmall: Dp,
    val spaceMedium: Dp,
    val spaceLarge: Dp,
    val spaceExtraLarge: Dp,

    // ─────────── Component Sizes ───────────
    val buttonHeight: Dp,
    val buttonHeightSmall: Dp,
    val iconSize: Dp,
    val iconSizeLarge: Dp,
    val iconSizeSmall: Dp,

    // ─────────── Border & Corner ───────────
    val borderWidth: Dp,
    val cornerRadius: Dp,
    val cornerRadiusLarge: Dp,
    val cornerRadiusSmall: Dp,

    // ─────────── Card & Glass ───────────
    val cardElevation: Dp,
    val glassBlur: Dp,
    val cardPadding: Dp,

    // ─────────── Scanner Specific ───────────
    val scannerFrameSize: Dp,
    val scannerCornerLength: Dp,
    val scannerCornerWidth: Dp,

    // ─────────── Bottom Navigation ───────────
    val bottomNavHeight: Dp,
    val bottomNavIconSize: Dp
)

// ═══════════════════════════════════════════════════════════════
// 📱 PHONE DIMENSIONS (Default)
// ═══════════════════════════════════════════════════════════════
private val PhoneDimensions = Dimensions(
    spaceExtraSmall = 4.dp,
    spaceSmall = 8.dp,
    spaceMedium = 16.dp,
    spaceLarge = 24.dp,
    spaceExtraLarge = 32.dp,

    buttonHeight = 56.dp,
    buttonHeightSmall = 44.dp,
    iconSize = 24.dp,
    iconSizeLarge = 48.dp,
    iconSizeSmall = 20.dp,

    borderWidth = 1.dp,
    cornerRadius = 16.dp,
    cornerRadiusLarge = 24.dp,
    cornerRadiusSmall = 12.dp,

    cardElevation = 0.dp,
    glassBlur = 20.dp,
    cardPadding = 16.dp,

    scannerFrameSize = 280.dp,
    scannerCornerLength = 40.dp,
    scannerCornerWidth = 4.dp,

    bottomNavHeight = 72.dp,
    bottomNavIconSize = 24.dp
)

// ═══════════════════════════════════════════════════════════════
// 📲 SMALL PHONE DIMENSIONS (< 360dp width)
// ═══════════════════════════════════════════════════════════════
private val SmallPhoneDimensions = Dimensions(
    spaceExtraSmall = 3.dp,
    spaceSmall = 6.dp,
    spaceMedium = 12.dp,
    spaceLarge = 18.dp,
    spaceExtraLarge = 24.dp,

    buttonHeight = 48.dp,
    buttonHeightSmall = 40.dp,
    iconSize = 20.dp,
    iconSizeLarge = 40.dp,
    iconSizeSmall = 18.dp,

    borderWidth = 1.dp,
    cornerRadius = 14.dp,
    cornerRadiusLarge = 20.dp,
    cornerRadiusSmall = 10.dp,

    cardElevation = 0.dp,
    glassBlur = 16.dp,
    cardPadding = 12.dp,

    scannerFrameSize = 240.dp,
    scannerCornerLength = 32.dp,
    scannerCornerWidth = 3.dp,

    bottomNavHeight = 64.dp,
    bottomNavIconSize = 22.dp
)

// ═══════════════════════════════════════════════════════════════
// 📱 LARGE PHONE DIMENSIONS (> 400dp width)
// ═══════════════════════════════════════════════════════════════
private val LargePhoneDimensions = Dimensions(
    spaceExtraSmall = 5.dp,
    spaceSmall = 10.dp,
    spaceMedium = 18.dp,
    spaceLarge = 28.dp,
    spaceExtraLarge = 40.dp,

    buttonHeight = 60.dp,
    buttonHeightSmall = 48.dp,
    iconSize = 26.dp,
    iconSizeLarge = 52.dp,
    iconSizeSmall = 22.dp,

    borderWidth = 1.5.dp,
    cornerRadius = 18.dp,
    cornerRadiusLarge = 26.dp,
    cornerRadiusSmall = 14.dp,

    cardElevation = 0.dp,
    glassBlur = 24.dp,
    cardPadding = 18.dp,

    scannerFrameSize = 320.dp,
    scannerCornerLength = 48.dp,
    scannerCornerWidth = 4.5.dp,

    bottomNavHeight = 76.dp,
    bottomNavIconSize = 26.dp
)

// ═══════════════════════════════════════════════════════════════
// 📱 TABLET DIMENSIONS (> 600dp width)
// ═══════════════════════════════════════════════════════════════
private val TabletDimensions = Dimensions(
    spaceExtraSmall = 6.dp,
    spaceSmall = 12.dp,
    spaceMedium = 24.dp,
    spaceLarge = 36.dp,
    spaceExtraLarge = 56.dp,

    buttonHeight = 68.dp,
    buttonHeightSmall = 56.dp,
    iconSize = 32.dp,
    iconSizeLarge = 72.dp,
    iconSizeSmall = 26.dp,

    borderWidth = 2.dp,
    cornerRadius = 24.dp,
    cornerRadiusLarge = 32.dp,
    cornerRadiusSmall = 18.dp,

    cardElevation = 0.dp,
    glassBlur = 28.dp,
    cardPadding = 24.dp,

    scannerFrameSize = 440.dp,
    scannerCornerLength = 70.dp,
    scannerCornerWidth = 6.dp,

    bottomNavHeight = 88.dp,
    bottomNavIconSize = 32.dp
)

// ═══════════════════════════════════════════════════════════════
// 📏 RESPONSIVE DIMENSION SELECTOR
// ═══════════════════════════════════════════════════════════════
@Composable
fun rememberDimensions(): Dimensions {
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    // Screen width (dp)
    val screenWidthDp = config.screenWidthDp

    // Screen height (dp)
    val screenHeightDp = config.screenHeightDp

    // Smallest width (sw) - iPhone va iPad'ni farqlash uchun
    val smallestWidth = minOf(screenWidthDp, screenHeightDp)

    return when {
        // 🖥 Tablet / iPad (sw >= 600dp)
        smallestWidth >= 600 -> TabletDimensions

        // 📱 Large Phone (w >= 400dp, masalan iPhone 14 Pro Max)
        screenWidthDp >= 400 -> LargePhoneDimensions

        // 📱 Normal Phone (w >= 360dp, masalan iPhone 12/13/14)
        screenWidthDp >= 360 -> PhoneDimensions

        // 📲 Small Phone (w < 360dp, masalan iPhone SE)
        else -> SmallPhoneDimensions
    }
}

// ═══════════════════════════════════════════════════════════════
// 🎯 LOCAL COMPOSITION
// ═══════════════════════════════════════════════════════════════
val LocalSpacing = staticCompositionLocalOf { PhoneDimensions }

// ═══════════════════════════════════════════════════════════════
// 🔍 DEVICE TYPE CHECKER (Optional utility)
// ═══════════════════════════════════════════════════════════════
enum class DeviceType {
    SMALL_PHONE,  // < 360dp
    PHONE,        // 360-400dp
    LARGE_PHONE,  // 400-600dp
    TABLET        // >= 600dp
}

@Composable
fun currentDeviceType(): DeviceType {
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp
    val smallestWidth = minOf(screenWidthDp, config.screenHeightDp)

    return when {
        smallestWidth >= 600 -> DeviceType.TABLET
        screenWidthDp >= 400 -> DeviceType.LARGE_PHONE
        screenWidthDp >= 360 -> DeviceType.PHONE
        else -> DeviceType.SMALL_PHONE
    }
}