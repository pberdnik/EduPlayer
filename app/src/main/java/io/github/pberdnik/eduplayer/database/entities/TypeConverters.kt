package io.github.pberdnik.eduplayer.database.entities

import android.net.Uri
import androidx.room.TypeConverter
import java.util.Date


class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? = dateLong?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

}

class UriConverter {
    @TypeConverter
    fun toUri(value: String?): Uri? = if (value == null) null else Uri.parse(value)

    @TypeConverter
    fun fromUri(uri: Uri?): String? = uri.toString()
}