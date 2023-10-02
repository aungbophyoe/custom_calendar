package com.example.customcalendar

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.customcalendar.WheelView.OnWheelViewListener


class CustomCalendarActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_calendar)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val listButton = findViewById<Button>(R.id.listButton)
        val wheelView = findViewById<WheelView>(R.id.wheelView)
        val calendarView = findViewById<CustomCalendarView>(R.id.calendarView)
        nextButton.setOnClickListener {
            Intent(this, EditCalendarActivity::class.java).apply {
                startActivity(this)
            }
        }
        listButton.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
        val array = arrayOf("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto")
        wheelView.setOffset(2)
        wheelView.setItems(array.toList())
        wheelView.setSeletion(4)
        wheelView.onWheelViewListener = object : OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String) {

            }
        }

        val periodDrawable = ContextCompat.getDrawable(this,R.drawable.default_background_cell)
        val ovulationDrawable = ContextCompat.getDrawable(this,R.drawable.ovulation_cell)
        val fertileDrawable = ContextCompat.getDrawable(this,R.drawable.fertile_cell)

        val dataHashMap = hashMapOf<String, Drawable>()
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

        calendarView.setSelectionDataHashMap(dataHashMap)

    }
}