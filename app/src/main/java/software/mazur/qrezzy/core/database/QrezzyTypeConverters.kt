package software.mazur.qrezzy.core.database

import androidx.room.TypeConverter
import software.mazur.qrezzy.data.history.local.QrHistorySource
import software.mazur.qrezzy.data.history.local.QrHistoryType

class QrezzyTypeConverters {
    @TypeConverter
    fun fromQrHistorySource(value: QrHistorySource): String {
        return value.name
    }

    @TypeConverter
    fun toQrHistorySource(value: String): QrHistorySource {
        return QrHistorySource.valueOf(value)
    }

    @TypeConverter
    fun fromQrHistoryType(value: QrHistoryType): String {
        return value.name
    }

    @TypeConverter
    fun toQrHistoryType(value: String): QrHistoryType {
        return QrHistoryType.valueOf(value)
    }
}