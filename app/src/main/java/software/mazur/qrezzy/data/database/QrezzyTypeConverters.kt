package software.mazur.qrezzy.data.database

import androidx.room.TypeConverter
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType

class QrezzyTypeConverters {
    @TypeConverter
    fun fromQrSource(value: QrSource): String = value.name

    @TypeConverter
    fun toQrSource(value: String): QrSource = QrSource.valueOf(value)

    @TypeConverter
    fun fromQrType(value: QrType): String = value.name

    @TypeConverter
    fun toQrType(value: String): QrType = QrType.valueOf(value)
}
