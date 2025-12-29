package uz.mukhammadsodikh.november.qrscanner.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode
import uz.mukhammadsodikh.november.qrscanner.domain.parser.QRCodeParser

class ScannerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun onBarcodeScanned(rawContent: String, format: String) {
        viewModelScope.launch {
            try {
                // Parse QR code content
                val qrCodeType = QRCodeParser.parse(rawContent, format)

                // Create QRCode model
                val qrCode = QRCode(
                    rawContent = rawContent,
                    type = qrCodeType
                )

                // Update state
                _uiState.update {
                    it.copy(
                        scannerState = ScannerState.Success(qrCode),
                        lastScannedCode = qrCode,
                        showResult = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        scannerState = ScannerState.Error(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    fun onPermissionGranted() {
        _uiState.update {
            it.copy(
                isCameraPermissionGranted = true,
                scannerState = ScannerState.Scanning
            )
        }
    }

    fun onPermissionDenied() {
        _uiState.update {
            it.copy(
                isCameraPermissionGranted = false,
                scannerState = ScannerState.Error("Camera permission denied")
            )
        }
    }

    fun dismissResult() {
        _uiState.update {
            it.copy(
                showResult = false,
                scannerState = ScannerState.Scanning
            )
        }
    }

    fun toggleFlash() {
        _uiState.update {
            it.copy(flashEnabled = !it.flashEnabled)
        }
    }

    fun resetScanner() {
        _uiState.update {
            it.copy(
                scannerState = ScannerState.Scanning,
                showResult = false
            )
        }
    }
}