package software.mazur.qrezzy.data.database

import androidx.room.TypeConverter
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType
import software.mazur.qrezzy.domain.qr.model.style.QrErrorCorrection
import software.mazur.qrezzy.domain.qr.model.style.QrPatternStyle

class QrezzyTypeConverters {
    @TypeConverter
    fun fromQrSource(value: QrSource): String = value.name

    @TypeConverter
    fun toQrSource(value: String): QrSource = QrSource.valueOf(value)

    @TypeConverter
    fun fromQrType(value: QrType): String = value.name

    @TypeConverter
    fun toQrType(value: String): QrType = QrType.valueOf(value)

    @TypeConverter
    fun fromQrPatternStyle(value: QrPatternStyle): String = value.name

    @TypeConverter
    fun toQrPatternStyle(value: String): QrPatternStyle = QrPatternStyle.valueOf(value)

    @TypeConverter
    fun fromQrErrorCorrection(value: QrErrorCorrection): String = value.name

    @TypeConverter
    fun toQrErrorCorrection(value: String): QrErrorCorrection = QrErrorCorrection.valueOf(value)
}
