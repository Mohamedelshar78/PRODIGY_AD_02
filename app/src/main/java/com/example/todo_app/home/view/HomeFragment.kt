package com.example.todo_app.home.view

import TaskAdapter
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.home.model.TaskDatabase
import com.example.todo_app.home.model.TaskRepository
import com.example.todo_app.home.viewModel.TaskViewModel
import com.example.todo_app.home.viewModel.TaskViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private val sdf = SimpleDateFormat("MMMM dd", Locale.ENGLISH)
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private lateinit var titel:TextView
    private lateinit var numberOfTask:TextView
    private lateinit var search:EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var progressBarPresentage:TextView
    private lateinit var recyclerViewTomorow:RecyclerView
    private lateinit var recyclerViewToday:RecyclerView

    private lateinit var addTask:FloatingActionButton
    private lateinit var taskRepository:TaskRepository


    private val viewModel: TaskViewModel by viewModels{
         taskRepository = TaskRepository(TaskDatabase.getInstance(requireContext().applicationContext).taskDao())
        TaskViewModelFactory(taskRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        initializeView(view)

        return view
    }


    private fun initializeView(view: View) {
        titel = view.findViewById(R.id.titel)
        numberOfTask = view.findViewById(R.id.numbersOfTasks)
        search = view.findViewById(R.id.search_task)
        addTask = view.findViewById(R.id.addTask)
        progressBar = view.findViewById(R.id.progressBarTasks)
        recyclerViewTomorow = view.findViewById(R.id.recyclerViewTomorow)
        recyclerViewToday = view.findViewById(R.id.recyclerViewToday)
        progressBarPresentage = view.findViewById(R.id.progressBarPresentage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        onClicked()
        val dateFormat = sdf.format(currentDate.time)
        taskAdapterOthers(dateFormat)
        setTasksToday(dateFormat)
    }

    private fun onClicked() {
        addTask.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
    }

    private fun taskAdapterOthers(dateFormat: String) {
        viewModel.getAllTasks(sdf.parse(dateFormat))
        val taskAdapter = TaskAdapter(findNavController(),viewModel,sdf)
        // Observe the LiveData in the ViewModel
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasks?.let {
                taskAdapter.setTasks(it)
            }
        }
        recyclerViewTomorow.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }

    private fun setTasksToday(dateFormat: String) {

        viewModel.getTasksToday(sdf.parse(dateFormat))
        val taskTodayAdapter = TaskAdapter(findNavController(),viewModel,sdf)
        viewModel.tasksToday.observe(viewLifecycleOwner) { tasksToday ->
            taskTodayAdapter.setTasks(tasksToday)

            val tasksCompleted:Int = tasksToday.filter { it.status }.size
            titel.text = "You have got ${tasksToday.size.minus(tasksCompleted)} tasks \n today to complete"

            // Assuming tasksCompleted is an Int variable and taskTodayAdapter.itemCount is also an Int
            var totalProgress = 0
            if (tasksToday.isNotEmpty())
                totalProgress = (tasksCompleted.div( tasksToday.size )) * 100

            // Calculate the progress as a value between 0 and 100
            val progress = totalProgress.toFloat()
            if (tasksToday.isNotEmpty())
                 numberOfTask.text = "$tasksCompleted / ${tasksToday.size}"
            else
                numberOfTask.text = "0"
            // Set the progress of the ProgressBar
            progressBar.progress = progress.toInt()
            progressBarPresentage.text = (progressBar.progress.toString() + "%")

        }

        recyclerViewToday.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskTodayAdapter
        }
    }
}