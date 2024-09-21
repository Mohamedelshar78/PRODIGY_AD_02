package com.example.todo_app.home.model
import androidx.room.TypeConverter
import java.sql.Time
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class Converters {


    var dateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd")

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        try {
            return if (dateString == null) null else dateFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return if (date == null) null else dateFormat.format(date)
    }

    @TypeConverter
    fun toTime(timeString: String?): Time? {
        return if (timeString == null) null else Time.valueOf(timeString)
    }

    @TypeConverter
    fun fromTime(time: Time?): String? {
        return time?.toString()
    }
}