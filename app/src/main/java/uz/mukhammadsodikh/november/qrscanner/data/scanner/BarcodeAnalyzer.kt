package uz.mukhammadsodikh.november.qrscanner.data.scanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/**
 * CameraX ImageAnalyzer - ML Kit bilan QR/Barcode scanning
 */
class BarcodeAnalyzer(
    private val onBarcodeDetected: (String, String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()
    private var lastScannedValue: String? = null
    private var lastScannedTime = 0L
    private val scanCooldown = 3000L // 3 sekund cooldown (duplicate va frequent scan oldini olish)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let { value ->
                            val currentTime = System.currentTimeMillis()

                            // Duplicate scan oldini olish - bir xil QR va vaqt check qilamiz
                            if (value != lastScannedValue || currentTime - lastScannedTime > scanCooldown) {
                                lastScannedValue = value
                                lastScannedTime = currentTime
                                val format = getBarcodeFormat(barcode.format)
                                onBarcodeDetected(value, format)
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    // Error handling - ignore for now
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun getBarcodeFormat(format: Int): String = when (format) {
        Barcode.FORMAT_QR_CODE -> "QR_CODE"
        Barcode.FORMAT_CODE_128 -> "CODE_128"
        Barcode.FORMAT_CODE_39 -> "CODE_39"
        Barcode.FORMAT_CODE_93 -> "CODE_93"
        Barcode.FORMAT_CODABAR -> "CODABAR"
        Barcode.FORMAT_EAN_13 -> "EAN_13"
        Barcode.FORMAT_EAN_8 -> "EAN_8"
        Barcode.FORMAT_UPC_A -> "UPC_A"
        Barcode.FORMAT_UPC_E -> "UPC_E"
        Barcode.FORMAT_PDF417 -> "PDF417"
        Barcode.FORMAT_AZTEC -> "AZTEC"
        Barcode.FORMAT_DATA_MATRIX -> "DATA_MATRIX"
        else -> "UNKNOWN"
    }
}