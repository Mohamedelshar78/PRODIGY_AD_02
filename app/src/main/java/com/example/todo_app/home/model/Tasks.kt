package com.example.todo_app.home.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity(tableName = "tasks")
data class Tasks (
    @PrimaryKey(autoGenerate = true)
 val id: Long = 0,
    var name: String,
    var description: String,
    var date: Date,
    var startTime: Date,
    var endTime: Date,
    var status: Boolean = false,
    var priority: String = "Low"
)