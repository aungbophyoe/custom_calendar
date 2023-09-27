package com.example.customcalendar

import java.util.Date

data class CalendarDay(
    val dayNumber: Int,
    val date: String?,
    val dayType: DayType,
    val isToday : Boolean = false,
    var isSelected : Boolean = false,
    var isChecked : Boolean = false
)

enum class DayType {
    CURRENT_MONTH,
    PREVIOUS_MONTH,
    NEXT_MONTH
}