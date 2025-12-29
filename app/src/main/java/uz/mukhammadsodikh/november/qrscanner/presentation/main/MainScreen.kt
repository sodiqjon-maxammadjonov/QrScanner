package uz.mukhammadsodikh.november.qrscanner.presentation.main

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import uz.mukhammadsodikh.november.qrscanner.core.design.components.CaptionText
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.PrimaryBlue
import uz.mukhammadsodikh.november.qrscanner.presentation.scanner.ScannerScreen
import uz.mukhammadsodikh.november.qrscanner.presentation.history.HistoryScreen
import uz.mukhammadsodikh.november.qrscanner.data.local.dao.QRDatabase
import uz.mukhammadsodikh.november.qrscanner.domain.repository.HistoryRepository
import uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components.CameraPreviewBox
import uz.mukhammadsodikh.november.qrscanner.utils.AdMobManager

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val navigationItems = listOf(
        NavigationItem("Scanner", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItem("Generate", Icons.Filled.Add, Icons.Outlined.Add),
        NavigationItem("History", Icons.Filled.List, Icons.Outlined.List),
        NavigationItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    // History repository for saving scanned codes
    val historyRepository = remember {
        HistoryRepository(QRDatabase.getInstance(context).qrHistoryDao())
    }

    // AdMob Manager
    val adMobManager = remember {
        AdMobManager(context).apply {
            initialize()
        }
    }

    // Show interstitial ad on tab change (except Scanner)
    LaunchedEffect(selectedTab) {
        if (selectedTab != 0 && context is android.app.Activity) {
            adMobManager.showInterstitialAd(context)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            GlassBottomNavigation(
                items = navigationItems,
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> ScannerScreen(
                    onQRScanned = { qrCode, format ->
                        // Save to history when scanned
                        scope.launch {
                            historyRepository.saveQRCode(qrCode, format)
                        }
                    }
                )
                1 -> uz.mukhammadsodikh.november.qrscanner.presentation.generator.GeneratorScreen()
                2 -> HistoryScreen()
                3 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun GlassBottomNavigation(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val spacing = LocalSpacing.current
    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(spacing.cornerRadiusLarge))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            surfaceColor.copy(alpha = 0.95f),
                            surfaceColor.copy(alpha = 0.85f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            borderColor.copy(alpha = 0.3f),
                            borderColor.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(spacing.cornerRadiusLarge)
                )
                .padding(horizontal = spacing.spaceSmall, vertical = spacing.spaceSmall),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                BottomNavItem(
                    item = item,
                    isSelected = selectedIndex == index,
                    onClick = { onItemSelected(index) }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    val scale by animateDpAsState(
        targetValue = if (isSelected) 1.1.dp else 1.dp,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(spacing.cornerRadius))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .scale(scale.value),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    PrimaryBlue.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }

            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title,
                tint = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(spacing.iconSize)
            )
        }

        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))

        CaptionText(
            text = item.title,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// PLACEHOLDER SCREENS - Keyinroq to'ldiramiz

@Composable
fun ScannerScreen() {
    // Ekran ochilganda avtomatik scanning = true
    var isScanning by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Camera Preview Box - iOS 18 style
            CameraPreviewBox(
                isScanning = isScanning,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun GeneratorScreen() {
    uz.mukhammadsodikh.november.qrscanner.presentation.generator.GeneratorScreen()
}

@Composable
fun HistoryScreen() {
    uz.mukhammadsodikh.november.qrscanner.presentation.history.HistoryScreen()
}

@Composable
fun SettingsScreen() {
    uz.mukhammadsodikh.november.qrscanner.presentation.settings.SettingsScreen()
}