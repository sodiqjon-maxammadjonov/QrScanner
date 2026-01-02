package uz.mukhammadsodikh.november.qrscanner.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing

@Composable
fun MyCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
    borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(spacing.cornerRadius))
            .background(backgroundColor)
            .border(
                width = spacing.borderWidth,
                brush = Brush.verticalGradient(
                    listOf(
                        borderColor.copy(alpha = 0.3f),
                        borderColor.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(spacing.cornerRadius)
            )
            .padding(spacing.spaceMedium)
    ) {
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = LocalSpacing.current
    val isDark = MaterialTheme.colorScheme.background == Color(0xFF000000)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(spacing.cornerRadiusLarge))
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        if (isDark) Color(0x33FFFFFF) else Color(0xCCFFFFFF),
                        if (isDark) Color(0x1AFFFFFF) else Color(0xB3FFFFFF)
                    )
                )
            )
            .border(
                width = spacing.borderWidth,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(spacing.cornerRadiusLarge)
            )
            .padding(spacing.spaceLarge)
    ) {
        content()
    }
}