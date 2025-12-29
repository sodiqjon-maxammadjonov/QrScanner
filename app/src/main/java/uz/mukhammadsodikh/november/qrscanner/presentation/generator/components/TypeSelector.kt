package uz.mukhammadsodikh.november.qrscanner.presentation.generator.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import uz.mukhammadsodikh.november.qrscanner.core.design.components.CaptionText
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.*
import uz.mukhammadsodikh.november.qrscanner.presentation.generator.QRInputType

data class TypeOption(
    val type: QRInputType,
    val title: String,
    val icon: ImageVector
)

@Composable
fun TypeSelector(
    selectedType: QRInputType,
    onTypeSelected: (QRInputType) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    val types = listOf(
        TypeOption(QRInputType.Text, "Text", Icons.Default.Description),
        TypeOption(QRInputType.Url, "URL", Icons.Default.Link),
        TypeOption(QRInputType.Email, "Email", Icons.Default.Email),
        TypeOption(QRInputType.Phone, "Phone", Icons.Default.Phone),
        TypeOption(QRInputType.WiFi, "WiFi", Icons.Default.Wifi),
        TypeOption(QRInputType.SMS, "SMS", Icons.Default.Message),
        TypeOption(QRInputType.BarcodeEAN13, "EAN-13", Icons.Default.QrCode),
        TypeOption(QRInputType.BarcodeEAN8, "EAN-8", Icons.Default.QrCode),
        TypeOption(QRInputType.BarcodeUPCA, "UPC-A", Icons.Default.QrCode),
        TypeOption(QRInputType.BarcodeCode128, "Code128", Icons.Default.QrCode)
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
        contentPadding = PaddingValues(horizontal = spacing.spaceMedium)
    ) {
        items(types) { typeOption ->
            TypeChip(
                typeOption = typeOption,
                isSelected = selectedType == typeOption.type,
                onClick = { onTypeSelected(typeOption.type) }
            )
        }
    }
}

@Composable
fun TypeChip(
    typeOption: TypeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Column(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(spacing.cornerRadius))
            .then(
                if (isSelected) {
                    Modifier.background(
                        brush = Brush.linearGradient(
                            listOf(PrimaryBlue, PrimaryPurple)
                        ),
                        shape = RoundedCornerShape(spacing.cornerRadius)
                    )
                } else {
                    Modifier.background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(spacing.cornerRadius)
                    )
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.spaceExtraSmall)
    ) {
        Icon(
            imageVector = typeOption.icon,
            contentDescription = typeOption.title,
            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(spacing.iconSize)
        )
        CaptionText(
            text = typeOption.title,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}