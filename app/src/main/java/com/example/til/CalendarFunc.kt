package com.example.til

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.calendar.*

class CalendarFunc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        //화면 이동
        mypage.setOnClickListener{
            val intent = Intent(this, MyPageFunc::class.java)
            startActivity(intent)
        }

        changeto.setOnClickListener {
            val intent = Intent(this, ListFunc::class.java)
            startActivity(intent)
        }

        floatingActionButton.setOnClickListener{
            val intent = Intent(this, WritingFunc::class.java)
            startActivity(intent)
        }


        //오늘 표시
        val todayIs = TodayExpress(this)
        materialCalendar.addDecorator(todayIs)

        //기록 유무 표시
        //api 받아와서 나중에 year, month, day 에 각각 int형으로 넣어주기
        val eventList = ArrayList<CalendarDay>()
        eventList.add(CalendarDay.from(2022,4,1))
        eventList.add(CalendarDay.from(2022,4,2))

        val eventIs = EventExpress(this, eventList)
        materialCalendar.addDecorator(eventIs)

        
        //클릭한 날짜 표시
        materialCalendar.setOnDateChangedListener ({ materialCalendarView: MaterialCalendarView, date: CalendarDay, b: Boolean ->
            calendartextView.text = String.format("%d, %d, %d",date.year, date.month, date.day)

        })



    }
}

