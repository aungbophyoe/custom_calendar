package com.example.customcalendar


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EditCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_calendar)
        val wheelView = findViewById<WheelView>(R.id.wheelView)
        val wheelViewDayTime = findViewById<WheelView>(R.id.wheelViewDayTime)
        val time = arrayOf("1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00","9:00","10:00","11:00","12:00")
        val dayTime = arrayOf("AM", "PM")
        wheelView.setOffset(2)
        wheelView.setItems(time.toList())
        wheelView.setSeletion(4)
        wheelView.onWheelViewListener = object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String) {

            }
        }

        wheelViewDayTime.setOffset(1)
        wheelViewDayTime.setItems(dayTime.toList())
        wheelViewDayTime.onWheelViewListener = object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String) {

            }
        }
    }
}