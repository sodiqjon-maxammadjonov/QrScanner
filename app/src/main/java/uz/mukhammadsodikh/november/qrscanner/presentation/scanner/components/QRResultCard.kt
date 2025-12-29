package uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.mukhammadsodikh.november.qrscanner.core.design.components.*
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.*
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCodeType

@Composable
fun QRResultCard(
    qrCode: QRCode,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    // Entry animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        GlassCard(
            modifier = modifier
                .fillMaxWidth()
                .padding(spacing.spaceMedium)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = getIconForType(qrCode.type),
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(spacing.iconSize)
                        )
                        TitleText(
                            text = qrCode.getTitle(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    // Close button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.5f))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Content preview
                SubtitleText(
                    text = qrCode.getPreview(),
                    style = MaterialTheme.typography.bodyLarge
                )

                // Actions based on type
                QRActionButtons(qrCode = qrCode)
            }
        }
    }
}

@Composable
fun QRActionButtons(qrCode: QRCode) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    when (val type = qrCode.type) {
        is QRCodeType.Url -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
            ) {
                ActionButton(
                    text = "Open Link",
                    icon = Icons.Default.ExitToApp,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(type.url))
                        context.startActivity(intent)
                    }
                )
                ActionButton(
                    text = "Copy",
                    icon = Icons.Default.ContentCopy,
                    modifier = Modifier.weight(1f),
                    isSecondary = true,
                    onClick = { /* Copy to clipboard */ }
                )
            }
        }

        is QRCodeType.Email -> {
            ActionButton(
                text = "Send Email",
                icon = Icons.Default.Email,
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${type.email}")
                        type.subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
                        type.body?.let { putExtra(Intent.EXTRA_TEXT, it) }
                    }
                    context.startActivity(intent)
                }
            )
        }

        is QRCodeType.Phone -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
            ) {
                ActionButton(
                    text = "Call",
                    icon = Icons.Default.Phone,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${type.number}"))
                        context.startActivity(intent)
                    }
                )
                ActionButton(
                    text = "SMS",
                    icon = Icons.Default.Message,
                    modifier = Modifier.weight(1f),
                    isSecondary = true,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${type.number}"))
                        context.startActivity(intent)
                    }
                )
            }
        }

        is QRCodeType.SMS -> {
            ActionButton(
                text = "Send SMS",
                icon = Icons.Default.Message,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("sms:${type.number}")
                        type.message?.let { putExtra("sms_body", it) }
                    }
                    context.startActivity(intent)
                }
            )
        }

        is QRCodeType.Location -> {
            ActionButton(
                text = "Open Maps",
                icon = Icons.Default.LocationOn,
                onClick = {
                    val uri = Uri.parse("geo:${type.latitude},${type.longitude}")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
            )
        }

        is QRCodeType.WiFi -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
            ) {
                SubtitleText(text = "SSID: ${type.ssid}")
                SubtitleText(text = "Password: ${type.password}")
                SubtitleText(text = "Security: ${type.security}")

                ActionButton(
                    text = "Copy Password",
                    icon = Icons.Default.ContentCopy,
                    onClick = { /* Copy password */ }
                )
            }
        }

        else -> {
            ActionButton(
                text = "Copy",
                icon = Icons.Default.ContentCopy,
                onClick = { /* Copy content */ }
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .height(spacing.buttonHeightSmall)
            .clip(RoundedCornerShape(spacing.cornerRadius))
            .then(
                if (isSecondary) {
                    Modifier.background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(0.6f),
                        shape = RoundedCornerShape(spacing.cornerRadius)
                    )
                } else {
                    Modifier.background(
                        brush = Brush.horizontalGradient(listOf(PrimaryBlue, PrimaryPurple)),
                        shape = RoundedCornerShape(spacing.cornerRadius)
                    )
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.spaceMedium),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSecondary) MaterialTheme.colorScheme.onSurface else Color.White,
                modifier = Modifier.size(20.dp)
            )
            MyText(
                text = text,
                color = if (isSecondary) MaterialTheme.colorScheme.onSurface else Color.White,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun getIconForType(type: QRCodeType): ImageVector = when (type) {
    is QRCodeType.Url -> Icons.Default.Link
    is QRCodeType.Email -> Icons.Default.Email
    is QRCodeType.Phone -> Icons.Default.Phone
    is QRCodeType.SMS -> Icons.Default.Message
    is QRCodeType.WiFi -> Icons.Default.Wifi
    is QRCodeType.Location -> Icons.Default.LocationOn
    is QRCodeType.VCard -> Icons.Default.Person
    is QRCodeType.Barcode -> Icons.Default.QrCode
    else -> Icons.Default.Description
}