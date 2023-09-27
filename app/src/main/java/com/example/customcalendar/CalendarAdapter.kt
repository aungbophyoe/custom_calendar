package com.example.customcalendar

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Date


class CalendarAdapter(val itemOnClickListener: ItemOnClickListener , val itemClickable : Boolean = false,private val dateToDrawableMap: HashMap<Date, Drawable>) : ListAdapter<CalendarDay, CalendarAdapter.ViewHolder>(CalendarDayDiffCallback()) {

    interface ItemOnClickListener {
        fun itemOnClick(calendarDay: CalendarDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calendarDay = getItem(position)
        holder.bind(calendarDay)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.tvDay)
        private val layoutDay: ConstraintLayout = itemView.findViewById(R.id.layoutDay)

        fun bind(calendarDay: CalendarDay) {
            val dayNumberText = calendarDay.dayNumber.toString()

            dayTextView.text = dayNumberText
            // You can customize this method to display additional data or handle item clicks.
           /* when (calendarDay.dayType) {
                DayType.PREVIOUS_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                DayType.NEXT_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                else -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
            }*/

            if(calendarDay.isSelected){
                dayTextView.background = ContextCompat.getDrawable(itemView.context,R.drawable.click_day_cell)
                dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
            }else {
                if(calendarDay.isToday){
                    layoutDay.background = ContextCompat.getDrawable(itemView.context,R.drawable.current_day_cell)
                }
                dayTextView.background = null
                when (calendarDay.dayType) {
                    DayType.PREVIOUS_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                    DayType.NEXT_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                    else -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
                }
            }

            val drawable = dateToDrawableMap[calendarDay.date]
            if (drawable != null) {
                dayTextView.background = drawable
                dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
            } else {
                dayTextView.background = null
            }

            if(itemClickable){
                itemView.setOnClickListener {
                    if(calendarDay.dayType == DayType.CURRENT_MONTH) {
                        calendarDay.isSelected = true
                        notifyItemChanged(bindingAdapterPosition)
                        itemOnClickListener.itemOnClick(calendarDay)
                        for (i in 0 until itemCount) {
                            if (i != bindingAdapterPosition) {
                                val otherDay = getItem(i)
                                otherDay.isSelected = false
                                notifyItemChanged(i)
                            }
                        }
                    }
                }
            }
        }
    }
}

class CalendarDayDiffCallback : DiffUtil.ItemCallback<CalendarDay>() {
    override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem.dayNumber == newItem.dayNumber
    }

    override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem == newItem
    }
}
