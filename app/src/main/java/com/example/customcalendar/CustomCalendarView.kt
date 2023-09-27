package com.example.customcalendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CustomCalendarView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs),CalendarAdapter.ItemOnClickListener {

    private val calendarData: MutableList<CalendarDay> = mutableListOf()
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var prevButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var tvMonthYear : TextView
    private val calendar = Calendar.getInstance()
    private val periodDrawable = ContextCompat.getDrawable(context,R.drawable.default_background_cell)
    private val ovulationDrawable = ContextCompat.getDrawable(context,R.drawable.ovulation_cell)
    private val fertileDrawable = ContextCompat.getDrawable(context,R.drawable.fertile_cell)

    init {
        inflate(context, R.layout.my_custom_calendar, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendarView)
        val isShowNavigation = attributes.getBoolean(R.styleable.CustomCalendarView_show_navigation, false)
        val isItemClickable = attributes.getBoolean(R.styleable.CustomCalendarView_item_clickable, false)
        recyclerView = findViewById(R.id.calendarRecyclerView)
        prevButton = findViewById(R.id.prevMonthButton)
        nextButton = findViewById(R.id.nextMonthButton)
        tvMonthYear = findViewById(R.id.tv_month_year)

        if(isShowNavigation.not()){
            prevButton.visibility = View.GONE
            nextButton.visibility = View.GONE
        }
        val dataHashMap = hashMapOf<Date,Drawable>()
        val periodsDay = arrayListOf<String>("7-9-2023","8-9-2023","9-9-2023")
        val ovulationDay = "17-9-2023"
        val fertileDays =  arrayListOf<String>("10-9-2023","11-9-2023","12-9-2023","13-9-2023","14-9-2023","15-9-2023","16-9-2023","18-9-2023","19-9-2023","20-9-2023")
        periodsDay.forEach {
            dataHashMap[parseDate(it)!!] = periodDrawable!!
        }
        dataHashMap[parseDate(ovulationDay)!!] = ovulationDrawable!!
        fertileDays.forEach {
            dataHashMap[parseDate(it)!!] = fertileDrawable!!
        }

        // Set up RecyclerView with your CalendarAdapter
        calendarAdapter = CalendarAdapter(this,isItemClickable,dataHashMap)
        recyclerView.layoutManager = GridLayoutManager(context, 7)
        recyclerView.adapter = calendarAdapter

        // Handle backward and forward button clicks
        prevButton.setOnClickListener {
            // Implement logic to navigate to the previous month
            updateCalendarData(-1)
            calendarAdapter.notifyDataSetChanged()
        }

        nextButton.setOnClickListener {
            // Implement logic to navigate to the next month
            updateCalendarData(1)
            calendarAdapter.notifyDataSetChanged()
        }

        // Initialize calendar data for the current month
        updateCalendarData(0)
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
        tvMonthYear.text = "$monthName $year"

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
            calendar.set(Calendar.YEAR, currentYear)
            calendar.set(Calendar.MONTH, currentMonth)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = calendar.time
            // Format the date in the desired format
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
            dateFormat.timeZone = TimeZone.getTimeZone("GMT+06:30")
            val formattedDate = dateFormat.format(date)
            calendarData.add(CalendarDay(dayOfMonth, formattedDate,DayType.CURRENT_MONTH , isToday = (dayOfMonth == currentDay && month == currentMonth && year == currentYear)))
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
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null // Handle parsing error, e.g., invalid date format
        }
    }

    override fun itemOnClick(calendarDay: CalendarDay) {
        Toast.makeText(context,"${calendarDay.dayNumber}",Toast.LENGTH_SHORT).show()
    }

}
