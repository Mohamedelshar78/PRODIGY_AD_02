package com.example.todo_app.home.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE date <> :date")
    suspend fun getAllTasks(date:Date):List<Tasks>

    @Insert
    suspend fun addTask(task: Tasks)

    @Update
    fun updateTask(task: Tasks)

    @Query("SELECT * FROM tasks WHERE name =:name")
    suspend fun getTask(name:String):Tasks

    @Delete
    suspend fun deleteTasks(task: Tasks)

    @Query("SELECT * FROM tasks WHERE date =:date")
    suspend fun getTasksToday(date:Date):List<Tasks>


}