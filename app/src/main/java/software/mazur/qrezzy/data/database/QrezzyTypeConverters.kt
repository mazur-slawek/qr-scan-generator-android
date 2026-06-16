package software.mazur.qrezzy.data.database

import androidx.room.TypeConverter
import software.mazur.qrezzy.domain.history.model.QrHistorySource
import software.mazur.qrezzy.domain.history.model.QrHistoryType

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