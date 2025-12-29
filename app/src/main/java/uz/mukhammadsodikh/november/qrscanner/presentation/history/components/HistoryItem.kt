package uz.mukhammadsodikh.november.qrscanner.presentation.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.mukhammadsodikh.november.qrscanner.core.design.components.*
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.PrimaryBlue
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCodeType
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryItemCard(
    qrCode: QRCode,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    MyCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                imageVector = getIconForType(qrCode.type),
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(spacing.iconSizeLarge)
            )

            Spacer(modifier = Modifier.width(spacing.spaceSmall))

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceExtraSmall)
            ) {
                MyText(
                    text = qrCode.getTitle(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                SubtitleText(
                    text = qrCode.getPreview(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                CaptionText(
                    text = dateFormat.format(qrCode.scannedAt),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(spacing.spaceSmall))

            // Actions
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(spacing.spaceExtraSmall)
            ) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (qrCode.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (qrCode.isFavorite) androidx.compose.ui.graphics.Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun getIconForType(type: QRCodeType): androidx.compose.ui.graphics.vector.ImageVector = when (type) {
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