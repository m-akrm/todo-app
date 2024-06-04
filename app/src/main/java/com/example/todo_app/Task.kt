package com.example.todo_app

import android.graphics.drawable.Drawable

data class Task(val taskName: String,
                val projectName: String,
                val startDate: String,
                val endDate: String,
                var status: String,
                var progressPercentage: Int,
                val description:String?=null)
