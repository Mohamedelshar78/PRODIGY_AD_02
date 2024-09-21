package com.example.todo_app.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.home.model.TaskRepository
import com.example.todo_app.home.model.Tasks
import kotlinx.coroutines.launch
import java.util.Date

class TaskViewModel(private val taskRepository: TaskRepository):ViewModel() {

    private val _tasksLiveData = MutableLiveData<List<Tasks>>()
    val tasks: LiveData<List<Tasks>> get() = _tasksLiveData


    private val _tasksTodayLiveData = MutableLiveData<List<Tasks>>()
    val tasksToday: LiveData<List<Tasks>> get() = _tasksTodayLiveData


    fun getAllTasks(date: Date) {
        viewModelScope.launch {
            val tasksList = taskRepository.getAllTasks(date)
            _tasksLiveData.value = tasksList
            Log.d("main", tasksList.toString())
        }
    }

    fun getTasksToday(date: Date){
        viewModelScope.launch {
            val tasksList = taskRepository.getTasksToday(date)
            _tasksTodayLiveData.value = tasksList
            Log.d("main", tasksList.toString())
        }
    }


}