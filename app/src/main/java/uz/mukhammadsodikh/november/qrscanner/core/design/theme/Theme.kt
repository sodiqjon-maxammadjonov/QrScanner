package uz.mukhammadsodikh.november.qrscanner.core.design.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = PrimaryPurple,
    tertiary = AccentCyan,
    background = DarkBackground,
    surface = DarkBottomNav,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = DarkText,
    onSecondary = DarkText,
    onTertiary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = DarkTextSecondary,
    error = ErrorRed,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = PrimaryPurple,
    tertiary = AccentCyan,
    background = LightBackground,
    surface = LightBottomNav,
    surfaceVariant = LightSurfaceVariant,
    onPrimary = LightText,
    onSecondary = LightText,
    onTertiary = LightText,
    onBackground = LightText,
    onSurface = LightText,
    onSurfaceVariant = LightTextSecondary,
    error = ErrorRed,
    outline = LightBorder
)

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
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
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