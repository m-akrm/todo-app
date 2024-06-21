package com.example.todo_app

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.example.todo_app.dao.AppDatabase
import com.example.todo_app.dao.ProjectDao
import com.example.todo_app.dao.TaskAppDatabase
import com.example.todo_app.dao.TaskDao

import com.example.todo_app.dataclasses.Project
import com.example.todo_app.dataclasses.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class SharedViewModel(private  val application: Application) : AndroidViewModel(application) {
    private lateinit var projectDatabase: ProjectDao
    lateinit var taskDatabase:TaskDao

    val tasks = MutableLiveData(listOf<Task>())
    val projects=MutableLiveData<List<Project>>()
    val currenttasks = MutableLiveData(listOf<Task>())

    private lateinit var tasksByProject:MutableMap<String,List<Task>>

    fun init(){
        // Initialize the databases
        projectDatabase = AppDatabase.getDatabase(application).projectDao()
        taskDatabase=TaskAppDatabase.getDatabase(application).taskDao()

        taskDatabase.getAllTasks().observeForever{
            tasks.value=it
            tasksByProject=it.groupBy { it.projectName }.toMutableMap()
        }
        projectDatabase.getAllProjects().observeForever {
            projects.value=it
        }

    }


    fun addTask(task: Task):Boolean {
        // Check if the task already exists
        if (isTaskExist(task)) return false
        // Add the task to the tasks list
        projects.value?.find { it.projectName ==  task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage+=task.progressPercentage
            it.taskscount++
            it.progressPercentage/=(it.taskscount)

            //update task and project database
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    projectDatabase.update(it)
                    taskDatabase.insert(task)
                }
            }
        }?: return false


        return true
    }

     fun isTaskExist(task: Task): Boolean {
         return tasks.value?.find {
             it.taskName == task.taskName
         } != null
     }

    fun addProject(project: Project, task: Task) {
        project.let {
            it.taskscount=1
            it.progressPercentage=task.progressPercentage
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                projectDatabase.insert(project)
                taskDatabase.insert(task)
            }
        }


    }
    fun getprojectsname(): List<String> {
        return tasksByProject.keys.toList()
    }

    fun editProject(project: Project, enteredText: String) {

        for (task in tasksByProject[project.projectName]!!){
            task.projectName=enteredText
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    taskDatabase.update(task)
                }
            }
        }
        projects.value?.find { it.projectName == project.projectName }?.let {
            it.projectName=enteredText
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    projectDatabase.update(it)
                }
            }
        }


    }

    fun deleteProject(project: Project) {
        //deleting tasks associated with the project
        for (task in tasksByProject[project.projectName].orEmpty()) {
            Log.i("test","deleting task")
            deleteTask(task)
        }


    }
    private fun _deleteProject(project: Project){
        Log.i("_deleteProject",project.toString())

        //delete project from project database
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                projectDatabase.delete(project)
            }
        }
    }
    fun deleteTask(task: Task) {
        //deleting task from project's tasks list
        Log.i("test","enterd the delete task function")
        //delete task from task database
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDatabase.delete(task)
            }
        }

        Log.i("test","updating project")
        projects.value?.find { it.projectName==task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage-=task.progressPercentage
            it.taskscount--

            if (it.taskscount == 0) {
                _deleteProject(it)
            }
            else{
                it.progressPercentage= it.progressPercentage/it.taskscount

                //update project database
                viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        projectDatabase.update(it)
                    }
                }
            }

        }

    }
    fun editTask(oldtask: Task, task: Task) {
        Log.i("edit task","enterd the edit task function")
        Log.i("edit task",oldtask.toString())
        Log.i("edit task",task.toString())

        //update task database
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDatabase.update(task)
            }
        }
        //update project
        projects.value?.find { it.projectName==oldtask.projectName }?.let {
            Log.i("edit task",it.toString())
            it.progressPercentage*=it.taskscount
            it.progressPercentage-=oldtask.progressPercentage
            it.taskscount--

            if (it.taskscount == 0) {
                _deleteProject(it)
            }
            else{
                it.progressPercentage= it.progressPercentage/it.taskscount

                //update project database
                viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        projectDatabase.update(it)
                    }
                }
            }

        }
        projects.value?.find { it.projectName==task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            Log.i("edit task","found new project")
            it.progressPercentage+=task.progressPercentage
            Log.i("edit task","new progress is $it.progressPercentage")
            it.taskscount++
            it.progressPercentage/=(it.taskscount)

            //update project database
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    projectDatabase.update(it)
                }
            }
        }


    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }
    fun changeCurrentTask(date: Date){

        val list: MutableList<Task> =tasks.value?.toMutableList()?: mutableListOf()
        list.filter {
            convertLongToTime(it.endDate.time)==convertLongToTime(date.time)
        }.let {
            currenttasks.value=it
        }

    }


}