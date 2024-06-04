package com.example.todo_app

import android.graphics.drawable.Drawable

data class Project(
                val projectName: String,
                var taskscount: Int,
                var progressPercentage: Int,
                var status: String="To do",
                var icon: Drawable? = null)