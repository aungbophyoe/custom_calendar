package com.example.customcalendar

import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Date


class CalendarAdapter(val itemOnClickListener: ItemOnClickListener ,
                      val itemClickable : Boolean = false,
                      private val dateToDrawableMap: HashMap<String, Drawable>,
                      private val isEdit : Boolean = false,
                      private val isShowCycleCount : Boolean,
                      private val itemTextSize: Int,
                      private val itemSize:Int,
                      private val itemPadding:Int) : ListAdapter<CalendarDay, CalendarAdapter.ViewHolder>(CalendarDayDiffCallback()) {

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
        private val isCheck : ImageView = itemView.findViewById(R.id.ivCheck)
        private val tvToday : TextView = itemView.findViewById(R.id.tvToday)
        private val tvCycleCount : TextView = itemView.findViewById(R.id.tvCycleCount)

        fun bind(calendarDay: CalendarDay) {
            val layoutParams = dayTextView.layoutParams
            layoutParams.width = itemView.context.resources.getDimensionPixelSize(itemSize)
            layoutParams.height = itemView.context.resources.getDimensionPixelSize(itemSize)
            dayTextView.layoutParams = layoutParams

            val paddingInPixels = itemView.context.resources.getDimensionPixelSize(itemPadding)
            dayTextView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

            dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,itemTextSize.toFloat())

            val dayNumberText = calendarDay.dayNumber.toString()
            dayTextView.text = dayNumberText
            tvToday.visibility = View.GONE
            if (isEdit){
                isCheck.visibility = View.VISIBLE
                dayTextView.background = null
                when (calendarDay.dayType) {
                    DayType.PREVIOUS_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                    DayType.NEXT_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                    else -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
                }
                if(calendarDay.isToday){
                    dayTextView.background = ContextCompat.getDrawable(itemView.context,R.drawable.current_day_cell)
                    dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.green))
                    tvToday.visibility = View.VISIBLE
                }
                if (calendarDay.isChecked){
                    isCheck.background = ContextCompat.getDrawable(itemView.context,R.drawable.iv_check_circle)
                } else {
                    isCheck.background = ContextCompat.getDrawable(itemView.context,R.drawable.iv_circle_outline)
                }
            } else {
                if(isShowCycleCount){
                    tvCycleCount.text = "$position"
                   tvCycleCount.visibility = View.VISIBLE
                }
                isCheck.visibility = View.GONE
                if(calendarDay.isSelected){
                    dayTextView.background = ContextCompat.getDrawable(itemView.context,R.drawable.click_day_cell)
                    dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                }else {
                    dayTextView.background = null
                    when (calendarDay.dayType) {
                        DayType.PREVIOUS_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                        DayType.NEXT_MONTH -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.grey))
                        else -> dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
                    }
                    if(calendarDay.isToday){
                        dayTextView.background = ContextCompat.getDrawable(itemView.context,R.drawable.current_day_cell)
                        dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.green))
                        tvToday.visibility = View.VISIBLE
                    }

                    val drawable = dateToDrawableMap[calendarDay.date]
                    if (drawable != null) {
                        dayTextView.background = drawable
                        dayTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                    }
                }
            }

            if(itemClickable){
                itemView.setOnClickListener {
                    if(calendarDay.dayType == DayType.CURRENT_MONTH) {
                        if(isEdit){
                            calendarDay.isChecked = calendarDay.isChecked.not()
                            notifyItemChanged(bindingAdapterPosition)
                        } else {
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
}

class CalendarDayDiffCallback : DiffUtil.ItemCallback<CalendarDay>() {
    override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem == newItem
    }
}
