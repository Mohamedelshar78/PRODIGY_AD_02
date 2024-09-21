package com.example.todo_app.home.view

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
    private lateinit var progressSeeAll:TextView
    private lateinit var dailySeeAll:TextView
    private lateinit var tommorowSeeAll:TextView

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
        progressSeeAll = view.findViewById(R.id.progress_see_all)
        dailySeeAll = view.findViewById(R.id.today_see_all)
        tommorowSeeAll = view.findViewById(R.id.tomorrow_see_all)
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

        // value in text view for progress bar
        progressBarPresentage.text = (progressBar.progress.toString() + "%")

    }

    private fun onClicked() {
        addTask.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
    }

    private fun taskAdapterOthers(dateFormat: String) {
        viewModel.getAllTasks(sdf.parse(dateFormat.toString()))
        val taskAdapter = TaskAdapter(findNavController(),taskRepository)
        // Observe the LiveData in the ViewModel
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasks?.let {
                taskAdapter.setTasks(it)
                taskAdapter.notifyDataSetChanged()
            }
        }
        recyclerViewTomorow.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }

    private fun setTasksToday(dateFormat: String) {

        viewModel.getTasksToday(sdf.parse(dateFormat.toString()))
        val taskTodayAdapter = TaskAdapter(findNavController(),taskRepository)
        viewModel.tasksToday.observe(viewLifecycleOwner) { tasksToday ->
            taskTodayAdapter.setTasks(tasksToday)
            taskTodayAdapter.notifyDataSetChanged()

            val tasksCompleted:Int = tasksToday.filter { it.status }.size
            titel.text = "You have got ${tasksToday.size} tasks \n today to complete"

            // Assuming tasksCompleted is an Int variable and taskTodayAdapter.itemCount is also an Int
            val totalProgress = (tasksCompleted + taskTodayAdapter.itemCount) * 100
            val progressBarMax = 100 // Maximum progress value

            // Calculate the progress as a value between 0 and 100
            val progress = (totalProgress.toFloat() / progressBarMax).coerceIn(0f, 100f)

            numberOfTask.text = "$tasksCompleted / ${taskTodayAdapter.itemCount}"
            // Set the progress of the ProgressBar
            progressBar.progress = progress.toInt()
        }

        recyclerViewToday.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskTodayAdapter
        }
    }
}