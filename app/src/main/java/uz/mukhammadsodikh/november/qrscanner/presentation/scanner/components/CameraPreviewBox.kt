package uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.*

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
        // Background blur effect (iOS style)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(spacing.cornerRadiusLarge))
                .background(Color.Black.copy(alpha = 0.3f))
                .blur(20.dp)
        )

        // Main camera frame
        CameraFrame(isScanning = isScanning)

        // Scanning line animation
        if (isScanning) {
            ScanningLine()
        }

        // Corner decorations
        ScannerCorners()
    }
}

@Composable
fun CameraFrame(isScanning: Boolean) {
    val spacing = LocalSpacing.current
    val frameSize = spacing.scannerFrameSize

    // Pulsing animation when scanning
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(
        modifier = Modifier
            .size(frameSize)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokeWidth = 3.dp.toPx()

        // Main frame border (dashed)
        drawRoundRect(
            color = if (isScanning) ScannerFrame.copy(alpha = pulseAlpha) else Color.White.copy(0.5f),
            topLeft = Offset.Zero,
            size = Size(canvasWidth, canvasHeight),
            cornerRadius = CornerRadius(24.dp.toPx()),
            style = Stroke(
                width = strokeWidth,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(20f, 10f),
                    phase = 0f
                )
            )
        )

        // Inner glow effect
        drawRoundRect(
            brush = Brush.radialGradient(
                listOf(
                    ScannerFrame.copy(alpha = 0.2f),
                    Color.Transparent
                ),
                center = Offset(canvasWidth / 2, canvasHeight / 2),
                radius = canvasWidth / 2
            ),
            topLeft = Offset.Zero,
            size = Size(canvasWidth, canvasHeight),
            cornerRadius = CornerRadius(24.dp.toPx())
        )
    }
}

@Composable
fun ScanningLine() {
    val spacing = LocalSpacing.current
    val frameSize = spacing.scannerFrameSize

    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = frameSize.value,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan"
    )

    Canvas(
        modifier = Modifier.size(frameSize)
    ) {
        val lineY = offsetY * density
        val canvasWidth = size.width

        // Scanning line with gradient
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Transparent,
                    AccentCyan.copy(alpha = 0.8f),
                    AccentCyan,
                    AccentCyan.copy(alpha = 0.8f),
                    Color.Transparent
                )
            ),
            start = Offset(0f, lineY),
            end = Offset(canvasWidth, lineY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Glow effect above the line
        drawLine(
            brush = Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    AccentCyan.copy(alpha = 0.3f)
                ),
                startY = lineY - 50.dp.toPx(),
                endY = lineY
            ),
            start = Offset(20.dp.toPx(), lineY - 25.dp.toPx()),
            end = Offset(canvasWidth - 20.dp.toPx(), lineY - 25.dp.toPx()),
            strokeWidth = 50.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun ScannerCorners() {
    val spacing = LocalSpacing.current
    val frameSize = spacing.scannerFrameSize
    val cornerLength = spacing.scannerCornerLength
    val cornerWidth = spacing.scannerCornerWidth

    Canvas(modifier = Modifier.size(frameSize)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokeWidth = cornerWidth.toPx()
        val length = cornerLength.toPx()
        val cornerRadius = 24.dp.toPx()

        // Top-Left Corner
        drawPath(
            path = Path().apply {
                moveTo(cornerRadius, 0f)
                lineTo(length, 0f)
                moveTo(0f, cornerRadius)
                lineTo(0f, length)
            },
            color = ScannerCorner,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Top-Right Corner
        drawPath(
            path = Path().apply {
                moveTo(canvasWidth - length, 0f)
                lineTo(canvasWidth - cornerRadius, 0f)
                moveTo(canvasWidth, cornerRadius)
                lineTo(canvasWidth, length)
            },
            color = ScannerCorner,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Bottom-Left Corner
        drawPath(
            path = Path().apply {
                moveTo(0f, canvasHeight - length)
                lineTo(0f, canvasHeight - cornerRadius)
                moveTo(cornerRadius, canvasHeight)
                lineTo(length, canvasHeight)
            },
            color = ScannerCorner,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Bottom-Right Corner
        drawPath(
            path = Path().apply {
                moveTo(canvasWidth, canvasHeight - length)
                lineTo(canvasWidth, canvasHeight - cornerRadius)
                moveTo(canvasWidth - cornerRadius, canvasHeight)
                lineTo(canvasWidth - length, canvasHeight)
            },
            color = ScannerCorner,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}