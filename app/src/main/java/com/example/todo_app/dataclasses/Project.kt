package com.example.todo_app.dataclasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
data class Project(
    var projectName: String,
    var taskscount: Int,
    var progressPercentage: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0) : Parcelable