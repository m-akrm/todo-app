package com.example.todo_app.dataclasses

data class Task(val taskName: String,
                val projectName: String,
                val startDate: String,
                val endDate: String,
                var status: String,
                var progressPercentage: Int,
                var dueTime:String,
                val description:String?=null)
