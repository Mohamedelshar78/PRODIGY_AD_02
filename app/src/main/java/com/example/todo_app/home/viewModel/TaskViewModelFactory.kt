package com.example.todo_app.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.home.model.TaskRepository

class TaskViewModelFactory(private val taskRepository: TaskRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java))
            return TaskViewModel(taskRepository) as T
        else
            throw IllegalArgumentException("ViewModel is not found")
    }
}