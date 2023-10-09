package com.example.customcalendar

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("bindTodayTV")
    @JvmStatic
    fun bindTodayTV(textView: TextView, calendarDay: CalendarDay?){
        calendarDay?.let {
            if(it.isToday){
                textView.visibility = View.VISIBLE
            } else {
                textView.visibility = View.GONE
            }
        }
    }

    @BindingAdapter(value = ["bindDayText", "bindDrawable"], requireAll = false)
    @JvmStatic
    fun bindDayText(textView: TextView, calendarDay: CalendarDay?,dateToDrawableMap: HashMap<String, Drawable>?) {
        textView.background = null
        calendarDay?.let {
            textView.text = calendarDay.dayNumber.toString()
            if(it.isSelected){
                textView.background = ContextCompat.getDrawable(textView.context,R.drawable.click_day_cell)
                textView.setTextColor(ContextCompat.getColor(textView.context,R.color.white))
            } else {
                when (calendarDay.dayType) {
                    DayType.PREVIOUS_MONTH -> textView.setTextColor(ContextCompat.getColor(textView.context,R.color.grey))
                    DayType.NEXT_MONTH -> textView.setTextColor(ContextCompat.getColor(textView.context,R.color.grey))
                    else -> textView.setTextColor(ContextCompat.getColor(textView.context,R.color.black))
                }
                if(it.isToday){
                    textView.background = ContextCompat.getDrawable(textView.context,R.drawable.current_day_cell)
                    textView.setTextColor(ContextCompat.getColor(textView.context,R.color.green))
                }
                dateToDrawableMap?.let { hMap ->
                    val drawable = hMap[calendarDay.date]
                    if (drawable != null) {
                        textView.background = drawable
                        textView.setTextColor(ContextCompat.getColor(textView.context,R.color.white))
                    }
                }

            }
        }
    }

    @BindingAdapter("bindCheckState")
    @JvmStatic
    fun bindCheckState(imageView: ImageView, calendarDay: CalendarDay?){
        calendarDay?.let {
            if(it.isChecked){
                imageView.background = ContextCompat.getDrawable(imageView.context,R.drawable.iv_check_circle)
            } else {
                imageView.background = ContextCompat.getDrawable(imageView.context,R.drawable.iv_circle_outline)
            }
        }
    }
}