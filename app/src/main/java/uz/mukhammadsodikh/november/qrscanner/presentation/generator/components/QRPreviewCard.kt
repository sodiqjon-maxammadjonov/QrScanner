package uz.mukhammadsodikh.november.qrscanner.presentation.generator.components

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import uz.mukhammadsodikh.november.qrscanner.core.design.components.GlassCard
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing
import uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components.ActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Composable
fun QRPreviewCard(
    bitmap: Bitmap,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    // Entry animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(bitmap) {
        visible = false
        kotlinx.coroutines.delay(100)
        visible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        GlassCard(
            modifier = modifier
                .fillMaxWidth()
                .scale(scale)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
            ) {
                // QR/Barcode Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            // Barcode lar uchun aspect ratio boshqacha
                            if (bitmap.width > bitmap.height) {
                                Modifier.height(200.dp)
                            } else {
                                Modifier.aspectRatio(1f)
                            }
                        )
                        .clip(RoundedCornerShape(spacing.cornerRadiusSmall))
                        .background(androidx.compose.ui.graphics.Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Generated Code",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(spacing.spaceSmall)
                    )
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
                ) {
                    ActionButton(
                        text = "Download",
                        icon = Icons.Default.Download,
                        modifier = Modifier.weight(1f),
                        onClick = { saveQRCode(context, bitmap) }
                    )
                    ActionButton(
                        text = "Share",
                        icon = Icons.Default.Share,
                        modifier = Modifier.weight(1f),
                        isSecondary = true,
                        onClick = { shareQRCode(context, bitmap) }
                    )
                }
            }
        }
    }
}

private fun saveQRCode(context: Context, bitmap: Bitmap) {
    try {
        val filename = "QR_${System.currentTimeMillis()}.png"
        var outputStream: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QR Codes")
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let {
                outputStream = context.contentResolver.openOutputStream(it)
            }
        } else {
            // Android 9 va pastroq
            val imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).toString() + "/QR Codes"
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdirs()
            }
            val image = File(imagesDir, filename)
            outputStream = FileOutputStream(image)
        }

        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(context, "QR Code saved!", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save QR Code", Toast.LENGTH_SHORT).show()
    }
}

private fun shareQRCode(context: Context, bitmap: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "qr_code.png")

        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(android.content.Intent.EXTRA_STREAM, uri)
            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(android.content.Intent.createChooser(intent, "Share QR Code"))
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to share QR Code", Toast.LENGTH_SHORT).show()
    }
}