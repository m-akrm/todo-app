package com.example.todo_app.dataclasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Task(
    var taskName: String,
    var projectName: String,
    var startDate: String,
    var endDate: String,
    var status: String,
    var progressPercentage: Int,
    var dueTime:String,
    val description:String?=null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0) : Parcelable
