package com.example.til

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.detail_page.*
import kotlinx.android.synthetic.main.detail_page.content_content
import kotlinx.android.synthetic.main.detail_page.content_date
import kotlinx.android.synthetic.main.list.*
import kotlinx.android.synthetic.main.mypage.*
import org.json.JSONArray
import org.json.JSONObject

class MyPageFunc  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage)

        change_to_c.setOnClickListener {
            val intent = Intent(this, CalendarFunc::class.java)
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        GetMaxPost()
        val day_list=GetNumPerDay("http://gdsc-knu-til.herokuapp.com/me/number_of_posts_per_day")

        val barList : ArrayList<BarEntry> = ArrayList()
        val title="게시물 수"
        for(i in 0 until day_list.size){
            barList.add(BarEntry(i.toFloat(), day_list.get(i).number.toFloat()))
        }

        val barChart : BarChart = findViewById(R.id.barChart)
        val DataSet = BarDataSet(barList, title)
        DataSet.setColor(Color.GRAY)
        val data= BarData(DataSet)
        barChart.data=data

    }

    private fun GetMaxPost() {
        try {
            val url = "http://gdsc-knu-til.herokuapp.com/me/maximum_post_number_date"
            val client = OkHttpClient()

            // GET 요청 객체 생성
            val builder = Request.Builder().url(url).get()
            builder.addHeader("Content-Type", "application/json; charset=utf-8");
            val request = builder.build()

            // OkHttp 클라이언트로 GET 요청 객체 전송
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                // 응답 받아서 처리
                val body = response.body()
                if (body != null) {
                    val responseStr = body.string()
                    println(responseStr)
                    val json = JSONObject(responseStr)
                    best_date.setText(json.getJSONObject("data").getString("date"))
                    best_cnt.setText(json.getJSONObject("data").getString("number"))
                }
            } else System.err.println("Error Occurred")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun GetNumPerDay(url : String) : ArrayList<Post> {
        val list = ArrayList<Post>()
        val client = OkHttpClient()

        val builder = Request.Builder().url(url).get()
        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        val request = builder.build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val responseStr = body.string()
                val json : JSONObject = JSONObject(responseStr)
                var json_arr : JSONArray =json.getJSONArray("data")

                for(i in 0 until json_arr.length()){
                    var json_content : JSONObject = json_arr.getJSONObject(i)
                    var post : Post = Post(
                        json_content.getString("date"),
                        json_content.getString("number")
                    )
                    list.add(post)
                }
            }
        } else System.err.println("Error Occurred")

        return list
    }

    class Post(val date : String, val number : String)
//    class BarEntry(val x : String, val y : Int)
}

