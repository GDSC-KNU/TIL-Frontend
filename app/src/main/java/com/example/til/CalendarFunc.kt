package com.example.til

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calendar.*

class CalendarFunc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)



        mypage.setOnClickListener({
            val intent = Intent(this, MyPageFunc::class.java)
            startActivity(intent)
        })

        changeto.setOnClickListener({
            val intent = Intent(this, ListFunc::class.java)
            startActivity(intent)
        })

        val todayexpress = TodayExpress(this)
        materialCalendar.addDecorator(todayexpress)
        floatingActionButton.setOnClickListener({
            val intent = Intent(this, WritingFunc::class.java)
            startActivity(intent)
        })

        materialCalendar.setOnDateChangedListener ({ materialCalendarView: MaterialCalendarView, date: CalendarDay, b: Boolean ->
            calendartextView.text = String.format("%d, %d, %d",date.year, date.month, date.day)
        })


    }
}