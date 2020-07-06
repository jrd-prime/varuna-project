package ru.jrd_prime.trainingdiary.utils

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class TypeConverterForRoom {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return value.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value),
                ZoneId.systemDefault()
            )
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime): Long {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

}