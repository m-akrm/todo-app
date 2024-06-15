package com.example.todo_app.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var taskName: String,
    var projectName: String,
    var startDate: String,
    var endDate: String,
    var status: String,
    var progressPercentage: Int,
    var dueTime:String,
    val description:String?=null) : Parcelable
