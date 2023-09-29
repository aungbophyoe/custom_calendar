package com.example.customcalendar

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roomorama.caldroid.CaldroidFragment
import java.util.Calendar
import java.util.Date

class MyAdapter : ListAdapter<MonthlyData, MyAdapter.ViewHolder>(DiffUtils) {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: CustomCalendarView = itemView.findViewById(R.id.calendarView)
    }

    object DiffUtils : DiffUtil.ItemCallback<MonthlyData>(){
        override fun areItemsTheSame(oldItem: MonthlyData, newItem: MonthlyData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MonthlyData, newItem: MonthlyData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_month_for_p_tracker, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val month = getItem(position)
        month?.let { monthDate ->
            holder.container.apply {
                this.setInitialMonthFromAttribute(monthDate.monthYear)
            }
        }
    }
}