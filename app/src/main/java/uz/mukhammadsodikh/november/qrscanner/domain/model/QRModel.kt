package uz.mukhammadsodikh.november.qrscanner.domain.model

import java.util.Date
sealed class QRCodeType {
    data class Text(val content: String) : QRCodeType()
    data class Url(val url: String) : QRCodeType()
    data class Email(val email: String, val subject: String? = null, val body: String? = null) : QRCodeType()
    data class Phone(val number: String) : QRCodeType()
    data class SMS(val number: String, val message: String? = null) : QRCodeType()
    data class WiFi(val ssid: String, val password: String, val security: String) : QRCodeType()
    data class VCard(val name: String, val phone: String?, val email: String?, val organization: String?) : QRCodeType()
    data class Location(val latitude: Double, val longitude: Double) : QRCodeType()
    data class Barcode(val code: String, val format: String) : QRCodeType()
    data class Unknown(val rawData: String) : QRCodeType()
}

data class QRCode(
    val id: String = java.util.UUID.randomUUID().toString(),
    val rawContent: String,
    val type: QRCodeType,
    val scannedAt: Date = Date(),
    val isFavorite: Boolean = false
) {
    fun getTitle(): String = when (type) {
        is QRCodeType.Text -> "Text"
        is QRCodeType.Url -> "Website"
        is QRCodeType.Email -> "Email"
        is QRCodeType.Phone -> "Phone"
        is QRCodeType.SMS -> "SMS"
        is QRCodeType.WiFi -> "WiFi"
        is QRCodeType.VCard -> "Contact"
        is QRCodeType.Location -> "Location"
        is QRCodeType.Barcode -> "Barcode"
        is QRCodeType.Unknown -> "Unknown"
    }

    fun getPreview(): String = when (type) {
        is QRCodeType.Text -> type.content.take(50)
        is QRCodeType.Url -> type.url
        is QRCodeType.Email -> type.email
        is QRCodeType.Phone -> type.number
        is QRCodeType.SMS -> type.number
        is QRCodeType.WiFi -> type.ssid
        is QRCodeType.VCard -> type.name
        is QRCodeType.Location -> "Lat: ${type.latitude}, Lng: ${type.longitude}"
        is QRCodeType.Barcode -> type.code
        is QRCodeType.Unknown -> rawContent.take(50)
    }

    fun isActionable(): Boolean = when (type) {
        is QRCodeType.Url,
        is QRCodeType.Email,
        is QRCodeType.Phone,
        is QRCodeType.SMS,
        is QRCodeType.Location -> true
        else -> false
    }
}