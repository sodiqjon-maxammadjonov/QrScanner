package uz.mukhammadsodikh.november.qrscanner.core.design.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ═══════════════════════════════════════════════════════════════
// 🌙 DARK COLOR SCHEME
// ═══════════════════════════════════════════════════════════════
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryPurple,
    onPrimaryContainer = Color.White,

    secondary = AccentCyan,
    onSecondary = Color.White,
    secondaryContainer = AccentPink,
    onSecondaryContainer = Color.White,

    tertiary = PrimaryPurple,
    onTertiary = Color.White,

    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,

    error = ErrorRed,
    onError = Color.White,

    outline = DarkBorder,
    outlineVariant = DarkBorder.copy(alpha = 0.5f),
    surfaceTint = PrimaryBlue.copy(alpha = 0.1f)
)

// ═══════════════════════════════════════════════════════════════
// 🌞 LIGHT COLOR SCHEME
// ═══════════════════════════════════════════════════════════════
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryPurple,
    onPrimaryContainer = Color.White,

    secondary = AccentCyan,
    onSecondary = Color.White,
    secondaryContainer = AccentPink,
    onSecondaryContainer = Color.White,

    tertiary = PrimaryPurple,
    onTertiary = Color.White,

    background = LightBackground,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,

    error = ErrorRed,
    onError = Color.White,

    outline = LightBorder,
    outlineVariant = LightBorder.copy(alpha = 0.5f),
    surfaceTint = PrimaryBlue.copy(alpha = 0.05f)
)

// ═══════════════════════════════════════════════════════════════
// 🎨 MAIN THEME COMPOSABLE
// ═══════════════════════════════════════════════════════════════
@Composable
fun QRScannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val dimensions = rememberDimensions()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalSpacing provides dimensions) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}