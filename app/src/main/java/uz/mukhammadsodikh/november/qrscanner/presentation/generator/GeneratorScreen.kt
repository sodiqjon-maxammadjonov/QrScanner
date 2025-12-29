package uz.mukhammadsodikh.november.qrscanner.presentation.generator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.mukhammadsodikh.november.qrscanner.core.design.components.*
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.LocalSpacing
import uz.mukhammadsodikh.november.qrscanner.presentation.generator.components.QRPreviewCard
import uz.mukhammadsodikh.november.qrscanner.presentation.generator.components.TypeSelector

@Composable
fun GeneratorScreen(
    viewModel: GeneratorViewModel = viewModel()
) {
    val spacing = LocalSpacing.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = spacing.spaceLarge),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceLarge)
        ) {
            // Header
            Column(
                modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
            ) {
                TitleText(text = "Generate QR Code")
                SubtitleText(text = "Create custom QR codes for different purposes")
            }

            // Type selector
            TypeSelector(
                selectedType = uiState.selectedType,
                onTypeSelected = { viewModel.selectType(it) }
            )

            // Input fields based on selected type
            Column(
                modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
            ) {
                when (uiState.selectedType) {
                    is QRInputType.Text -> {
                        InputField(
                            value = uiState.textInput,
                            onValueChange = { viewModel.updateTextInput(it) },
                            placeholder = "Enter text...",
                            maxLines = 5,
                            singleLine = false
                        )
                    }

                    is QRInputType.Url -> {
                        InputField(
                            value = uiState.urlInput,
                            onValueChange = { viewModel.updateUrlInput(it) },
                            placeholder = "https://example.com",
                            keyboardType = KeyboardType.Uri
                        )
                    }

                    is QRInputType.Email -> {
                        InputField(
                            value = uiState.emailAddress,
                            onValueChange = { viewModel.updateEmailAddress(it) },
                            placeholder = "email@example.com",
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                        InputField(
                            value = uiState.emailSubject,
                            onValueChange = { viewModel.updateEmailSubject(it) },
                            placeholder = "Subject (optional)",
                            imeAction = ImeAction.Next
                        )
                        InputField(
                            value = uiState.emailBody,
                            onValueChange = { viewModel.updateEmailBody(it) },
                            placeholder = "Message (optional)",
                            maxLines = 3,
                            singleLine = false
                        )
                    }

                    is QRInputType.Phone -> {
                        InputField(
                            value = uiState.phoneNumber,
                            onValueChange = { viewModel.updatePhoneNumber(it) },
                            placeholder = "+998901234567",
                            keyboardType = KeyboardType.Phone
                        )
                    }

                    is QRInputType.WiFi -> {
                        InputField(
                            value = uiState.wifiSSID,
                            onValueChange = { viewModel.updateWiFiSSID(it) },
                            placeholder = "WiFi Name (SSID)",
                            imeAction = ImeAction.Next
                        )
                        InputField(
                            value = uiState.wifiPassword,
                            onValueChange = { viewModel.updateWiFiPassword(it) },
                            placeholder = "Password",
                            keyboardType = KeyboardType.Password
                        )
                    }

                    is QRInputType.SMS -> {
                        InputField(
                            value = uiState.smsNumber,
                            onValueChange = { viewModel.updateSMSNumber(it) },
                            placeholder = "+998901234567",
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        )
                        InputField(
                            value = uiState.smsMessage,
                            onValueChange = { viewModel.updateSMSMessage(it) },
                            placeholder = "Message (optional)",
                            maxLines = 3,
                            singleLine = false
                        )
                    }

                    is QRInputType.BarcodeEAN13 -> {
                        SubtitleText(text = "EAN-13: 12-13 digits")
                        InputField(
                            value = uiState.barcodeInput,
                            onValueChange = { viewModel.updateBarcodeInput(it) },
                            placeholder = "1234567890123",
                            keyboardType = KeyboardType.Number
                        )
                    }

                    is QRInputType.BarcodeEAN8 -> {
                        SubtitleText(text = "EAN-8: 7-8 digits")
                        InputField(
                            value = uiState.barcodeInput,
                            onValueChange = { viewModel.updateBarcodeInput(it) },
                            placeholder = "12345678",
                            keyboardType = KeyboardType.Number
                        )
                    }

                    is QRInputType.BarcodeUPCA -> {
                        SubtitleText(text = "UPC-A: 11-12 digits")
                        InputField(
                            value = uiState.barcodeInput,
                            onValueChange = { viewModel.updateBarcodeInput(it) },
                            placeholder = "012345678905",
                            keyboardType = KeyboardType.Number
                        )
                    }

                    is QRInputType.BarcodeCode128 -> {
                        SubtitleText(text = "Code 128: Alphanumeric")
                        InputField(
                            value = uiState.barcodeInput,
                            onValueChange = { viewModel.updateBarcodeInput(it) },
                            placeholder = "ABC123XYZ"
                        )
                    }
                }

                // Generate button
                MyButton(
                    text = "Generate QR Code",
                    onClick = { viewModel.generateQR() },
                    isLoading = uiState.generatorState is GeneratorState.Generating
                )
            }

            // QR Preview
            if (uiState.generatedQRBitmap != null) {
                QRPreviewCard(
                    bitmap = uiState.generatedQRBitmap!!,
                    modifier = Modifier.padding(horizontal = spacing.spaceMedium)
                )
            }

            // Error message
            if (uiState.generatorState is GeneratorState.Error) {
                val error = (uiState.generatorState as GeneratorState.Error).message
                MyCard(
                    modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                    backgroundColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                ) {
                    SubtitleText(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}