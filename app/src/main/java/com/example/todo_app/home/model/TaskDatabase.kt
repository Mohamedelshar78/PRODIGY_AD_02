package com.example.todo_app.home.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Tasks::class], version = 1)
@TypeConverters(Converters::class)
abstract class TaskDatabase :RoomDatabase(){
    abstract fun taskDao(): TaskDao

    companion object{
        @Volatile
        private var instance: TaskDatabase? = null
        fun getInstance(context: Context): TaskDatabase {
            return instance ?: synchronized(this){
                 instance ?: Room.databaseBuilder(
                     context.applicationContext
                     , TaskDatabase::class.java,
                     "task_database"
                 ).build().also { instance = it }
            }
        }
    }

}