package com.example.customcalendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Calendar


/**
 * This class represents the calendar that will be visible on the screen.
 *
 * @author Naishadh Parmar
 * @version 1.0
 * @since 2017-07-14
 */
class CustomCalendar(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    private val MONTHS = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    private var context: Context? = null
    private var view: View? = null
    private var butLeft: ImageButton? = null
    private var butRight: ImageButton? = null

    /**
     * Returns the TextView that shows the month and the year.
     * @return The TextView that shows the month and the year.
     */
    var monthYearTextView: TextView? = null
    private var tvDaysOfWeek: Array<TextView?>? = null
    private var llWeeks: LinearLayout? = null

    /**
     * Returns an array of all the date views.
     * @return An array of all the date views. Does not include the disabled previous month and next month views shown for continuity.
     */
    var allViews: Array<View?>? = null

    /**
     * Returns a Calendar representation of the selected date.
     * @return A Calendar representation of the selected date.
     */
    private var selectedDate: Calendar? = null
    private var listener: OnDateSelectedListener? = null
    private var leftButtonListener: OnNavigationButtonClickedListener? = null
    private var rightButtonListener: OnNavigationButtonClickedListener? = null
    private var rowHeight = 0f
    private var selectedButton: View? = null
    private var startFrom = -1
    private var monthYearFormat = -1
    private var dayOfWeekLength = -1
    private var draLeftButton: Drawable? = null
    private var draRightButton: Drawable? = null
    private var mapDateToTag: Map<Int, Any?>? = null
    private var mapDateToDesc: Map<Int, Any?>? = null
    private var mapDescToProp: Map<Any?, Property?>? = null

    /**
     * Constructor that is called when inflating from XML.
     * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    init {
        this.context = context
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendar)
        startFrom = attributes.getInt(R.styleable.CustomCalendar_day_of_week_start_from, 0)
        monthYearFormat = attributes.getInt(R.styleable.CustomCalendar_month_year_format, 0)
        dayOfWeekLength = attributes.getInt(R.styleable.CustomCalendar_day_of_week_length, 1)
        draLeftButton = attributes.getDrawable(R.styleable.CustomCalendar_left_button_src)
        draRightButton = attributes.getDrawable(R.styleable.CustomCalendar_right_button_src)
        rowHeight = attributes.getDimension(R.styleable.CustomCalendar_row_height, 0.0f)
        initialize()
    }

    private fun initialize() {
        view = inflate(context, R.layout.customcalendar, this)
        butLeft = findViewById<View>(R.id.but_left) as ImageButton
        butRight = findViewById<View>(R.id.but_right) as ImageButton
        if (draLeftButton != null) butLeft!!.setImageDrawable(draLeftButton)
        if (draRightButton != null) butRight!!.setImageDrawable(draRightButton)
        monthYearTextView = findViewById<View>(R.id.tv_month_year) as TextView
        tvDaysOfWeek = arrayOfNulls(7)
        tvDaysOfWeek!![0] = findViewById<View>(R.id.tv_day_of_week_0) as TextView
        tvDaysOfWeek!![1] = findViewById<View>(R.id.tv_day_of_week_1) as TextView
        tvDaysOfWeek!![2] = findViewById<View>(R.id.tv_day_of_week_2) as TextView
        tvDaysOfWeek!![3] = findViewById<View>(R.id.tv_day_of_week_3) as TextView
        tvDaysOfWeek!![4] = findViewById<View>(R.id.tv_day_of_week_4) as TextView
        tvDaysOfWeek!![5] = findViewById<View>(R.id.tv_day_of_week_5) as TextView
        tvDaysOfWeek!![6] = findViewById<View>(R.id.tv_day_of_week_6) as TextView
        llWeeks = findViewById<View>(R.id.ll_weeks) as LinearLayout
        selectedDate = Calendar.getInstance()
        readyDaysOfWeek()
        setAll()
        butLeft!!.setOnClickListener {
            val previousMonth = Calendar.getInstance()
            previousMonth[Calendar.MONTH] = if (selectedDate!!.get(Calendar.MONTH) - 1 != -1) selectedDate!!.get(Calendar.MONTH) - 1 else Calendar.DECEMBER
            previousMonth[Calendar.YEAR] = if (selectedDate!!.get(Calendar.MONTH) - 1 != -1) selectedDate!!.get(Calendar.YEAR) else selectedDate!!.get(Calendar.YEAR) - 1
            previousMonth[Calendar.DAY_OF_MONTH] = if (selectedDate!!.get(Calendar.DAY_OF_MONTH) < previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)) selectedDate!!.get(Calendar.DAY_OF_MONTH) else previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            selectedDate = previousMonth
            if (rightButtonListener != null) {
                val arr: Array<Map<Int, Any?>> =
                    leftButtonListener!!.onNavigationButtonClicked(
                        PREVIOUS,
                        previousMonth
                    )
                mapDateToDesc = arr[0]
                mapDateToTag = arr[1]
            } else {
                mapDateToDesc = null
                mapDateToTag = null
            }
            setAll()
        }
        butRight!!.setOnClickListener {
            val nextMonth = Calendar.getInstance()
            nextMonth[Calendar.MONTH] =
                if (selectedDate!!.get(Calendar.MONTH) + 1 != 12) selectedDate!!.get(Calendar.MONTH) + 1 else Calendar.JANUARY
            nextMonth[Calendar.YEAR] =
                if (selectedDate!!.get(Calendar.MONTH) + 1 != 12) selectedDate!!.get(Calendar.YEAR) else selectedDate!!.get(
                    Calendar.YEAR
                ) + 1
            nextMonth[Calendar.DAY_OF_MONTH] =
                if (selectedDate!!.get(Calendar.DAY_OF_MONTH) < nextMonth.getActualMaximum(
                        Calendar.DAY_OF_MONTH
                    )
                ) selectedDate!!.get(Calendar.DAY_OF_MONTH) else nextMonth.getActualMaximum(
                    Calendar.DAY_OF_MONTH
                )
            selectedDate = nextMonth
            if (leftButtonListener != null) {
                val arr: Array<Map<Int, Any?>> =
                    leftButtonListener!!.onNavigationButtonClicked(
                        NEXT,
                        nextMonth
                    )
                mapDateToDesc = arr[0]
                mapDateToTag = arr[1]
            } else {
                mapDateToDesc = null
                mapDateToTag = null
            }
            setAll()
        }
    }

    private fun setAll() {
        readyMonthAndYear()
        llWeeks!!.removeAllViews()
        allViews = arrayOfNulls(selectedDate!!.getActualMaximum(Calendar.DAY_OF_MONTH))
        var llWeek = LinearLayout(context)
        llWeek.layoutParams =
            LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                if (rowHeight == 0f) LayoutParams.WRAP_CONTENT else rowHeight.toInt()
            )
        llWeek.orientation = HORIZONTAL
        val previousMonth = Calendar.getInstance()
        previousMonth[Calendar.MONTH] = if (selectedDate!![Calendar.MONTH] - 1 != -1) selectedDate!![Calendar.MONTH] - 1 else Calendar.DECEMBER
        previousMonth[Calendar.YEAR] = if (selectedDate!![Calendar.MONTH] - 1 != -1) selectedDate!![Calendar.YEAR] else selectedDate!![Calendar.YEAR] - 1
        val thisMonth = Calendar.getInstance()
        thisMonth[Calendar.MONTH] = selectedDate!![Calendar.MONTH]
        thisMonth[Calendar.YEAR] = selectedDate!![Calendar.YEAR]
        thisMonth[Calendar.DAY_OF_MONTH] = 1
        val j = thisMonth[Calendar.DAY_OF_WEEK] - startFrom - 1
        for (i in 0 until j) {
            var btn: View? = null
            if (mapDescToProp != null && mapDescToProp!!["disabled"] != null && mapDescToProp!!["disabled"]!!.layoutResource !== -1) {
                val prop: Property? = mapDescToProp!!["disabled"]
                btn = LayoutInflater.from(context).inflate(prop!!.layoutResource, null)
                if (prop!!.dateTextViewResource !== -1 && btn!!.findViewById<View?>(prop!!.dateTextViewResource) != null) (btn.findViewById<View>(
                    prop.dateTextViewResource
                ) as TextView).text =
                    "" + (previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - (j - i - 1))
            } else {
                btn = TextView(context)
                btn.text =
                    "" + (previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - (j - i - 1))
            }
            btn!!.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            llWeek.addView(btn)
            btn.isEnabled = false
        }
        var index = 0
        for (i in 0 until 7 - j) {
            allViews!![index] = readyButton(index + 1)
            allViews!![index]!!.isEnabled = true
            llWeek.addView(allViews!![index])
            index++
        }
        llWeeks!!.addView(llWeek)
        while (thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - 7 > index) {
            llWeek = LinearLayout(context)
            llWeek.layoutParams =
                LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    if (rowHeight == 0f) LayoutParams.WRAP_CONTENT else rowHeight.toInt()
                )
            llWeek.orientation = HORIZONTAL
            for (i in 0..6) {
                allViews!![index] = readyButton(index + 1)
                llWeek.addView(allViews!![index])
                allViews!![index]!!.isEnabled = true
                index++
            }
            llWeeks!!.addView(llWeek)
        }
        llWeek = LinearLayout(context)
        llWeek.layoutParams =
            LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                if (rowHeight == 0f) LayoutParams.WRAP_CONTENT else rowHeight.toInt()
            )
        llWeek.orientation = HORIZONTAL
        var i = 0
        while (index < selectedDate!!.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            allViews!![index] = readyButton(index + 1)
            llWeek.addView(allViews!![index])
            allViews!![index]!!.isEnabled = true
            index++
            i++
        }
        for (k in 1..7 - i) {
            var btn: View? = null
            if (mapDescToProp != null && mapDescToProp!!["disabled"] != null) {
                val prop: Property? = mapDescToProp!!["disabled"]
                prop?.let {
                    btn = LayoutInflater.from(context).inflate(prop.layoutResource, null)
                    if (prop.dateTextViewResource !== -1 && btn!!.findViewById<View?>(prop.dateTextViewResource) != null) (btn!!.findViewById<View>(prop.dateTextViewResource) as TextView).text = "" + k
                }
            } else {
                btn = TextView(context)
                (btn as TextView).text = "" + k
            }
            btn!!.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            llWeek.addView(btn)
            btn!!.isEnabled = false
        }
        llWeeks!!.addView(llWeek)
    }

    private fun readyDaysOfWeek() {
        val arrOfDaysOfWeek = resources.getStringArray(R.array.days_of_week)
        var j = 0
        run {
            var i = startFrom
            while (i < 7) {
                if (dayOfWeekLength > arrOfDaysOfWeek[i].length) tvDaysOfWeek!![j]!!.text =
                    arrOfDaysOfWeek[i] else tvDaysOfWeek!![j]!!.text =
                    arrOfDaysOfWeek[i].substring(0, dayOfWeekLength)
                i++
                j++
            }
        }
        var i = 0
        while (i < startFrom) {
            if (dayOfWeekLength > arrOfDaysOfWeek[i].length) tvDaysOfWeek!![j]!!.text =
                arrOfDaysOfWeek[i] else tvDaysOfWeek!![j]!!.text =
                arrOfDaysOfWeek[i].substring(0, dayOfWeekLength)
            i++
            j++
        }
    }

    private fun readyMonthAndYear() {
        when (monthYearFormat) {
            0 -> monthYearTextView!!.text =
                MONTHS[selectedDate!![Calendar.MONTH]].substring(
                    0,
                    3
                ) + " " + selectedDate!![Calendar.YEAR]

            1 -> monthYearTextView!!.text =
                MONTHS[selectedDate!![Calendar.MONTH]] + " " + selectedDate!![Calendar.YEAR]
        }
    }

    private fun readyButton(date: Int): View {
        val btn: View
        if (mapDescToProp != null) {
            var prop: Property? = mapDescToProp!!["default"]
            if (mapDateToDesc != null && mapDateToDesc!![date] != null && mapDateToDesc!![date] != "default") {
                prop = mapDescToProp!![mapDateToDesc!![date]]
            }
            if (prop != null && prop.layoutResource !== -1) {
                btn = LayoutInflater.from(context).inflate(prop.layoutResource, null)
                if (prop.dateTextViewResource !== -1) (btn.findViewById<View>(prop.dateTextViewResource) as TextView).text =
                    "" + date
                btn.isEnabled = prop.enable
            } else {
                btn = TextView(context)
                btn.text = "" + date
            }
        } else {
            btn = TextView(context)
            btn.text = "" + date
        }
        btn.layoutParams =
            LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        if (mapDateToTag != null) btn.tag = mapDateToTag!![date]
        btn.setOnClickListener {
            selectedDate!![Calendar.DAY_OF_MONTH] = date
            if (listener != null) {
                listener!!.onDateSelected(btn, selectedDate, mapDateToDesc!![date])
            }
            if (selectedButton != null) selectedButton!!.isSelected = false
            btn.isSelected = true
            selectedButton = btn
        }
        if (selectedDate!![Calendar.DAY_OF_MONTH] == date) {
            btn.isSelected = true
            selectedButton = btn
        }
        return btn
    }

    /**
     * Set the format in which the month and the year are displayed on the top.
     * @param monthYearFormat Either `CustomCalendar.THREE_LETTER_MONTH__WITH_YEAR` or `CustomCalendar.FULL_MONTH__WITH_YEAR`.
     */
    fun setMonthYearFormat(monthYearFormat: Int) {
        this.monthYearFormat = monthYearFormat
        readyMonthAndYear()
    }

    /**
     * Set the length of the day of week displayed above the dates.
     * @param length length of the day of week.
     */
    fun setDayOfWeekLength(length: Int) {
        dayOfWeekLength = length
        readyDaysOfWeek()
    }

    /**
     * Set the height of every row of the CustomCalendar.
     * @param rowHeight Height of the row.
     */
    fun setRowHeight(rowHeight: Float) {
        if (rowHeight > 0) {
            this.rowHeight = rowHeight
            setAll()
        }
    }

    /**
     * Set the day of week from which the calendar starts.
     * @param whichDay `CustomCalendar.SUNDAY`, `CustomCalendar.MONDAY`, `CustomCalendar.TUESDAY`, `CustomCalendar.WEDNESDAY`, `CustomCalendar.THURSDAY`, `CustomCalendar.FRIDAY` or `CustomCalendar.SATURDAY`.
     */
    fun setDayOfWeekStartFrom(whichDay: Int) {
        startFrom = whichDay
        setAll()
    }

    /**
     * Set the drawable on a navigation button.
     * @param whichButton Either `CustomCalendar.PREVIOUS` or `CustomCalendar.NEXT`.
     * @param resourceId Resource id of the drawable.
     */
    fun setNavigationButtonDrawable(whichButton: Int, resourceId: Int) {
        when (whichButton) {
            PREVIOUS -> butLeft!!.setImageResource(resourceId)
            NEXT -> butRight!!.setImageResource(resourceId)
        }
    }

    /**
     * Set the drawable on a navigation button.
     * @param whichButton Either `CustomCalendar.PREVIOUS` or `CustomCalendar.NEXT`.
     * @param drawable Drawable to be set.
     */
    fun setNavigationButtonDrawable(whichButton: Int, drawable: Drawable?) {
        when (whichButton) {
            PREVIOUS -> butLeft!!.setImageDrawable(drawable)
            NEXT -> butRight!!.setImageDrawable(drawable)
        }
    }

    /**
     * Set the date shown on the CustomCalendar.
     * @param calendar The month and year combination in this calendar object will be used to show the current month and the day of month in this calendar object will be set selected.
     */
    fun setDate(calendar: Calendar?) {
        selectedDate = calendar
        setAll()
    }

    /**
     * Set the date shown on the CustomCalendar and the map linking a date to its description.
     * @param calendar The month and year combination in this calendar object will be used to show the current month and the day of month in this calendar object will be set selected.
     * @param mapDateToDesc The map linking a date to its description. This description will be accessible from the `desc` parameter of the onDateSelected method of OnDateSelectedListener.
     */
    fun setDate(calendar: Calendar?, mapDateToDesc: Map<Int, Any?>?) {
        selectedDate = calendar
        this.mapDateToDesc = mapDateToDesc
        setAll()
    }

    /**
     * Set the date shown on the CustomCalendar, the map linking a date to its description and the map linking a date to the tag on its view.
     * @param calendar The month and year combination in this calendar object will be used to show the current month and the day of month in this calendar object will be set selected.
     * @param mapDateToDesc The map linking a date to its description. This description will be accessible from the `desc` parameter of the onDateSelected method of OnDateSelectedListener.
     * @param mapDateToTag The map linking a date to the tag to be set on its date view. This tag will be accessible from the `view` parameter of the onDateSelected method of the OnDateSelectedListener.
     */
    fun setDate(
        calendar: Calendar?,
        mapDateToDesc: Map<Int, Any?>?,
        mapDateToTag: Map<Int, Any?>?
    ) {
        selectedDate = calendar
        this.mapDateToDesc = mapDateToDesc
        this.mapDateToTag = mapDateToTag
        setAll()
    }

    /**
     * Set the map linking a description to its respective Property object.
     * @param mapDescToProp The map linking description to its property.
     */
    fun setMapDescToProp(mapDescToProp: Map<Any?, Property?>?) {
        this.mapDescToProp = mapDescToProp
        setAll()
    }

    /**
     * Register a callback to be invoked when a date is clicked.
     * @param listener The callback that will run.
     */
    fun setOnDateSelectedListener(listener: OnDateSelectedListener?) {
        this.listener = listener
        readyMonthAndYear()
    }

    /**
     * Register a callback to be invoked when a month navigation button is clicked.
     * @param whichButton Either `CustomCalendar.PREVIOUS` or `CustomCalendar.NEXT`.
     * @param listener The callback that will run.
     */
    fun setOnNavigationButtonClickedListener(
        whichButton: Int,
        listener: OnNavigationButtonClickedListener?
    ) {
        if (whichButton == PREVIOUS) leftButtonListener =
            listener else if (whichButton == NEXT) rightButtonListener = listener
    }

    /**
     * Set the enabled state of a month navigation button.
     * @param whichButton Either `CustomCalendar.PREVIOUS` or `CustomCalendar.NEXT`.
     * @param enable True if the button is enabled, false otherwise.
     */
    fun setNavigationButtonEnabled(whichButton: Int, enable: Boolean) {
        if (whichButton == PREVIOUS) butLeft!!.isEnabled =
            enable else if (whichButton == NEXT) butRight!!.isEnabled =
            enable
    }

    companion object {
        const val PREVIOUS = -1
        const val NEXT = 1
        const val THREE_LETTER_MONTH__WITH_YEAR = 0
        const val FULL_MONTH__WITH_YEAR = 1
        const val SUNDAY = 0
        const val MONDAY = 1
        const val TUESDAY = 2
        const val WEDNESDAY = 3
        const val THURSDAY = 4
        const val FRIDAY = 5
        const val SATURDAY = 6
    }
}