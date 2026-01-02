package uz.mukhammadsodikh.november.qrscanner.core.design.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════
// 🎨 PREMIUM GRADIENT COLORS (Play Store style)
// ═══════════════════════════════════════════════════════════════

// Primary Gradient (Ko'k-Binafsha)
val PrimaryBlue = Color(0xFF0A84FF)      // Yorqin ko'k
val PrimaryPurple = Color(0xFF5E5CE6)    // Binafsha
val AccentCyan = Color(0xFF64D2FF)       // Och ko'k
val AccentPink = Color(0xFFFF375F)       // Pushti

// Scanner Gradient Colors
val ScannerBlue = Color(0xFF0A84FF)
val ScannerCyan = Color(0xFF64D2FF)
val ScannerPurple = Color(0xFF5E5CE6)

// Success & Status
val SuccessGreen = Color(0xFF34C759)
val ErrorRed = Color(0xFFFF3B30)
val WarningOrange = Color(0xFFFF9500)

// ═══════════════════════════════════════════════════════════════
// 🌞 LIGHT THEME COLORS (Gradient + Glass)
// ═══════════════════════════════════════════════════════════════
val LightBackground = Color(0xFFF5F7FA)          // Och kulrang gradient fon
val LightSurface = Color(0xFFFFFFFF)             // Oq card
val LightSurfaceVariant = Color(0xFFF9FAFB)      // Och-och kulrang
val LightBottomNav = Color(0xFFFDFDFE)           // Bottom nav foni
val LightText = Color(0xFF1A1A1A)                // Qora matn
val LightTextSecondary = Color(0xFF6B7280)       // Kulrang matn
val LightBorder = Color(0x1A000000)              // Transparent chegara

// Glass Effect - Light
val GlassLight = Color(0xF5FFFFFF)               // 96% oq glass
val GlassLightBorder = Color(0x33FFFFFF)         // Glass border
val GlassShadowLight = Color(0x1A000000)         // Glass shadow

// ═══════════════════════════════════════════════════════════════
// 🌙 DARK THEME COLORS (Gradient + Glass)
// ═══════════════════════════════════════════════════════════════
val DarkBackground = Color(0xFF0A0A0F)           // To'q qora gradient fon
val DarkSurface = Color(0xFF1C1C23)              // To'q kulrang card
val DarkSurfaceVariant = Color(0xFF2A2A35)       // Och kulrang variant
val DarkBottomNav = Color(0xFF15151A)            // Bottom nav foni
val DarkText = Color(0xFFFFFFFF)                 // Oq matn
val DarkTextSecondary = Color(0xFF9CA3AF)        // Kulrang matn
val DarkBorder = Color(0x33FFFFFF)               // Transparent chegara

// Glass Effect - Dark
val GlassDark = Color(0xF51C1C23)                // 96% dark glass
val GlassDarkBorder = Color(0x4DFFFFFF)          // Glass border
val GlassShadowDark = Color(0x33000000)          // Glass shadow

// ═══════════════════════════════════════════════════════════════
// 🎯 GRADIENT BRUSHES (Kod ichida ishlatish uchun)
// ═══════════════════════════════════════════════════════════════
// Bu gradient'larni compose ichida Brush.linearGradient() bilan ishlating:
//
// Example:
// modifier = Modifier.background(
//     brush = Brush.linearGradient(
//         colors = listOf(PrimaryBlue, PrimaryPurple)
//     )
// )
// ═══════════════════════════════════════════════════════════════