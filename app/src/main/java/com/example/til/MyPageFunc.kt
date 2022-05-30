package com.example.til

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import com.example.til.jwt.AuthInterceptor
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.mypage.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Integer.min

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
        DataSet.setColor(Color.rgb(107,153,0))
        val data= BarData(DataSet)
        // 격자선 제거
        barChart.getXAxis().setDrawGridLines(false)
        barChart.getAxisLeft().setDrawGridLines(false)

        barChart.getXAxis().setDrawLabels(false)

        // y축 간격 조절
        barChart.getAxisLeft().granularity=1f
        barChart.getAxisRight().granularity=1f
        barChart.data=data


    }

    private fun GetMaxPost() {
        try {
            val url = "http://gdsc-knu-til.herokuapp.com/me/maximum_post_number_date"
            val client = OkHttpClient()
            client.interceptors().add(AuthInterceptor())

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
                    val json = JSONObject(responseStr)
                    val jsonArr = json.getJSONArray("data")
                    val listLength = min(jsonArr.length(), 3)

                    top_title.text = getString(R.string.mypage_top_title, listLength)
                    // FIXME 잘 몰라서... 하드 코딩으로...
                    if (listLength >= 1) {
                        best_date1.text = jsonArr.getJSONObject(0).getString("date")
                        best_cnt1.text = jsonArr.getJSONObject(0).getString("number")
                    }
                    if (listLength >= 2) {
                        best_date2.text = jsonArr.getJSONObject(1).getString("date")
                        best_cnt2.text = jsonArr.getJSONObject(1).getString("number")
                    }
                    if (listLength >= 3) {
                        best_date3.text = jsonArr.getJSONObject(2).getString("date")
                        best_cnt3.text = jsonArr.getJSONObject(2).getString("number")
                    }
                }
            } else System.err.println("Error Occurred")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun GetNumPerDay(url : String) : ArrayList<Post> {
        val list = ArrayList<Post>()
        val client = OkHttpClient()
        client.interceptors().add(AuthInterceptor())

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

