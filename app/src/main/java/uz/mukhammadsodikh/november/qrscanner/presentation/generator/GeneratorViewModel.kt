package uz.mukhammadsodikh.november.qrscanner.presentation.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.mukhammadsodikh.november.qrscanner.domain.generator.QRCodeGenerator

class GeneratorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GeneratorUiState())
    val uiState: StateFlow<GeneratorUiState> = _uiState.asStateFlow()

    fun selectType(type: QRInputType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    // Text input
    fun updateTextInput(text: String) {
        _uiState.update { it.copy(textInput = text) }
    }

    // URL input
    fun updateUrlInput(url: String) {
        _uiState.update { it.copy(urlInput = url) }
    }

    // Email inputs
    fun updateEmailAddress(email: String) {
        _uiState.update { it.copy(emailAddress = email) }
    }

    fun updateEmailSubject(subject: String) {
        _uiState.update { it.copy(emailSubject = subject) }
    }

    fun updateEmailBody(body: String) {
        _uiState.update { it.copy(emailBody = body) }
    }

    // Phone input
    fun updatePhoneNumber(phone: String) {
        _uiState.update { it.copy(phoneNumber = phone) }
    }

    // WiFi inputs
    fun updateWiFiSSID(ssid: String) {
        _uiState.update { it.copy(wifiSSID = ssid) }
    }

    fun updateWiFiPassword(password: String) {
        _uiState.update { it.copy(wifiPassword = password) }
    }

    fun updateWiFiSecurity(security: String) {
        _uiState.update { it.copy(wifiSecurity = security) }
    }

    // SMS inputs
    fun updateSMSNumber(number: String) {
        _uiState.update { it.copy(smsNumber = number) }
    }

    fun updateSMSMessage(message: String) {
        _uiState.update { it.copy(smsMessage = message) }
    }

    // Barcode input
    fun updateBarcodeInput(code: String) {
        _uiState.update { it.copy(barcodeInput = code) }
    }

    // Generate QR
    fun generateQR() {
        viewModelScope.launch {
            _uiState.update { it.copy(generatorState = GeneratorState.Generating) }

            try {
                val bitmap = withContext(Dispatchers.Default) {
                    when (val type = _uiState.value.selectedType) {
                        is QRInputType.Text -> {
                            val text = _uiState.value.textInput
                            if (text.isBlank()) throw IllegalArgumentException("Text cannot be empty")
                            QRCodeGenerator.generateTextQR(text)
                        }

                        is QRInputType.Url -> {
                            val url = _uiState.value.urlInput
                            if (url.isBlank()) throw IllegalArgumentException("URL cannot be empty")
                            QRCodeGenerator.generateUrlQR(url)
                        }

                        is QRInputType.Email -> {
                            val email = _uiState.value.emailAddress
                            if (email.isBlank()) throw IllegalArgumentException("Email cannot be empty")
                            QRCodeGenerator.generateEmailQR(
                                email = email,
                                subject = _uiState.value.emailSubject.takeIf { it.isNotBlank() },
                                body = _uiState.value.emailBody.takeIf { it.isNotBlank() }
                            )
                        }

                        is QRInputType.Phone -> {
                            val phone = _uiState.value.phoneNumber
                            if (phone.isBlank()) throw IllegalArgumentException("Phone number cannot be empty")
                            QRCodeGenerator.generatePhoneQR(phone)
                        }

                        is QRInputType.WiFi -> {
                            val ssid = _uiState.value.wifiSSID
                            val password = _uiState.value.wifiPassword
                            if (ssid.isBlank()) throw IllegalArgumentException("WiFi SSID cannot be empty")
                            QRCodeGenerator.generateWiFiQR(
                                ssid = ssid,
                                password = password,
                                security = _uiState.value.wifiSecurity
                            )
                        }

                        is QRInputType.SMS -> {
                            val number = _uiState.value.smsNumber
                            if (number.isBlank()) throw IllegalArgumentException("Phone number cannot be empty")
                            QRCodeGenerator.generateSMSQR(
                                number = number,
                                message = _uiState.value.smsMessage.takeIf { it.isNotBlank() }
                            )
                        }

                        is QRInputType.BarcodeEAN13 -> {
                            val code = _uiState.value.barcodeInput
                            if (code.isBlank()) throw IllegalArgumentException("Code cannot be empty")
                            QRCodeGenerator.generateEAN13(code)
                                ?: throw IllegalArgumentException("Invalid EAN-13 code (must be 12-13 digits)")
                        }

                        is QRInputType.BarcodeEAN8 -> {
                            val code = _uiState.value.barcodeInput
                            if (code.isBlank()) throw IllegalArgumentException("Code cannot be empty")
                            QRCodeGenerator.generateEAN8(code)
                                ?: throw IllegalArgumentException("Invalid EAN-8 code (must be 7-8 digits)")
                        }

                        is QRInputType.BarcodeUPCA -> {
                            val code = _uiState.value.barcodeInput
                            if (code.isBlank()) throw IllegalArgumentException("Code cannot be empty")
                            QRCodeGenerator.generateUPCA(code)
                                ?: throw IllegalArgumentException("Invalid UPC-A code (must be 11-12 digits)")
                        }

                        is QRInputType.BarcodeCode128 -> {
                            val code = _uiState.value.barcodeInput
                            if (code.isBlank()) throw IllegalArgumentException("Code cannot be empty")
                            QRCodeGenerator.generateCode128(code)
                                ?: throw IllegalArgumentException("Invalid Code 128")
                        }
                    }
                }

                if (bitmap != null) {
                    _uiState.update {
                        it.copy(
                            generatorState = GeneratorState.Success(bitmap),
                            generatedQRBitmap = bitmap
                        )
                    }
                } else {
                    throw Exception("Failed to generate QR code")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        generatorState = GeneratorState.Error(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    fun resetGenerator() {
        _uiState.update {
            it.copy(
                generatorState = GeneratorState.Idle,
                generatedQRBitmap = null
            )
        }
    }
}