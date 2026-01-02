package uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.*
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun CameraPreviewBox(
    modifier: Modifier = Modifier,
    isScanning: Boolean = false
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(spacing.spaceLarge),
        contentAlignment = Alignment.Center
    ) {
        // Ultra Premium Scanner Frame
        UltraPremiumScannerFrame(
            isScanning = isScanning,
            frameSize = spacing.scannerFrameSize
        )

        // Smooth Scanning Effect
        if (isScanning) {
            SmoothScanningEffect(frameSize = spacing.scannerFrameSize)
        }
    }
}

@Composable
fun UltraPremiumScannerFrame(
    isScanning: Boolean,
    frameSize: androidx.compose.ui.unit.Dp
) {
    val spacing = LocalSpacing.current

    // Breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Canvas(
        modifier = Modifier
            .size(frameSize)
            .graphicsLayer {
                scaleX = if (isScanning) breatheScale else 1f
                scaleY = if (isScanning) breatheScale else 1f
            }
    ) {
        val w = size.width
        val h = size.height
        val cornerLen = spacing.scannerCornerLength.toPx()
        val strokeW = spacing.scannerCornerWidth.toPx()
        val radius = 40.dp.toPx()

        val frameColor = if (isScanning) {
            Color(0xFF0A84FF)
        } else {
            Color.White.copy(alpha = 0.7f)
        }

        // ═══════════════════════════════════════════
        // 🌟 OUTER GLOW (Premium halo effect)
        // ═══════════════════════════════════════════
        if (isScanning) {
            drawRoundRect(
                brush = Brush.radialGradient(
                    listOf(
                        Color(0xFF0A84FF).copy(alpha = glowIntensity * 0.3f),
                        Color(0xFF64D2FF).copy(alpha = glowIntensity * 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(w / 2, h / 2),
                    radius = w / 1.8f
                ),
                topLeft = Offset(-20.dp.toPx(), -20.dp.toPx()),
                size = Size(w + 40.dp.toPx(), h + 40.dp.toPx()),
                cornerRadius = CornerRadius(radius + 20.dp.toPx())
            )
        }

        // ═══════════════════════════════════════════
        // ✨ PREMIUM CORNERS with GRADIENT
        // ═══════════════════════════════════════════
        val cornerGradient = Brush.linearGradient(
            listOf(
                frameColor,
                frameColor.copy(alpha = 0.6f)
            )
        )

        drawCornerPath(
            color = frameColor,
            w = w,
            h = h,
            cornerLen = cornerLen,
            strokeW = strokeW,
            radius = radius,
            glowIntensity = if (isScanning) glowIntensity else 0.5f
        )

        // ═══════════════════════════════════════════
        // 💎 INNER SUBTLE BORDER (depth effect)
        // ═══════════════════════════════════════════
        drawRoundRect(
            color = Color.White.copy(alpha = 0.1f),
            topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
            size = Size(w - 20.dp.toPx(), h - 20.dp.toPx()),
            cornerRadius = CornerRadius(radius - 10.dp.toPx()),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCornerPath(
    color: Color,
    w: Float,
    h: Float,
    cornerLen: Float,
    strokeW: Float,
    radius: Float,
    glowIntensity: Float
) {
    val path = Path()

    // Enhanced glow for each corner
    listOf(
        Offset(cornerLen / 2, cornerLen / 2),
        Offset(w - cornerLen / 2, cornerLen / 2),
        Offset(cornerLen / 2, h - cornerLen / 2),
        Offset(w - cornerLen / 2, h - cornerLen / 2)
    ).forEach { corner ->
        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    color.copy(alpha = glowIntensity * 0.4f),
                    Color.Transparent
                ),
                center = corner,
                radius = 30.dp.toPx()
            ),
            radius = 30.dp.toPx(),
            center = corner
        )
    }

    // ╔ Top-Left
    path.reset()
    path.moveTo(0f, cornerLen)
    path.lineTo(0f, radius)
    path.arcTo(Rect(0f, 0f, radius * 2, radius * 2), 180f, 90f, false)
    path.lineTo(cornerLen, 0f)
    drawPath(path, color, style = Stroke(width = strokeW, cap = StrokeCap.Round))

    // ╗ Top-Right
    path.reset()
    path.moveTo(w - cornerLen, 0f)
    path.lineTo(w - radius, 0f)
    path.arcTo(Rect(w - radius * 2, 0f, w, radius * 2), 270f, 90f, false)
    path.lineTo(w, cornerLen)
    drawPath(path, color, style = Stroke(width = strokeW, cap = StrokeCap.Round))

    // ╚ Bottom-Left
    path.reset()
    path.moveTo(0f, h - cornerLen)
    path.lineTo(0f, h - radius)
    path.arcTo(Rect(0f, h - radius * 2, radius * 2, h), 180f, -90f, false)
    path.lineTo(cornerLen, h)
    drawPath(path, color, style = Stroke(width = strokeW, cap = StrokeCap.Round))

    // ╝ Bottom-Right
    path.reset()
    path.moveTo(w, h - cornerLen)
    path.lineTo(w, h - radius)
    path.arcTo(Rect(w - radius * 2, h - radius * 2, w, h), 0f, 90f, false)
    path.lineTo(w - cornerLen, h)
    drawPath(path, color, style = Stroke(width = strokeW, cap = StrokeCap.Round))
}

@Composable
fun SmoothScanningEffect(frameSize: androidx.compose.ui.unit.Dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")

    // Smooth sine wave movement
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // Breathing pulse
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.size(frameSize)) {
        val w = size.width
        val h = size.height
        val padding = 50.dp.toPx()

        // Sine wave position for smooth motion
        val sineY = (sin(progress * 2 * PI) + 1) / 2
        val lineY = padding + (h - padding * 2) * sineY.toFloat()

        // ═══════════════════════════════════════════
        // 🌊 GRADIENT WAVE (top to bottom fade)
        // ═══════════════════════════════════════════
        val waveHeight = 80.dp.toPx()
        val waveTop = (lineY - waveHeight).coerceAtLeast(padding)

        drawRect(
            brush = Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    Color(0xFF64D2FF).copy(alpha = pulseAlpha * 0.15f),
                    Color(0xFF0A84FF).copy(alpha = pulseAlpha * 0.25f)
                ),
                startY = waveTop,
                endY = lineY
            ),
            topLeft = Offset(padding, waveTop),
            size = Size(w - padding * 2, lineY - waveTop)
        )

        // ═══════════════════════════════════════════
        // ✨ MAIN SCANNING LINE (ultra bright)
        // ═══════════════════════════════════════════
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Transparent,
                    Color(0xFF64D2FF).copy(alpha = pulseAlpha * 0.4f),
                    Color(0xFF0A84FF).copy(alpha = pulseAlpha),
                    Color(0xFF5E5CE6).copy(alpha = pulseAlpha),
                    Color(0xFF64D2FF).copy(alpha = pulseAlpha * 0.4f),
                    Color.Transparent
                )
            ),
            start = Offset(padding, lineY),
            end = Offset(w - padding, lineY),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )

        // ═══════════════════════════════════════════
        // 💫 CENTER GLOW SPOT (focal point)
        // ═══════════════════════════════════════════
        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Color(0xFF0A84FF).copy(alpha = pulseAlpha * 0.8f),
                    Color(0xFF64D2FF).copy(alpha = pulseAlpha * 0.4f),
                    Color(0xFF5E5CE6).copy(alpha = pulseAlpha * 0.2f),
                    Color.Transparent
                ),
                center = Offset(w / 2, lineY),
                radius = 60.dp.toPx()
            ),
            radius = 60.dp.toPx(),
            center = Offset(w / 2, lineY)
        )

        // ═══════════════════════════════════════════
        // ⚡ PARTICLE TRAIL (behind line)
        // ═══════════════════════════════════════════
        for (i in 1..5) {
            val trailY = lineY - (i * 15.dp.toPx())
            if (trailY > padding) {
                drawLine(
                    color = Color(0xFF64D2FF).copy(alpha = pulseAlpha * 0.1f / i),
                    start = Offset(padding + 30.dp.toPx(), trailY),
                    end = Offset(w - padding - 30.dp.toPx(), trailY),
                    strokeWidth = (4 - i).dp.toPx().coerceAtLeast(1f),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}