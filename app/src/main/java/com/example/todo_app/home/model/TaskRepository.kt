package com.example.todo_app.home.model

import java.util.Date

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insertTask(task: Tasks){
        taskDao.addTask(task)
    }

    suspend fun getAllTasks(date:Date):List<Tasks>{
       return taskDao.getAllTasks(date)
    }

    fun updateTask(task: Tasks){
        taskDao.updateTask(task)
    }

    suspend fun getTask(name:String):Tasks{
        return taskDao.getTask(name)
    }

    suspend fun deleteTask(task: Tasks)
    {
        taskDao.deleteTasks(task)
    }

    suspend fun getTasksToday(date:Date):List<Tasks>{
        return taskDao.getTasksToday(date)
    }

}