package com.example.todo_app.dataclasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Task(
    var taskName: String,
    var projectName: String,
    var startDate: Date,
    var endDate: Date,
    var status: String,
    var progressPercentage: Int,
    var dueTime:String,
    val description:String?=null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0) : Parcelable

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}