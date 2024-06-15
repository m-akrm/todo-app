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

    fun addTask(task: Task):Boolean {
        // Add the task to the tasks list
        if(tasks.value?.find {
            it.taskName==task.taskName
        }==null) return false

        projects.value?.find { it.projectName ==  task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage+=task.progressPercentage
            it.taskscount++
            it.progressPercentage/=(it.taskscount)
        }?: return false

        val list: MutableList<Task> =tasks.value?.toMutableList()?: mutableListOf()
        list.add(task)
        tasks.value=list

        // Add the task to the project's tasks list

        return true
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
        //deleting tasks associated with the project
        for (task in tasksByProject[project.projectName].orEmpty()) deleteTask(task)

        //deleting project itself
        val templist: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        templist.remove(project)
        projects.value=templist



    }
    fun deleteTask(task: Task) {
        //deleting task from project's tasks list
        val templist: MutableList<Project> =projects.value ?.toMutableList()?: mutableListOf()
        templist.find { it.projectName==task.projectName }?.let {
            it.progressPercentage*=it.taskscount
            it.progressPercentage-=task.progressPercentage
            it.taskscount--
            it.progressPercentage=if(it.taskscount==0) 0 else it.progressPercentage/it.taskscount
        }
        projects.value=templist


        //deleting task itself
        val temp_tasks = tasks.value?.toMutableList() ?: mutableListOf()
        temp_tasks.remove(task)
        tasks.value = temp_tasks
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

    }

}