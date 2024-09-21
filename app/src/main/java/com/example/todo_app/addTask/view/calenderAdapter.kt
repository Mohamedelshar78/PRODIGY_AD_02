package com.example.todo_app.addTask.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.addTask.model.CalendarDateModel
import android.content.res.Configuration

class CalendarAdapter(private val listener: (calendarDateModel: CalendarDateModel, context: Context, position: Int) -> Unit) :
    RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {
    private val list = ArrayList<CalendarDateModel>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun inflateData(calendarDateModel: CalendarDateModel) {
            val calendarDay = itemView.findViewById<TextView>(R.id.tv_calendar_day)
            val calendarDate = itemView.findViewById<TextView>(R.id.tv_calendar_date)
            val cardView = itemView.findViewById<CardView>(R.id.card_calendar)

            if (calendarDateModel.isSelected) {
                calendarDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.purple
                    )
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.purple
                    )
                )

            } else {
                // Get the appropriate text color based on the current theme
                val textColor = when (itemView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> ContextCompat.getColor(itemView.context, R.color.white)
                    else -> ContextCompat.getColor(itemView.context, R.color.black)
                }

                // Apply the obtained color to the text views
                calendarDay.setTextColor(textColor)
                calendarDate.setTextColor(textColor)
            }

            calendarDay.text = calendarDateModel.calendarDay
            calendarDate.text = calendarDateModel.calendarDate
            cardView.setOnClickListener {
                listener.invoke(calendarDateModel, itemView.context, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_calender_date, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.inflateData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(calendarList: ArrayList<CalendarDateModel>) {
        list.clear()
        list.addAll(calendarList)
        notifyDataSetChanged()
    }
}