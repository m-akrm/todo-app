package com.example.todo_app.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Project(
    var projectName: String,
    var taskscount: Int,
    var progressPercentage: Int) : Parcelable