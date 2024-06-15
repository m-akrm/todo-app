package com.example.todo_app.dataclasses

import android.graphics.drawable.Drawable

data class Project(
                val projectName: String,
                var taskscount: Int,
                var progressPercentage: Int)