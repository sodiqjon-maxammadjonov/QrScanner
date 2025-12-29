package uz.mukhammadsodikh.november.qrscanner.presentation.scanner

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uz.mukhammadsodikh.november.qrscanner.core.design.components.MyButton
import uz.mukhammadsodikh.november.qrscanner.core.design.components.SubtitleText
import uz.mukhammadsodikh.november.qrscanner.core.design.components.TitleText
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.PrimaryBlue
import uz.mukhammadsodikh.november.qrscanner.data.preferences.PreferencesManager
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode
import uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components.CameraPreview
import uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components.CameraPreviewBox
import uz.mukhammadsodikh.november.qrscanner.presentation.scanner.components.QRResultCard
import uz.mukhammadsodikh.november.qrscanner.utils.HapticFeedbackManager
import uz.mukhammadsodikh.november.qrscanner.utils.SoundManager
import uz.mukhammadsodikh.november.qrscanner.utils.AdMobManager

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = viewModel(),
    onQRScanned: ((QRCode, String) -> Unit)? = null
) {
    val spacing = LocalSpacing.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferencesManager = remember { PreferencesManager(context) }
    val adMobManager = remember {
        AdMobManager(context).apply {
            initialize()
        }
    }

    // Track scanning state for AdMob
    LaunchedEffect(uiState.showResult) {
        adMobManager.setScanning(!uiState.showResult)
    }

    // Camera permission
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA) { isGranted ->
        if (isGranted) {
            viewModel.onPermissionGranted()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    // Gallery picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                scanImageFromGallery(context, it, viewModel)
            }
        }
    }

    // Permission check
    LaunchedEffect(cameraPermissionState.status.isGranted) {
        if (cameraPermissionState.status.isGranted) {
            viewModel.onPermissionGranted()
        }
    }

    // Save to history and play feedback
    LaunchedEffect(uiState.lastScannedCode) {
        uiState.lastScannedCode?.let { qrCode ->
            onQRScanned?.invoke(qrCode, "QR_CODE")

            scope.launch {
                // Haptic feedback
                val vibrationEnabled = preferencesManager.vibration.first()
                if (vibrationEnabled) {
                    HapticFeedbackManager.vibrateSuccess(context)
                }

                // Sound feedback
                val soundEnabled = preferencesManager.sound.first()
                if (soundEnabled) {
                    SoundManager.playSuccessSound(context)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            // Permission berilmagan
            !cameraPermissionState.status.isGranted -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(spacing.spaceLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TitleText(text = "Camera Permission Required")
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    SubtitleText(text = "Please grant camera permission to scan QR codes")
                    Spacer(modifier = Modifier.height(spacing.spaceLarge))
                    MyButton(
                        text = "Grant Permission",
                        onClick = { cameraPermissionState.launchPermissionRequest() }
                    )
                }
            }

            // Permission berilgan - Camera ko'rsatamiz
            else -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Real Camera Preview with Flash support
                    CameraPreview(
                        onBarcodeDetected = { content, format ->
                            viewModel.onBarcodeScanned(content, format)
                        },
                        flashEnabled = uiState.flashEnabled,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Scanner frame overlay
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CameraPreviewBox(
                            isScanning = !uiState.showResult,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Flash & Gallery buttons (top)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(spacing.spaceMedium)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Flash button
                        FloatingActionButton(
                            onClick = { viewModel.toggleFlash() },
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            contentColor = if (uiState.flashEnabled) PrimaryBlue else MaterialTheme.colorScheme.onSurface
                        ) {
                            Icon(
                                imageVector = if (uiState.flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Flash"
                            )
                        }

                        // Gallery button
                        FloatingActionButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            Icon(
                                imageVector = Icons.Default.Photo,
                                contentDescription = "Gallery"
                            )
                        }
                    }

                    // Result card (agar scan bo'lgan bo'lsa)
                    if (uiState.showResult && uiState.lastScannedCode != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = spacing.spaceExtraLarge),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            QRResultCard(
                                qrCode = uiState.lastScannedCode!!,
                                onDismiss = { viewModel.dismissResult() }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Scan QR from gallery image
 */
private fun scanImageFromGallery(
    context: android.content.Context,
    uri: Uri,
    viewModel: ScannerViewModel
) {
    try {
        val image = InputImage.fromFilePath(context, uri)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.let { barcode ->
                    barcode.rawValue?.let { value ->
                        val format = when (barcode.format) {
                            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE -> "QR_CODE"
                            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_13 -> "EAN_13"
                            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_8 -> "EAN_8"
                            else -> "UNKNOWN"
                        }
                        viewModel.onBarcodeScanned(value, format)
                    }
                }
            }
            .addOnFailureListener {
                // Error - show toast or snackbar
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}