package uz.mukhammadsodikh.november.qrscanner.presentation.scanner

import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode

sealed class ScannerState {
    object Idle : ScannerState()
    object Scanning : ScannerState()
    data class Success(val qrCode: QRCode) : ScannerState()
    data class Error(val message: String) : ScannerState()
}

data class ScannerUiState(
    val scannerState: ScannerState = ScannerState.Idle,
    val isCameraPermissionGranted: Boolean = false,
    val flashEnabled: Boolean = false,
    val lastScannedCode: QRCode? = null,
    val showResult: Boolean = false
)