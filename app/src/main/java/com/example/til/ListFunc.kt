package com.example.til

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import com.example.til.jwt.AuthInterceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.list.*
import org.json.JSONArray
import org.json.JSONObject

class ListFunc: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val list = dataAdd()
        val adapter = RecycleAdapter(list)
        recycleView.adapter = adapter

        list_search.setOnKeyListener {v, keyCode, event ->
            val imm=this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
              //  Toast.makeText(this, list_search.text, Toast.LENGTH_LONG).show()
                imm.hideSoftInputFromWindow(list_search.windowToken, 0)

                val list = search_dataAdd(list_search.text.toString())
                val adapter = RecycleAdapter(list)
                recycleView.adapter = adapter
                return@setOnKeyListener true
            }
            false
        }

        changetoc.setOnClickListener {
            finish()
        }

        mypage.setOnClickListener {
            val intent = Intent(this, MyPageFunc::class.java)
            startActivity(intent)

            finish()
        }

        floatingActionButton.setOnClickListener{
            val intent = Intent(this, WritingFunc::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val list = dataAdd()
        val adapter = RecycleAdapter(list)
        recycleView.adapter = adapter
    }

    fun getData(url : String) : ArrayList<Data> {
        val list = ArrayList<Data>()
        // OkHttp 클라이언트 객체 생성
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
                val json_arr : JSONArray =json.getJSONArray("data")

                for(i in 0 until json_arr.length()){
                    val json_content : JSONObject = json_arr.getJSONObject(i)

                    var content = json_content.getString("content")
                    if (content.length > 40) {
                        content = content.substring(0, 40) + "..."
                    }

                    val til = Data(
                        json_content.getInt("id"),
                        json_content.getString("title"),
                        json_content.getString("date"),
                        content
                    )
                    list.add(til)
                }
            }
        } else System.err.println("Error Occurred")

        return list
    }

    fun dataAdd(): ArrayList<Data> {
        var list = ArrayList<Data>()

        try {
            val url = "http://gdsc-knu-til.herokuapp.com/posts"
            list=getData(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun search_dataAdd(search_item : String): ArrayList<Data> {
        var list = ArrayList<Data>()
        try {
            val url = "http://gdsc-knu-til.herokuapp.com/posts/search?query="+search_item
            list=getData(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

}

