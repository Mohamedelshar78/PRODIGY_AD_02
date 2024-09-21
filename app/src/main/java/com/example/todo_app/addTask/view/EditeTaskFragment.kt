package com.example.todo_app.addTask.view

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.todo_app.R
import com.example.todo_app.addTask.model.CalendarDateModel
import com.example.todo_app.addTask.model.HorizontalItemDecoration
import com.example.todo_app.addTask.viewModel.AddTaskViewModel
import com.example.todo_app.addTask.viewModel.AddTaskViewModelFactory
import com.example.todo_app.home.model.TaskDatabase
import com.example.todo_app.home.model.TaskRepository
import com.example.todo_app.home.model.Tasks
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EditeTaskFragment : Fragment() {

    private val sdf = SimpleDateFormat("MMMM dd", Locale.ENGLISH)
    private val timeFormat = SimpleDateFormat("hh:mm a",Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private val calendarList2 = ArrayList<CalendarDateModel>()

    private lateinit var adapter: CalendarAdapter
    private lateinit var back: ImageView
    private lateinit var calendarNext: ImageView
    private lateinit var calendarPrevious: ImageView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var dateMonth: TextView
    private lateinit var editeTaskButton: Button
    private lateinit var removeTask: Button
    private lateinit var priorityTaskHigh: Button
    private lateinit var priorityTaskMeduim: Button
    private lateinit var priorityTaskLow: Button
    private lateinit var taskName: EditText
    private lateinit var taskDescription: EditText
    private lateinit var startTime:TextView
    private lateinit var endTime:TextView
    private lateinit var priority:String
    private lateinit var date:String


    private val args: EditeTaskFragmentArgs by navArgs()

    private val viewModel: AddTaskViewModel by viewModels{
        AddTaskViewModelFactory(TaskRepository(TaskDatabase.getInstance(requireContext().applicationContext).taskDao()))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edite_task, container, false)
        initializeView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpClickListener()
        setUpCalendar()
        getTask(args.taskName)
    }

    private fun initializeView(view: View) {
        back = view.findViewById(R.id.back)
        calendarPrevious = view.findViewById(R.id.back_year)
        calendarNext = view.findViewById(R.id.next_year)
        calendarRecyclerView = view.findViewById(R.id.recyclerView)
        dateMonth = view.findViewById(R.id.tv_date_month)

        editeTaskButton = view.findViewById(R.id.editeTask)
        removeTask = view.findViewById(R.id.removeTask)
        startTime = view.findViewById(R.id.startTime)
        endTime = view.findViewById(R.id.endTime)
        priorityTaskLow = view.findViewById(R.id.priorityLow)
        priorityTaskHigh = view.findViewById(R.id.priorityHigh)
        priorityTaskMeduim = view.findViewById(R.id.priorityMedium)

        taskName = view.findViewById(R.id.addTaskName)
        taskDescription = view.findViewById(R.id.addTaskDescreption)
    }

    private fun setUpClickListener() {

        back.setOnClickListener {
            findNavController().navigate(R.id.action_editeTaskFragment_to_homeFragment)
        }

        calendarNext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar()
        }

        calendarPrevious.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            setUpCalendar()
        }
        editeTaskButton.setOnClickListener {
            editeTasks()
            findNavController().navigate(R.id.action_editeTaskFragment_to_homeFragment)
        }

        priorityTaskLow.setOnClickListener {
            priority = "low"
            priorityTaskLow.setBackgroundResource(R.drawable.edite_button_low)
            priorityTaskMeduim.setBackgroundResource(R.drawable.button_meduim)
            priorityTaskHigh.setBackgroundResource(R.drawable.button_high)

        }

        priorityTaskMeduim.setOnClickListener {
            priorityTaskMeduim.setBackgroundResource(R.drawable.edite_button_meduim)
            priorityTaskHigh.setBackgroundResource(R.drawable.button_high)
            priorityTaskLow.setBackgroundResource(R.drawable.button_low)
            priority = "medium"
        }
        priorityTaskHigh.setOnClickListener {
            priorityTaskHigh.setBackgroundResource(R.drawable.edite_button_high)
            priorityTaskLow.setBackgroundResource(R.drawable.button_low)
            priorityTaskMeduim.setBackgroundResource(R.drawable.button_meduim)

            priority = "high"
        }

        startTime.setOnClickListener {
           viewModel.showTimePickerDialog(startTime,requireContext())
        }

        endTime.setOnClickListener {
            viewModel.showTimePickerDialog(endTime,requireContext())
        }

        removeTask.setOnClickListener {
            deleteTask(args.taskName)
            findNavController().navigate(R.id.action_editeTaskFragment_to_homeFragment)
        }

    }

    private fun setUpAdapter() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.single_calendar_margin)
        calendarRecyclerView.addItemDecoration(HorizontalItemDecoration(spacingInPixels))

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(calendarRecyclerView)

        adapter = CalendarAdapter { selectedDate: CalendarDateModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }
            adapter.setData(calendarList2)

            // Extract the month and year from the selected date
            val selectedCalendar = selectedDate.data
            val selectedMonthYear = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(selectedCalendar.time)
            date = selectedMonthYear

            dateMonth.text = selectedMonthYear.toString()

            // Now you can use selectedMonthYear as needed
            Log.d("SelectedDate", "Selected Date: $selectedMonthYear")
        }
        calendarRecyclerView.adapter = adapter
    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        dateMonth.text = sdf.format(cal.time)

        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendarList2.clear()
        calendarList2.addAll(calendarList)
        adapter.setData(calendarList)
    }

    private fun editeTasks(){

        // Define date and time formats
        val timeFormat = SimpleDateFormat("hh:mm ",Locale.ENGLISH)
        val startTimeTask = Time(timeFormat.parse(startTime.text.toString()).time)
        val endTimeTask = Time(timeFormat.parse(endTime.text.toString()).time)
        // Create a Tasks object with the converted date and time
        val task = Tasks(
            name = taskName.text.toString(),
            date = sdf.parse(dateMonth.text.toString()),
            startTime = startTimeTask,
            endTime = endTimeTask,
            priority = priority,
            description = taskDescription.text.toString()
        )

        viewModel.editeTask(task)
        Toast.makeText(requireContext(),"Task edited successfully !!",Toast.LENGTH_LONG).show()
    }

    private fun getTask(name:String){
        viewModel.getTask(name)
        viewModel.task.observe(viewLifecycleOwner){ task ->
            setTaskData(task)
        }
    }

    private fun setTaskData(tasks: Tasks){
        setPriority(tasks.priority)
        dateMonth.text = sdf.format(tasks.date)
        taskName.setText(tasks.name)
        taskDescription.setText(tasks.description)
        startTime.text = timeFormat.format(tasks.startTime)
        endTime.text = timeFormat.format(tasks.endTime)
        priority = tasks.priority
    }

    private fun deleteTask(taskName:String){
        viewModel.getTask(taskName)
        viewModel.task.observe(viewLifecycleOwner){ task ->
            viewModel.deleteTask(task)
            Toast.makeText(requireContext(),"Task deleted successfully !!",Toast.LENGTH_LONG).show()

        }
    }

    private fun setPriority(priority:String){
        when(priority){
            "high" -> priorityTaskHigh.setBackgroundResource(R.drawable.edite_button_high)
            "medium" -> priorityTaskMeduim.setBackgroundResource(R.drawable.edite_button_meduim)
            "low" -> priorityTaskLow.setBackgroundResource(R.drawable.edite_button_low)
        }
    }
}