package com.example.todo_app.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity(tableName = "tasks")
data class Tasks (
    @PrimaryKey(autoGenerate = true)
 val id: Long = 0,
    val name: String,
    val description: String,
    val date: Date,
    val startTime: Time,
    val endTime: Time,
    var status: Boolean = false,
    val priority: String = "Low"
)