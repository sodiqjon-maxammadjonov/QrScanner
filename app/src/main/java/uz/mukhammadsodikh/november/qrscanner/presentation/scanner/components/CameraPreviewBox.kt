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
        // Premium Scanner Frame
        PremiumScannerFrame(
            isScanning = isScanning,
            frameSize = spacing.scannerFrameSize
        )

        // Smooth Scanning Effect
        if (isScanning) {
            PremiumScanningEffect(frameSize = spacing.scannerFrameSize)
        }
    }
}

@Composable
fun PremiumScannerFrame(
    isScanning: Boolean,
    frameSize: androidx.compose.ui.unit.Dp
) {
    val spacing = LocalSpacing.current

    // Breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")

    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
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
        val radius = 50.dp.toPx()

        // ═══════════════════════════════════════
        // 🌟 OUTER PREMIUM GLOW
        // ═══════════════════════════════════════
        if (isScanning) {
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ScannerBlue.copy(alpha = glowIntensity * 0.4f),
                        ScannerCyan.copy(alpha = glowIntensity * 0.2f),
                        Color.Transparent
                    ),
                    center = Offset(w / 2, h / 2),
                    radius = w / 1.6f
                ),
                topLeft = Offset(-30.dp.toPx(), -30.dp.toPx()),
                size = Size(w + 60.dp.toPx(), h + 60.dp.toPx()),
                cornerRadius = CornerRadius(radius + 30.dp.toPx())
            )
        }

        // ═══════════════════════════════════════
        // 💎 GRADIENT CORNERS
        // ═══════════════════════════════════════
        val cornerGradient = Brush.linearGradient(
            colors = if (isScanning) {
                listOf(ScannerBlue, ScannerCyan, ScannerPurple)
            } else {
                listOf(
                    Color.White.copy(alpha = 0.7f),
                    Color.White.copy(alpha = 0.5f)
                )
            }
        )

        drawPremiumCorners(
            w = w,
            h = h,
            cornerLen = cornerLen,
            strokeW = strokeW,
            radius = radius,
            brush = cornerGradient,
            glowIntensity = if (isScanning) glowIntensity else 0.5f
        )

        // ═══════════════════════════════════════
        // ✨ INNER BORDER (depth effect)
        // ═══════════════════════════════════════
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.15f),
                    Color.White.copy(alpha = 0.05f)
                )
            ),
            topLeft = Offset(12.dp.toPx(), 12.dp.toPx()),
            size = Size(w - 24.dp.toPx(), h - 24.dp.toPx()),
            cornerRadius = CornerRadius(radius - 12.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // ═══════════════════════════════════════
        // 🔷 CORNER GLOW SPOTS
        // ═══════════════════════════════════════
        if (isScanning) {
            listOf(
                Offset(cornerLen / 2, cornerLen / 2),
                Offset(w - cornerLen / 2, cornerLen / 2),
                Offset(cornerLen / 2, h - cornerLen / 2),
                Offset(w - cornerLen / 2, h - cornerLen / 2)
            ).forEach { corner ->
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ScannerCyan.copy(alpha = glowIntensity * 0.6f),
                            Color.Transparent
                        ),
                        center = corner,
                        radius = 40.dp.toPx()
                    ),
                    radius = 40.dp.toPx(),
                    center = corner
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPremiumCorners(
    w: Float,
    h: Float,
    cornerLen: Float,
    strokeW: Float,
    radius: Float,
    brush: Brush,
    glowIntensity: Float
) {
    val path = Path()

    // ╔ Top-Left Corner
    path.reset()
    path.moveTo(0f, cornerLen)
    path.lineTo(0f, radius)
    path.arcTo(Rect(0f, 0f, radius * 2, radius * 2), 180f, 90f, false)
    path.lineTo(cornerLen, 0f)
    drawPath(
        path = path,
        brush = brush,
        style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // ╗ Top-Right Corner
    path.reset()
    path.moveTo(w - cornerLen, 0f)
    path.lineTo(w - radius, 0f)
    path.arcTo(Rect(w - radius * 2, 0f, w, radius * 2), 270f, 90f, false)
    path.lineTo(w, cornerLen)
    drawPath(
        path = path,
        brush = brush,
        style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // ╚ Bottom-Left Corner
    path.reset()
    path.moveTo(0f, h - cornerLen)
    path.lineTo(0f, h - radius)
    path.arcTo(Rect(0f, h - radius * 2, radius * 2, h), 180f, -90f, false)
    path.lineTo(cornerLen, h)
    drawPath(
        path = path,
        brush = brush,
        style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // ╝ Bottom-Right Corner
    path.reset()
    path.moveTo(w, h - cornerLen)
    path.lineTo(w, h - radius)
    path.arcTo(Rect(w - radius * 2, h - radius * 2, w, h), 0f, 90f, false)
    path.lineTo(w - cornerLen, h)
    drawPath(
        path = path,
        brush = brush,
        style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

@Composable
fun PremiumScanningEffect(frameSize: androidx.compose.ui.unit.Dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")

    // Smooth sine wave movement
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // Breathing pulse
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.size(frameSize)) {
        val w = size.width
        val h = size.height
        val padding = 60.dp.toPx()

        // Sine wave position
        val sineY = (sin(progress * 2 * PI) + 1) / 2
        val lineY = padding + (h - padding * 2) * sineY.toFloat()

        // ═══════════════════════════════════════
        // 🌊 GRADIENT WAVE (top to bottom)
        // ═══════════════════════════════════════
        val waveHeight = 100.dp.toPx()
        val waveTop = (lineY - waveHeight).coerceAtLeast(padding)

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    ScannerCyan.copy(alpha = pulseAlpha * 0.15f),
                    ScannerBlue.copy(alpha = pulseAlpha * 0.3f)
                ),
                startY = waveTop,
                endY = lineY
            ),
            topLeft = Offset(padding, waveTop),
            size = Size(w - padding * 2, lineY - waveTop)
        )

        // ═══════════════════════════════════════
        // ✨ MAIN SCANNING LINE
        // ═══════════════════════════════════════
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    ScannerCyan.copy(alpha = pulseAlpha * 0.5f),
                    ScannerBlue.copy(alpha = pulseAlpha),
                    ScannerPurple.copy(alpha = pulseAlpha),
                    ScannerCyan.copy(alpha = pulseAlpha * 0.5f),
                    Color.Transparent
                )
            ),
            start = Offset(padding, lineY),
            end = Offset(w - padding, lineY),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // ═══════════════════════════════════════
        // 💫 CENTER GLOW SPOT
        // ═══════════════════════════════════════
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ScannerBlue.copy(alpha = pulseAlpha * 0.9f),
                    ScannerCyan.copy(alpha = pulseAlpha * 0.5f),
                    ScannerPurple.copy(alpha = pulseAlpha * 0.3f),
                    Color.Transparent
                ),
                center = Offset(w / 2, lineY),
                radius = 80.dp.toPx()
            ),
            radius = 80.dp.toPx(),
            center = Offset(w / 2, lineY)
        )

        // ═══════════════════════════════════════
        // ⚡ PARTICLE TRAILS
        // ═══════════════════════════════════════
        for (i in 1..6) {
            val trailY = lineY - (i * 18.dp.toPx())
            if (trailY > padding) {
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            ScannerCyan.copy(alpha = pulseAlpha * 0.15f / i),
                            ScannerBlue.copy(alpha = pulseAlpha * 0.2f / i),
                            ScannerCyan.copy(alpha = pulseAlpha * 0.15f / i),
                            Color.Transparent
                        )
                    ),
                    start = Offset(padding + 40.dp.toPx(), trailY),
                    end = Offset(w - padding - 40.dp.toPx(), trailY),
                    strokeWidth = (5 - i * 0.5f).dp.toPx().coerceAtLeast(1f),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}