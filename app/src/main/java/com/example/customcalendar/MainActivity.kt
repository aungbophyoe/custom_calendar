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

    private val monthlyDataList = listOf<MonthlyData>(
        MonthlyData(1,"1-2023"),
        MonthlyData(2,"2-2023"),
        MonthlyData(3,"3-2023"),
        MonthlyData(4,"4-2023"),
        MonthlyData(5,"5-2023"),
        MonthlyData(6,"6-2023"),
        MonthlyData(7,"7-2023"),
        MonthlyData(8,"8-2023"),
        MonthlyData(9,"9-2023"),
        MonthlyData(10,"10-2023"),
        MonthlyData(11,"11-2023"),
        MonthlyData(12,"12-2023"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = MyAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,2)
        adapter.submitList(monthlyDataList)
        recyclerView.scrollToPosition(6)
    }

}


