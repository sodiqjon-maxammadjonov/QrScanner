package uz.mukhammadsodikh.november.qrscanner.presentation.generator

import android.graphics.Bitmap

/**
 * QR Generator uchun input turlari
 */
sealed class QRInputType {
    object Text : QRInputType()
    object Url : QRInputType()
    object Email : QRInputType()
    object Phone : QRInputType()
    object WiFi : QRInputType()
    object SMS : QRInputType()

    // Barcode types
    object BarcodeEAN13 : QRInputType()
    object BarcodeEAN8 : QRInputType()
    object BarcodeUPCA : QRInputType()
    object BarcodeCode128 : QRInputType()
}

/**
 * Generator state
 */
sealed class GeneratorState {
    object Idle : GeneratorState()
    object Generating : GeneratorState()
    data class Success(val bitmap: Bitmap) : GeneratorState()
    data class Error(val message: String) : GeneratorState()
}

/**
 * UI State
 */
data class GeneratorUiState(
    val selectedType: QRInputType = QRInputType.Text,
    val generatorState: GeneratorState = GeneratorState.Idle,

    // Text input
    val textInput: String = "",

    // URL input
    val urlInput: String = "",

    // Email inputs
    val emailAddress: String = "",
    val emailSubject: String = "",
    val emailBody: String = "",

    // Phone input
    val phoneNumber: String = "",

    // WiFi inputs
    val wifiSSID: String = "",
    val wifiPassword: String = "",
    val wifiSecurity: String = "WPA", // WPA, WEP, nopass

    // SMS inputs
    val smsNumber: String = "",
    val smsMessage: String = "",

    // Barcode input
    val barcodeInput: String = "",

    // Generated QR
    val generatedQRBitmap: Bitmap? = null
)