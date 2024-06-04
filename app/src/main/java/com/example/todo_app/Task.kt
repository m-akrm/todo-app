package com.example.todo_app

import android.graphics.drawable.Drawable

data class Task(val taskName: String,
                val projectName: String,
                val startDate: String,
                val endDate: String,
                val status: String,
                val progressPercentage: Int,
                var icon: Drawable? = null,
                val description:String?=null)
