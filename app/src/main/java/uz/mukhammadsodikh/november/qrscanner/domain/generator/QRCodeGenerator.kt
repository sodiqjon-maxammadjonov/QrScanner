package uz.mukhammadsodikh.november.qrscanner.domain.generator

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.MultiFormatWriter

/**
 * QR Code va Barcode yaratish
 */
object QRCodeGenerator {

    /**
     * QR code bitmap yaratish
     */
    fun generateQRCode(
        content: String,
        size: Int = 800,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap? {
        return try {
            val hints = hashMapOf<EncodeHintType, Any>().apply {
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
                put(EncodeHintType.MARGIN, 1)
            }

            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)

            createBitmapFromMatrix(bitMatrix, foregroundColor, backgroundColor)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Barcode bitmap yaratish
     */
    fun generateBarcode(
        content: String,
        format: BarcodeFormat,
        width: Int = 800,
        height: Int = 300,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap? {
        return try {
            val hints = hashMapOf<EncodeHintType, Any>().apply {
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.MARGIN, 1)
            }

            val writer = MultiFormatWriter()
            val bitMatrix = writer.encode(content, format, width, height, hints)

            createBitmapFromMatrix(bitMatrix, foregroundColor, backgroundColor)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createBitmapFromMatrix(
        bitMatrix: com.google.zxing.common.BitMatrix,
        foregroundColor: Int,
        backgroundColor: Int
    ): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix[x, y]) foregroundColor else backgroundColor
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }

    // QR Code methods
    fun generateTextQR(text: String, size: Int = 800): Bitmap? {
        return generateQRCode(text, size)
    }

    fun generateUrlQR(url: String, size: Int = 800): Bitmap? {
        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }
        return generateQRCode(formattedUrl, size)
    }

    fun generateEmailQR(
        email: String,
        subject: String? = null,
        body: String? = null,
        size: Int = 800
    ): Bitmap? {
        val content = buildString {
            append("mailto:$email")
            val params = mutableListOf<String>()
            subject?.let { params.add("subject=$it") }
            body?.let { params.add("body=$it") }
            if (params.isNotEmpty()) {
                append("?${params.joinToString("&")}")
            }
        }
        return generateQRCode(content, size)
    }

    fun generatePhoneQR(phoneNumber: String, size: Int = 800): Bitmap? {
        return generateQRCode("tel:$phoneNumber", size)
    }

    fun generateWiFiQR(
        ssid: String,
        password: String,
        security: String = "WPA",
        size: Int = 800
    ): Bitmap? {
        val content = "WIFI:T:$security;S:$ssid;P:$password;;"
        return generateQRCode(content, size)
    }

    fun generateSMSQR(
        number: String,
        message: String? = null,
        size: Int = 800
    ): Bitmap? {
        val content = buildString {
            append("sms:$number")
            message?.let { append("?body=$it") }
        }
        return generateQRCode(content, size)
    }

    // Barcode methods
    fun generateEAN13(code: String): Bitmap? {
        // EAN-13 must be 12 or 13 digits
        val cleanCode = code.filter { it.isDigit() }
        if (cleanCode.length !in 12..13) return null
        return generateBarcode(cleanCode, BarcodeFormat.EAN_13)
    }

    fun generateEAN8(code: String): Bitmap? {
        // EAN-8 must be 7 or 8 digits
        val cleanCode = code.filter { it.isDigit() }
        if (cleanCode.length !in 7..8) return null
        return generateBarcode(cleanCode, BarcodeFormat.EAN_8)
    }

    fun generateUPCA(code: String): Bitmap? {
        // UPC-A must be 11 or 12 digits
        val cleanCode = code.filter { it.isDigit() }
        if (cleanCode.length !in 11..12) return null
        return generateBarcode(cleanCode, BarcodeFormat.UPC_A)
    }

    fun generateCode128(code: String): Bitmap? {
        // Code 128 supports alphanumeric
        if (code.isEmpty()) return null
        return generateBarcode(code, BarcodeFormat.CODE_128)
    }
}