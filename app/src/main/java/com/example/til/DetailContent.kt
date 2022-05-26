package com.example.til

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.detail_page.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class DetailContent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val tmp=intent.getIntExtra("Detail_ID", 0).toString()
        val id=Integer.parseInt(tmp)

        try {
            val url = "https://gdsc-knu-til.herokuapp.com/posts/"+id
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
                    println(responseStr)
                    val json : JSONObject = JSONObject(responseStr)
                    content_title.setText(json.getJSONObject("data").getString("title"))
                    content_date.setText(json.getJSONObject("data").getString("date"))
                    content_content.setText(json.getJSONObject("data").getString("content"))
//                    var json_arr : JSONArray =json.getJSONArray("data")
//
//
//                    var json_content: JSONObject = json_arr.getJSONObject(0)
//                    println(json_content.getInt("id"))

                    //    var arrayList : ArrayList<TIL> = ArrayList()
//                    for(i in 0 until json_arr.length()) {
//                        var json_content: JSONObject = json_arr.getJSONObject(i)
//                        println(json_content.getInt("id"))

//                        content_title.setText(json_content.getString("title"))
//                        content_date.setText(json_content.getString("date"))
//                        content_content.setText(json_content.getString("content"))
                 //   }
                }
            } else System.err.println("Error Occurred")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return_button.setOnClickListener() {
            finish()
        }
    }
}