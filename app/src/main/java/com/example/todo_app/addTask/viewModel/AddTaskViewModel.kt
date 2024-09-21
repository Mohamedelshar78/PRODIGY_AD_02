package com.example.todo_app.addTask.viewModel

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import android.widget.TimePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.R
import com.example.todo_app.home.model.TaskRepository
import com.example.todo_app.home.model.Tasks
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class AddTaskViewModel(private val taskRepository: TaskRepository):ViewModel() {

    private val _taskLiveData = MutableLiveData<Tasks>()
    val task: LiveData<Tasks> get() = _taskLiveData


    fun addTask(tasks: Tasks) {
        viewModelScope.launch {
            taskRepository.insertTask(tasks)
        }
    }


    fun editeTask(tasks: Tasks){
        viewModelScope.launch {
            taskRepository.updateTask(tasks)
        }
    }

    fun getTask(name:String){
        viewModelScope.launch {
           _taskLiveData.postValue( taskRepository.getTask(name))
        }
    }

    fun deleteTask(tasks: Tasks){
        viewModelScope.launch {
            taskRepository.deleteTask(tasks)
        }
    }

    fun showTimePickerDialog(view: TextView , context: Context) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context,
            R.style.CustomTimePickerDialog,  // Applying custom style here
            TimePickerDialog.OnTimeSetListener { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val hourOfDay = if (selectedHour > 12) selectedHour - 12 else selectedHour
                val amPm = if (selectedHour >= 12) "PM" else "AM"
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d ", hourOfDay, selectedMinute, amPm)
                view.text = selectedTime
            }, hour, minute, false)

        timePickerDialog.show()
    }

}