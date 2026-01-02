package uz.mukhammadsodikh.november.qrscanner.domain.parser

import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCodeType

/**
 * QR Code contentini parse qilish
 */
object QRCodeParser {

    fun parse(rawContent: String, format: String = "QR_CODE"): QRCodeType {
        return when {
            // URL
            isUrl(rawContent) -> QRCodeType.Url(rawContent)

            // Email
            isEmail(rawContent) -> parseEmail(rawContent)

            // Phone
            isPhone(rawContent) -> QRCodeType.Phone(cleanPhone(rawContent))

            // SMS
            isSMS(rawContent) -> parseSMS(rawContent)

            // WiFi
            isWiFi(rawContent) -> parseWiFi(rawContent)

            // VCard
            isVCard(rawContent) -> parseVCard(rawContent)

            // Location (geo:)
            isLocation(rawContent) -> parseLocation(rawContent)

            // Barcode (agar QR_CODE emas)
            format != "QR_CODE" -> QRCodeType.Barcode(rawContent, format)

            // Oddiy text
            else -> QRCodeType.Text(rawContent)
        }
    }

    // URL checker
    private fun isUrl(content: String): Boolean {
        val urlPattern = Regex("^(https?://|www\\.).*", RegexOption.IGNORE_CASE)
        return urlPattern.matches(content)
    }

    // Email checker
    private fun isEmail(content: String): Boolean {
        return content.startsWith("mailto:", ignoreCase = true) ||
                content.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }

    private fun parseEmail(content: String): QRCodeType {
        if (content.startsWith("mailto:", ignoreCase = true)) {
            val parts = content.substringAfter("mailto:").split("?")
            val email = parts[0]
            var subject: String? = null
            var body: String? = null

            if (parts.size > 1) {
                val params = parts[1].split("&")
                params.forEach { param ->
                    val (key, value) = param.split("=")
                    when (key.lowercase()) {
                        "subject" -> subject = value
                        "body" -> body = value
                    }
                }
            }
            return QRCodeType.Email(email, subject, body)
        }
        return QRCodeType.Email(content)
    }

    // Phone checker
    private fun isPhone(content: String): Boolean {
        return content.startsWith("tel:", ignoreCase = true) ||
                content.matches(Regex("^[+]?[0-9\\s\\-()]+$"))
    }

    private fun cleanPhone(content: String): String {
        return content.removePrefix("tel:").removePrefix("TEL:")
    }

    // SMS checker
    private fun isSMS(content: String): Boolean {
        return content.startsWith("sms:", ignoreCase = true) ||
                content.startsWith("smsto:", ignoreCase = true)
    }

    private fun parseSMS(content: String): QRCodeType {
        val cleanContent = content.removePrefix("sms:").removePrefix("smsto:")
        val parts = cleanContent.split("?", ":")
        val number = parts[0]
        val message = if (parts.size > 1) parts[1].substringAfter("body=") else null
        return QRCodeType.SMS(number, message)
    }

    // WiFi checker
    private fun isWiFi(content: String): Boolean {
        return content.startsWith("WIFI:", ignoreCase = true)
    }

    private fun parseWiFi(content: String): QRCodeType {
        val params = content.substringAfter("WIFI:").split(";")
        var ssid = ""
        var password = ""
        var security = "WPA"

        params.forEach { param ->
            if (param.contains(":")) {
                val parts = param.split(":", limit = 2)
                val key = parts[0].trim()
                val value = parts.getOrNull(1)?.trim() ?: ""

                when (key.uppercase()) {
                    "S" -> ssid = value.removeSurrounding("\"")
                    "P" -> password = value.removeSurrounding("\"")
                    "T" -> security = value.uppercase()
                }
            }
        }

        // Security type fix
        if (security.isEmpty() || security == "NOPASS") {
            security = "OPEN"
        }

        return QRCodeType.WiFi(ssid, password, security)
    }

    // VCard checker
    private fun isVCard(content: String): Boolean {
        return content.startsWith("BEGIN:VCARD", ignoreCase = true)
    }

    private fun parseVCard(content: String): QRCodeType {
        var name = ""
        var phone: String? = null
        var email: String? = null
        var org: String? = null

        content.lines().forEach { line ->
            when {
                line.startsWith("FN:", ignoreCase = true) -> name = line.substringAfter(":")
                line.startsWith("TEL:", ignoreCase = true) -> phone = line.substringAfter(":")
                line.startsWith("EMAIL:", ignoreCase = true) -> email = line.substringAfter(":")
                line.startsWith("ORG:", ignoreCase = true) -> org = line.substringAfter(":")
            }
        }
        return QRCodeType.VCard(name, phone, email, org)
    }

    // Location checker
    private fun isLocation(content: String): Boolean {
        return content.startsWith("geo:", ignoreCase = true)
    }

    private fun parseLocation(content: String): QRCodeType {
        val coords = content.substringAfter("geo:").split(",")
        val lat = coords.getOrNull(0)?.toDoubleOrNull() ?: 0.0
        val lng = coords.getOrNull(1)?.toDoubleOrNull() ?: 0.0
        return QRCodeType.Location(lat, lng)
    }
}