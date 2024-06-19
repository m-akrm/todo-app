package com.example.todo_app.dao

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo_app.dataclasses.Converters
import com.example.todo_app.dataclasses.Project
import com.example.todo_app.dataclasses.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskAppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskAppDatabase? = null

        fun getDatabase(context: Context): TaskAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskAppDatabase::class.java,
                    "task_database")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->

                                val calender = Calendar.getInstance()
                                val startdate=calender.time
                                calender.add(Calendar.DAY_OF_MONTH,5)
                                val enddate=calender.time

                                val tasks = listOf(
                                    Task("Task 1", "Project A", startdate,enddate, "In Progress", 50,"10:00"),
                                    Task("Task 2", "Project B", startdate,enddate, "Completed", 100,"10:00"),
                                    Task("Task 3", "Project C", startdate,enddate, "To Do", 0,"10:00")
                                )
                                for (task in tasks){
                                    CoroutineScope(Dispatchers.IO).launch {
                                        database.taskDao().insert(task)
                                    }
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}