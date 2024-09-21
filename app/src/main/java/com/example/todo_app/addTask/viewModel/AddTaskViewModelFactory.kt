package com.example.todo_app.addTask.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.home.model.TaskRepository

class AddTaskViewModelFactory(private val taskRepository: TaskRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTaskViewModel::class.java))
            return AddTaskViewModel(taskRepository) as T
        else
            throw IllegalArgumentException("ViewModel is not found")
    }
}