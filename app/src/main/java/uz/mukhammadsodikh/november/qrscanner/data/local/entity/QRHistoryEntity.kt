package uz.mukhammadsodikh.november.qrscanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCode
import uz.mukhammadsodikh.november.qrscanner.domain.model.QRCodeType
import uz.mukhammadsodikh.november.qrscanner.domain.parser.QRCodeParser
import java.util.Date

/**
 * Room Database Entity - QR History
 */
@Entity(tableName = "qr_history")
data class QRHistoryEntity(
    @PrimaryKey
    val id: String,
    val rawContent: String,
    val type: String, // QRCodeType ni String sifatida saqlaymiz
    val format: String, // QR_CODE, EAN_13, etc.
    val scannedAt: Long,
    val isFavorite: Boolean = false
) {
    /**
     * Entity -> Domain Model
     */
    fun toQRCode(): QRCode {
        val qrCodeType = QRCodeParser.parse(rawContent, format)
        return QRCode(
            id = id,
            rawContent = rawContent,
            type = qrCodeType,
            scannedAt = Date(scannedAt),
            isFavorite = isFavorite
        )
    }

    companion object {
        /**
         * Domain Model -> Entity
         */
        fun fromQRCode(qrCode: QRCode, format: String = "QR_CODE"): QRHistoryEntity {
            return QRHistoryEntity(
                id = qrCode.id,
                rawContent = qrCode.rawContent,
                type = qrCode.getTitle(),
                format = format,
                scannedAt = qrCode.scannedAt.time,
                isFavorite = qrCode.isFavorite
            )
        }
    }
}