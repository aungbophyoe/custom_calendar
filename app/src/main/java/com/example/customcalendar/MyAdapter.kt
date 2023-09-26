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
        val container: FrameLayout = itemView.findViewById(R.id.calendarView)
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

            val calendar = Calendar.getInstance()
            val caldroidFragment = CaldroidFragment().apply {
                arguments = Bundle().apply {
                    putInt(CaldroidFragment.MONTH, monthDate.month)
                    putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR))
                    putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false)
                    putBoolean(CaldroidFragment.ENABLE_SWIPE,false)
                    putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS,false)
                    putInt(CaldroidFragment.THEME_RESOURCE,R.style.CaldroidCustom)
                }
            }
            val dayArr = listOf<Int>(7,8,9)
            val fertileDayArr = listOf<Int>(10,11,12,13,14,15,16,18,19,20)
            val ovulationDayArr = listOf<Int>(17)
            val selectionBackground = hashMapOf<Date, Drawable?>()
            val selectionTextColor = hashMapOf<Date,Int>()
            dayArr.forEach {
                val date = getDate(2023,monthDate.month,it)
                selectionBackground[date] = ContextCompat.getDrawable(holder.container.context,R.drawable.default_background_cell)
                selectionTextColor[date] = R.color.white
            }
            fertileDayArr.forEach {
                val date = getDate(2023,monthDate.month,it)
                selectionBackground[date] = ContextCompat.getDrawable(holder.container.context,R.drawable.fertile_cell)
                selectionTextColor[date] = R.color.primary_green_blue
            }
            ovulationDayArr.forEach {
                val date = getDate(2023,monthDate.month,it)
                selectionBackground[date] = ContextCompat.getDrawable(holder.container.context,R.drawable.ovulation_cell)
                selectionTextColor[date] = R.color.white
            }
            caldroidFragment.setTextColorForDates(selectionTextColor)
            caldroidFragment.setBackgroundDrawableForDates(selectionBackground)

            // You can add fragments dynamically here
            val fragmentManger = (holder.container.context as AppCompatActivity)
                .supportFragmentManager
           /* val containerId = holder.container.id // Get container id
            // Delete old fragment
            val oldFragment = fragmentManger.findFragmentById(containerId)
            if (oldFragment != null) {
                fragmentManger.beginTransaction().remove(oldFragment).commit()
            }
            // Generate a unique container id
            val newContainerId = View.generateViewId()
            // Set the new container id
            holder.container.id = newContainerId*/
            // Add new fragment
            fragmentManger.beginTransaction().add(holder.container.id, caldroidFragment).commit()
        }
    }

    fun getDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // Month is zero-based, so we subtract 1
        return calendar.time
    }
}