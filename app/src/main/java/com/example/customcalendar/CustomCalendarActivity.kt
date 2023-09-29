package com.example.customcalendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CustomCalendarActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_calendar)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val listButton = findViewById<Button>(R.id.listButton)
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

    }
}