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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class SharedViewModel(private  val application: Application) : AndroidViewModel(application) {
    private lateinit var projectDatabase: ProjectDao
    private lateinit var taskDatabase:TaskDao

    val tasks = MutableLiveData(listOf<Task>())
    val projects=MutableLiveData<List<Project>>()
    val currenttasks = MutableLiveData(listOf<Task>())
    private val calendar=Calendar.getInstance()

    private  lateinit var tasksByProject:MutableMap<String,List<Task>>
    fun init(){
        // Initialize the databases
        projectDatabase = AppDatabase.getDatabase(application).projectDao()
        taskDatabase=TaskAppDatabase.getDatabase(application).taskDao()

        //recive data from database
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val project=async { projectDatabase.getAllProjects() }
                projects.postValue(project.await())

            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val task=async { taskDatabase.getAllTasks() }
                tasks.postValue(task.await())
                tasksByProject= task.await().groupBy { it.projectName }?.toMutableMap()?: mutableMapOf()

            }
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

        val list: MutableList<Task> =tasks.value?.toMutableList()?: mutableListOf()
        list.add(task)
        tasks.value=list

        // Add the task to the project's tasks list
        tasksByProject[task.projectName]?.let {
            var templist=it.toMutableList()
            templist.add(task)
            tasksByProject[task.projectName]=templist
        }

        return true
    }

     fun isTaskExist(task: Task): Boolean {
         return tasks.value?.find {
             it.taskName == task.taskName
         } != null
     }

    fun addProject(project: Project) {
        // Add the project to the projects list
        var list: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        list.add(project)
        projects.value=list
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                projectDatabase.insert(project)
            }
        }
        // Add the project to the tasksByProject map
        tasksByProject[project.projectName] = emptyList()

    }
    fun getprojectsname(): List<String> {
        return tasksByProject.keys.toList()
    }

    fun editProject(project: Project, enteredText: String) {
        val templist: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        templist.find { it.projectName == project.projectName }?.let {
            it.projectName=enteredText
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    projectDatabase.update(it)
                }
            }
        }
        projects.value=templist
    }

    fun deleteProject(project: Project) {
        //deleting tasks associated with the project

        for (task in tasksByProject[project.projectName].orEmpty()) {
            Log.i("test","deleting task")
            deleteTask(task)
        }


    }
    private fun _deleteProject(project: Project){
        //deleting project itself
        val templist: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        templist.remove(project)
        projects.value=templist

        //delete project from project database
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                projectDatabase.delete(project)
            }
        }
        // Remove the project from the tasksByProject map
        tasksByProject.remove(project.projectName)
    }
    fun deleteTask(task: Task) {
        //deleting task from project's tasks list
        Log.i("test","enterd the delete task function")
        val templist: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()

        Log.i("test","updating project")
        templist.find { it.projectName==task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage-=task.progressPercentage
            it.taskscount--

            if (it.taskscount == 0) {
                _deleteProject(it)
            }
            else{
                it.progressPercentage= it.progressPercentage/it.taskscount
                Log.i("test","projects postvalue")
                projects.postValue(templist)

                //update project database
                viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        projectDatabase.update(it)
                    }
                }
            }

        }



        Log.i("test","tasks postvalue")
        //deleting task itself
        val temp_tasks = tasks.value?.toMutableList() ?: mutableListOf()
        temp_tasks.remove(task)
        tasks.postValue(temp_tasks)

        //delete task from task database
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDatabase.delete(task)
            }
        }
        // Remove the task from the tasksByProject map
        tasksByProject[task.projectName]?.let {
            val templist=it.toMutableList()
            templist.remove(task)
            tasksByProject[task.projectName]=templist
        }



    }
    fun editTask(oldtask: Task, task: Task) {
        val temp_tasks = tasks.value?.toMutableList() ?: mutableListOf()
        temp_tasks.find{
            it.taskName==oldtask.taskName
        }?.let {
            it.taskName=task.taskName
            it.projectName=task.projectName
            it.startDate=task.startDate
            it.endDate=task.endDate
            it.status=task.status
        }
        tasks.value = temp_tasks
        //update task database
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDatabase.update(task)
            }
        }
        //update project
        val temp_project=projects.value?.toMutableList()?: mutableListOf()
        temp_project.find { it.projectName==oldtask.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage-=oldtask.progressPercentage
            it.taskscount--
            it.progressPercentage=if(it.taskscount==0) 0 else it.progressPercentage/it.taskscount
            //update project database
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    projectDatabase.update(it)
                }
            }
        }
        temp_project.find { it.projectName==task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage+=task.progressPercentage
            it.taskscount++
            it.progressPercentage/=(it.taskscount)

            //update project database
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    projectDatabase.update(it)
                }
            }
        }
        projects.value=temp_project

        tasksByProject[oldtask.projectName]?.let {
            val templist=it.toMutableList()
            templist.remove(oldtask)
            tasksByProject[oldtask.projectName]=templist
        }
        tasksByProject[task.projectName]?.let {
            val templist=it.toMutableList()
            templist.add(task)
            tasksByProject[task.projectName]=templist
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