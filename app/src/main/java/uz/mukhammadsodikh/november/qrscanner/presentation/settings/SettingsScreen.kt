package uz.mukhammadsodikh.november.qrscanner.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.mukhammadsodikh.november.qrscanner.core.design.components.*
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.PrimaryBlue
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.PrimaryPurple
import uz.mukhammadsodikh.november.qrscanner.presentation.components.BannerAd
import uz.mukhammadsodikh.november.qrscanner.utils.AdMobManager

@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
    viewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(context) as T
            }
        }
    )
) {
    val spacing = LocalSpacing.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val adMobManager = remember { AdMobManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(vertical = spacing.spaceLarge)
    ) {
        // Header
        Column(
            modifier = Modifier.padding(horizontal = spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
        ) {
            TitleText(text = "Settings")
            SubtitleText(text = "Customize your experience")
        }

        Spacer(modifier = Modifier.height(spacing.spaceLarge))

        // Banner Ad 1
        BannerAd(adMobManager = adMobManager)

        Spacer(modifier = Modifier.height(spacing.spaceMedium))

        // Appearance Section
        SettingsSection(title = "Appearance") {
            SettingsSwitchItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                subtitle = "Enable dark theme",
                checked = uiState.darkMode,
                onCheckedChange = { viewModel.toggleDarkMode() }
            )
        }

        // Preferences Section
        SettingsSection(title = "Preferences") {
//            SettingsSwitchItem(
//                icon = Icons.Default.Vibration,
//                title = "Vibration",
//                subtitle = "Vibrate on scan",
//                checked = uiState.vibration,
//                onCheckedChange = { viewModel.toggleVibration() }
//            )

            SettingsSwitchItem(
                icon = Icons.Default.VolumeUp,
                title = "Sound",
                subtitle = "Play sound on scan",
                checked = uiState.sound,
                onCheckedChange = { viewModel.toggleSound() }
            )

            SettingsSwitchItem(
                icon = Icons.Default.ContentCopy,
                title = "Auto Copy",
                subtitle = "Copy result to clipboard",
                checked = uiState.autoCopy,
                onCheckedChange = { viewModel.toggleAutoCopy() }
            )
        }

        // About Section
        SettingsSection(title = "About") {
            SettingsClickableItem(
                icon = Icons.Default.Star,
                title = "Rate App",
                subtitle = "Rate us on Play Store",
                onClick = {
                    try {
                        // Avval Play Store ilovasini ochishga harakat qilamiz
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("market://details?id=${context.packageName}")
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Agar Play Store ilova o'rnatilmagan bo'lsa, brauzerda ochamiz
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    }
                }
            )

            SettingsClickableItem(
                icon = Icons.Default.Share,
                title = "Share App",
                subtitle = "Share with friends",
                onClick = {
                    val shareText = viewModel.getShareText(context.packageName)

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        putExtra(Intent.EXTRA_SUBJECT, "QR Scanner App")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via"))
                }
            )

            SettingsInfoItem(
                icon = Icons.Default.Info,
                title = "Version",
                value = uiState.appVersion
            )
        }

        // Banner Ad 2
        BannerAd(adMobManager = adMobManager)

        Spacer(modifier = Modifier.height(spacing.spaceMedium))
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = spacing.spaceSmall)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PrimaryBlue, PrimaryPurple)
                        )
                    )
            )

            CaptionText(
                text = title.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
            ) {
                content()
            }
        }

        Spacer(modifier = Modifier.height(spacing.spaceSmall))
    }
}

@Composable
fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(spacing.iconSize)
            )

            Column {
                MyText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                SubtitleText(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryBlue,
                checkedTrackColor = PrimaryBlue.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun SettingsClickableItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(spacing.iconSize)
            )

            Column {
                MyText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                SubtitleText(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun SettingsInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(spacing.iconSize)
            )

            MyText(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }

        SubtitleText(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}