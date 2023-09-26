package com.example.customcalendar

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendar.databinding.RowMonthForPTrackerBinding
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import java.time.Month
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val caldroidFragment: CaldroidFragment by lazy {
        val calendar = Calendar.getInstance()
        CaldroidFragment().apply {
            arguments = Bundle().apply {
                putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1)
                putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR))
                putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false)
                putBoolean(CaldroidFragment.ENABLE_SWIPE,false)
                putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS,false)
                putInt(CaldroidFragment.THEME_RESOURCE,R.style.CaldroidCustom)
            }
        }
    }

    private val monthlyDataList = listOf<MonthlyData>(
        MonthlyData(1,9),
        MonthlyData(2,10),
        MonthlyData(3,11),
        MonthlyData(4,12),
        MonthlyData(5,1),
        MonthlyData(6,2),
        MonthlyData(7,3),
        MonthlyData(8,4),
        MonthlyData(9,5),
        MonthlyData(10,6),
        MonthlyData(11,7),
        MonthlyData(12,8),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = MyAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,2)

        // Submit the data to the adapter
        adapter.submitList(monthlyDataList)
    }

    /*private fun setup() {
        caldroidFragment.caldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date, view: View?) {

            }
        }
        val dayArr = listOf<Int>(7,8,9)
        val fertileDayArr = listOf<Int>(10,11,12,13,14,15,16,18,19,20)
        val ovulationDayArr = listOf<Int>(17)
        val selectionBackground = hashMapOf<Date,Drawable?>()
        val selectionTextColor = hashMapOf<Date,Int>()
        dayArr.forEach {
            val date = getDate(2023,9,it)
            selectionBackground[date] = ContextCompat.getDrawable(this,R.drawable.default_background_cell)
            selectionTextColor[date] = R.color.white
        }
        fertileDayArr.forEach {
            val date = getDate(2023,9,it)
            selectionBackground[date] = ContextCompat.getDrawable(this,R.drawable.fertile_cell)
            selectionTextColor[date] = R.color.primary_green_blue
        }
        ovulationDayArr.forEach {
            val date = getDate(2023,9,it)
            selectionBackground[date] = ContextCompat.getDrawable(this,R.drawable.ovulation_cell)
            selectionTextColor[date] = R.color.white
        }
        caldroidFragment.setTextColorForDates(selectionTextColor)
        caldroidFragment.setBackgroundDrawableForDates(selectionBackground)
        supportFragmentManager.beginTransaction()
            .replace(R.id.calendarView, caldroidFragment as Fragment)
            .commit()
    }*/

    fun getDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // Month is zero-based, so we subtract 1
        return calendar.time
    }

}


