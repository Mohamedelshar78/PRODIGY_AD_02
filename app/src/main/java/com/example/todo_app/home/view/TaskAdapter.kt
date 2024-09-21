import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.home.model.TaskRepository
import com.example.todo_app.home.model.Tasks
import com.example.todo_app.home.view.HomeFragmentDirections
import com.example.todo_app.home.viewModel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(private val navController: NavController, private val taskViewModel: TaskViewModel,sdf: SimpleDateFormat) :
    RecyclerView.Adapter<TaskAdapter.MyHolder>() {

    private var taskList: List<Tasks> = emptyList()
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val dateFormat = sdf.format(currentDate.time)


    fun setTasks(tasks: List<Tasks>) {
        taskList = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    private val sdf = SimpleDateFormat("MMMM dd", Locale.ENGLISH)

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.taskName.text = taskList[position].name
        holder.taskDate.text = sdf.format(taskList[position].date)
        when (taskList[position].priority) {
            "high" -> holder.taskPriority.setBackgroundResource(R.drawable.edite_button_high)
            "medium" -> holder.taskPriority.setBackgroundResource(R.drawable.edite_button_meduim)
            "low" -> holder.taskPriority.setBackgroundResource(R.drawable.edite_button_low)
        }

        if (taskList[position].status) {
            holder.taskStatus.setBackgroundResource(R.drawable.ic_cheked_foreground)
        }

        holder.taskStatus.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (!taskList[position].status) {
                    taskList[position].status = true
                    holder.taskStatus.setBackgroundResource(R.drawable.ic_cheked_foreground)
                    taskViewModel.updateTasks(taskList[position],sdf.parse(dateFormat))
                } else {
                    taskList[position].status = false
                    holder.taskStatus.setBackgroundResource(R.drawable.custom_checkbox_background)
                    taskViewModel.updateTasks(taskList[position],sdf.parse(dateFormat))
                    notifyDataSetChanged()
                }
            }
        }

        holder.itemView.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToEditeTaskFragment((taskList[position].name))
            navController.navigate(action)
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.taskname)
        val taskDate: TextView = itemView.findViewById(R.id.task_date)
        val taskPriority: View = itemView.findViewById(R.id.color_priority)
        val taskStatus: CheckBox = itemView.findViewById(R.id.task_finished)
    }
}