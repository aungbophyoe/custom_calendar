package com.example.customcalendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendar.databinding.MyCustomCalendarBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CustomCalendarView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs),CalendarAdapter.ItemOnClickListener {

    private val calendarData: MutableList<CalendarDay> = mutableListOf()
    private lateinit var calendarAdapter: CalendarAdapter
    private val calendar = Calendar.getInstance()
    private val periodDrawable = ContextCompat.getDrawable(context,R.drawable.default_background_cell)
    private val ovulationDrawable = ContextCompat.getDrawable(context,R.drawable.ovulation_cell)
    private val fertileDrawable = ContextCompat.getDrawable(context,R.drawable.fertile_cell)
    private lateinit var binding: MyCustomCalendarBinding

    init {
        binding = MyCustomCalendarBinding.inflate(LayoutInflater.from(context), this, true)
        binding.apply {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendarView)
            val isShowNavigation = attributes.getBoolean(R.styleable.CustomCalendarView_show_navigation, false)
            val isItemClickable = attributes.getBoolean(R.styleable.CustomCalendarView_item_clickable, false)
            val isEdit = attributes.getBoolean(R.styleable.CustomCalendarView_is_edit, false)
            val titleStyle = attributes.getResourceId(R.styleable.CustomCalendarView_title_style, com.google.android.material.R.style.TextAppearance_Material3_TitleSmall)
            val titleTextSize = attributes.getDimensionPixelSize(R.styleable.CustomCalendarView_title_text_size,24)

            // for day item
            val itemTextSize = attributes.getDimensionPixelSize(R.styleable.CustomCalendarView_item_text_size, 36)
            val itemSize = attributes.getResourceId(R.styleable.CustomCalendarView_item_size, R.dimen.default_item_size)
            val itemPadding = attributes.getResourceId(R.styleable.CustomCalendarView_item_padding, R.dimen.default_item_padding)
            val itemTextStyle = attributes.getResourceId(R.styleable.CustomCalendarView_item_text_style, com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)

            val initialMonthYear = attributes.getString(R.styleable.CustomCalendarView_initial_month)

            if(isShowNavigation.not()){
                prevMonthButton.visibility = View.GONE
                nextMonthButton.visibility = View.GONE
            }
            tvDayOfWeek0.setTextAppearance(titleStyle)
            tvDayOfWeek1.setTextAppearance(titleStyle)
            tvDayOfWeek2.setTextAppearance(titleStyle)
            tvDayOfWeek3.setTextAppearance(titleStyle)
            tvDayOfWeek4.setTextAppearance(titleStyle)
            tvDayOfWeek5.setTextAppearance(titleStyle)
            tvDayOfWeek6.setTextAppearance(titleStyle)

            tvDayOfWeek0.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize.toFloat())
            tvDayOfWeek1.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize.toFloat())
            tvDayOfWeek2.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize.toFloat())
            tvDayOfWeek3.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize.toFloat())
            tvDayOfWeek4.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize.toFloat())
            tvDayOfWeek5.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize.toFloat())
            tvDayOfWeek6.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize.toFloat())

            tvMonthYear.setTextAppearance(titleStyle)

            val dataHashMap = hashMapOf<String,Drawable>()
            val periodsDay = arrayListOf<String>("07-09-2023","08-09-2023","09-09-2023")
            val ovulationDay = "17-09-2023"
            val fertileDays =  arrayListOf<String>("10-09-2023","11-09-2023","12-09-2023","13-09-2023","14-09-2023","15-09-2023","16-09-2023","18-09-2023","19-09-2023","20-09-2023")
            periodsDay.forEach {
                dataHashMap[it] = periodDrawable!!
            }
            dataHashMap[ovulationDay] = ovulationDrawable!!
            fertileDays.forEach {
                dataHashMap[it] = fertileDrawable!!
            }
            Log.d("item - ","$titleTextSize , $itemTextSize , $itemSize , $itemPadding , $itemTextStyle")
            // Set up RecyclerView with your CalendarAdapter
            calendarAdapter = CalendarAdapter(this@CustomCalendarView,isItemClickable,dataHashMap,
                isEdit,itemTextSize,itemSize,itemPadding)
            calendarRecyclerView.layoutManager = GridLayoutManager(context, 7)
            calendarRecyclerView.adapter = calendarAdapter

            // Handle backward and forward button clicks
            prevMonthButton.setOnClickListener {
                // Implement logic to navigate to the previous month
                updateCalendarData(-1)
                calendarAdapter.notifyDataSetChanged()
            }

            nextMonthButton.setOnClickListener {
                // Implement logic to navigate to the next month
                updateCalendarData(1)
                calendarAdapter.notifyDataSetChanged()
            }

            // Initialize calendar data for the current month
            /*updateCalendarData(0)*/
            setInitialMonthFromAttribute(initialMonthYear)
        }
        /*inflate(context, R.layout.my_custom_calendar, this)*/
    }

    fun setInitialMonthFromAttribute(monthYearAttribute: String?) {
        val dateFormat = SimpleDateFormat("M-yyyy", Locale.ENGLISH)
        try {
            val date = if (!monthYearAttribute.isNullOrEmpty()) {
                dateFormat.parse(monthYearAttribute)
            } else {
                Date() // If the attribute is not specified, set it to the current date
            }
            calendar.time = date
            updateCalendarData(0) // Update the calendar data for the specified month and year
        } catch (e: ParseException) {
            // Handle parsing error if the attribute string is not in the expected format
            e.printStackTrace()
        }
    }

    // Method to update calendar data based on the month offset (0 for current month)
    private fun updateCalendarData(monthOffset: Int) {
        calendarData.clear()
        calendar.add(Calendar.MONTH, monthOffset)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        // Set the calendar to the first day of the month
        calendar.set(year, month, 1)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
        binding.tvMonthYear.text = "$monthName $year"

        // Calculate the number of days to display from the previous month
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysFromPreviousMonth = (firstDayOfWeek - Calendar.SUNDAY) % 7

        // Fill in the calendarData with day numbers and types
        for (i in 1..daysFromPreviousMonth) {
            val day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - (daysFromPreviousMonth - i)
            calendarData.add(CalendarDay(day, null,DayType.PREVIOUS_MONTH))
        }

        val currentCalendar = Calendar.getInstance()
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentYear = currentCalendar.get(Calendar.YEAR)

        for (dayOfMonth in 1..daysInMonth) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = calendar.time
            // Format the Date object to the "dd-MM-yyyy" format
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            calendarData.add(CalendarDay(dayOfMonth, formattedDate ,DayType.CURRENT_MONTH , isToday = (dayOfMonth == currentDay && month == currentMonth && year == currentYear)))
        }

        // Calculate the number of days to display from the next month
        val daysRemaining = 42 - daysFromPreviousMonth - daysInMonth
        for (i in 1..daysRemaining) {
            calendarData.add(CalendarDay(i,null, DayType.NEXT_MONTH))
        }

        // Notify the adapter that the data has changed
        calendarAdapter.submitList(calendarData)
    }

    fun parseDate(dateString: String): Date? {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null // Handle parsing error, e.g., invalid date format
        }
    }

    override fun itemOnClick(calendarDay: CalendarDay) {
        /*Toast.makeText(context,"${calendarDay.dayNumber}",Toast.LENGTH_SHORT).show()*/
    }

}
