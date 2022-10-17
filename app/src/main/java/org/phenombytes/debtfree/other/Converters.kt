package org.phenombytes.debtfree.other

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value:Long?):Date? = value?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?):Long? = date?.time?.toLong()
}