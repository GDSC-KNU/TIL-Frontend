package com.example.til

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.calendar.*
import kotlinx.android.synthetic.main.calendar.mypage
import kotlinx.android.synthetic.main.list.*

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
//예시
        val list = java.util.ArrayList<Data>()
        list.add(Data(1, "title", "2022-05-08", "hello"))
        list.add(Data(2, "title2", "2022-05-09","hello2"))
        list.add(Data(3, "title3", "2022-05-10","hello3"))
        list.add(Data(4, "title4", "2022-05-10","hello4"))
        list.add(Data(5, "title5", "2022-05-16","hello5"))

        val eventList = ArrayList<CalendarDay>()
        for(d in list){
            var ymd = d.date.split("-")
            eventList.add(CalendarDay.from(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2])))
        }

//        eventList.add(CalendarDay.from(2022,4,1))
//        eventList.add(CalendarDay.from(2022,4,2))

        val eventIs = EventExpress(this, eventList)
        materialCalendar.addDecorator(eventIs)

        
        //클릭한 날짜 표시
        materialCalendar.setOnDateChangedListener { _: MaterialCalendarView, date: CalendarDay, b: Boolean ->
            // calendartextView.text = String.format("%d, %d, %d",date.year, date.month, date.day)
            val clickList = java.util.ArrayList<Data>()

            for (d in list) {
                var ymd = d.date.split("-")
                var yy = Integer.parseInt(ymd[0])
                var mm = Integer.parseInt(ymd[1])
                var dd = Integer.parseInt(ymd[2])
                if (date.year == yy && date.month == mm && date.day == dd) {
                    clickList.add(Data(d.id, d.title, d.date, d.content))
                }
            }
            val adapter = RecycleAdapter(clickList)
            calendar_recycleView.adapter = adapter

        }

        //검색

        search.setOnKeyListener {v, keyCode, event ->
            val imm=this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(event.action == ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                Toast.makeText(this, search.text, Toast.LENGTH_LONG).show()
                imm.hideSoftInputFromWindow(search.windowToken, 0)

                var list = search_dataAdd(search.text.toString())
                val adapter = RecycleAdapter(list)
                calendar_recycleView.adapter = adapter
                return@setOnKeyListener true
            }
            false
        }
    }

    fun search_dataAdd(search_item : String): java.util.ArrayList<Data> {
        val list = java.util.ArrayList<Data>()
        list.add(Data(1, "title", "2022-05-08", "hello"))
        list.add(Data(2, "title2", "2022-05-09","hello2"))
        list.add(Data(3, "title3", "2022-05-10","hello3"))

        return list
    }
}


