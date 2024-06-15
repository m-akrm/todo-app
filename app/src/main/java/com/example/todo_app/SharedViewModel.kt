package com.example.todo_app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo_app.dataclasses.Project
import com.example.todo_app.dataclasses.Task

class SharedViewModel:ViewModel() {

    val tasks = MutableLiveData(listOf(
        Task("Task 1", "Project A", "2023-04-01", "2023-04-15", "In Progress", 50,"10:00"),
        Task("Task 2", "Project B", "2023-04-16", "2023-04-30", "Completed", 100,"10:00"),
        Task("Task 3", "Project C", "2023-05-01", "2023-05-15", "To Do", 0,"10:00")
    ))
    val projects=MutableLiveData<List<Project>>()
    private  var tasksByProject = tasks.value?.groupBy { it.projectName }?: mapOf()
    fun init(){
        intializeProjects()
    }

    private fun intializeProjects() {
        val temp_projects = mutableListOf<Project>()
        for ((project, tasks) in tasksByProject) temp_projects += Project(
            project,
            taskscount = tasks.size,
            tasks.sumOf {
                it.progressPercentage
            } / tasks.size)
        projects.value = temp_projects

    }

    fun addTask(task: Task) {
        // Add the task to the tasks list
        var list: MutableList<Task> =tasks.value?.toMutableList()?: mutableListOf()
        list.add(task)
        tasks.value=list

        // Add the task to the project's tasks list
        projects.value?.find { it.projectName ==  task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage+=task.progressPercentage
            it.taskscount++
            it.progressPercentage/=(it.taskscount)
        }
    }
    fun addProject(project: Project) {
        // Add the project to the projects list
        var list: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        list.add(project)
        projects.value=list
    }
    fun getprojectsname(): List<String> {
        val temp_projects = mutableListOf<String>()
        projects.value?.let {
            for (project in it) {
                temp_projects.add(project.projectName)
            }
        }
        return temp_projects.toList()
    }

    fun editProject(project: Project, enteredText: String) {
        val templist: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        templist.find { it.projectName == project.projectName }?.projectName=enteredText
        projects.value=templist
    }

    fun deleteProject(project: Project) {
        //deleting project itself
        val templist: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        templist.remove(project)
        projects.value=templist

        //deleting tasks associated with the project
        for (task in tasksByProject[project.projectName].orEmpty()) deleteTask(task)

    }
    fun deleteTask(task: Task) {
        val temp_tasks = tasks.value?.toMutableList() ?: mutableListOf()
        temp_tasks.remove(task)
        tasks.value = temp_tasks
    }

}