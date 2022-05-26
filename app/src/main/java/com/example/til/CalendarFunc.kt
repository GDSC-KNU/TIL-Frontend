package com.example.til

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.calendar.*
import kotlinx.android.synthetic.main.calendar.mypage
import kotlinx.android.synthetic.main.list.*
import org.json.JSONArray
import org.json.JSONObject

class CalendarFunc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

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
        var list = ArrayList<Data>()
        try {
            val url = "https://gdsc-knu-til.herokuapp.com/posts"
            list=getData(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val eventList = ArrayList<CalendarDay>()
        for(d in list){
            var ymd = d.date.split("-")
            eventList.add(CalendarDay.from(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2])))
        }

        val eventIs = EventExpress(this, eventList)
        materialCalendar.addDecorator(eventIs)


        //클릭한 날짜 표시
        materialCalendar.setOnDateChangedListener { _: MaterialCalendarView, date: CalendarDay, b: Boolean ->
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
           //     Toast.makeText(this, search.text, Toast.LENGTH_LONG).show()
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
        var list = java.util.ArrayList<Data>()
        try {
            val url = "https://gdsc-knu-til.herokuapp.com/posts/search?query="+search_item
            list=getData(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getData(url : String) : java.util.ArrayList<Data> {
        val list = java.util.ArrayList<Data>()
        // OkHttp 클라이언트 객체 생성
        val client = OkHttpClient()

        // GET 요청 객체 생성
        val builder = Request.Builder().url(url).get()
        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        val request = builder.build()

        // OkHttp 클라이언트로 GET 요청 객체 전송
        val response = client.newCall(request).execute()
        println("START")
        if (response.isSuccessful) {
            // 응답 받아서 처리
            val body = response.body()
            if (body != null) {
                val responseStr = body.string()
                val json : JSONObject = JSONObject(responseStr)
                var json_arr : JSONArray =json.getJSONArray("data")

                //    var arrayList : ArrayList<TIL> = ArrayList()
                for(i in 0 until json_arr.length()){
                    var json_content : JSONObject = json_arr.getJSONObject(i)
                    var til : Data = Data(
                        json_content.getInt("id"),
                        json_content.getString("title"),
                        json_content.getString("date"),
                        json_content.getString("content")
                    )
                    list.add(til)
                }
            }
        } else System.err.println("Error Occurred")

        return list
    }
}

