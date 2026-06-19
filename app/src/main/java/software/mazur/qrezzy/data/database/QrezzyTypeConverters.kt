package software.mazur.qrezzy.data.database

import androidx.room.TypeConverter
import software.mazur.qrezzy.domain.qr.model.QrSource
import software.mazur.qrezzy.domain.qr.model.QrType

class QrezzyTypeConverters {
    @TypeConverter
    fun fromQrHistorySource(value: QrSource): String {
        return value.name
    }

    @TypeConverter
    fun toQrHistorySource(value: String): QrSource {
        return QrSource.valueOf(value)
    }

    @TypeConverter
    fun fromQrHistoryType(value: QrType): String {
        return value.name
    }

    @TypeConverter
    fun toQrHistoryType(value: String): QrType {
        return QrType.valueOf(value)
    }
}