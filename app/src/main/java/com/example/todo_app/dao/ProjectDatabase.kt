package com.example.todo_app.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo_app.dataclasses.Project
import com.example.todo_app.dataclasses.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Project::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "project_database")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                for (project in intializeProjects()){
                                    CoroutineScope(Dispatchers.IO).launch {
                                        database.projectDao().insert(project)
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
private fun intializeProjects(): MutableList<Project> {
    val tasks = listOf(
        Task("Task 1", "Project A", "2023-04-01", "2023-04-15", "In Progress", 50,"10:00"),
        Task("Task 2", "Project B", "2023-04-16", "2023-04-30", "Completed", 100,"10:00"),
        Task("Task 3", "Project C", "2023-05-01", "2023-05-15", "To Do", 0,"10:00")
    )
    var tasksByProject = tasks.groupBy { it.projectName }

    val temp_projects = mutableListOf<Project>()
    for ((project, tasks) in tasksByProject) temp_projects += Project(
        project,
        taskscount = tasks.size,
        tasks.sumOf {
            it.progressPercentage
        } / tasks.size)
    return temp_projects

}
