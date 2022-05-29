package com.example.til

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.example.til.jwt.AuthInterceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.detail_page.*
import org.json.JSONObject


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

        renderPage(id)

        edit_btn.setOnClickListener {
            val intent = Intent(this, EditFunc::class.java)
            intent.putExtra("Edit_ID", id)
            startActivity(intent)
        }

        delete_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("게시물 삭제")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("확인"
                ) { _, _ ->
                    this.deletePost(id)
                }
                .setNegativeButton("취소"
                ) { _, _ -> }

            builder.show()
        }

        return_button.setOnClickListener() {
            val intent = Intent(this, ListFunc::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun renderPage(id: Int) {
        try {
            val url = "http://gdsc-knu-til.herokuapp.com/posts/"+id
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

                    val title = json.getJSONObject("data").getString("title")
                    val date = json.getJSONObject("data").getString("date")

                    content_title.setText(title)
                    content_date.setText(date)
                    content_content.setText(json.getJSONObject("data").getString("content"))
                }
            } else {
                System.err.println("Code: ${response.code()}, body: ${response.body().string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deletePost(id: Int) {
        try {
            val url = "http://gdsc-knu-til.herokuapp.com/posts/" + id
            val client = OkHttpClient()
            client.interceptors().add(AuthInterceptor())

            // GET 요청 객체 생성
            val builder = Request.Builder().url(url).delete()
            builder.addHeader("Content-Type", "application/json; charset=utf-8");
            val request = builder.build()

            // OkHttp 클라이언트로 GET 요청 객체 전송
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                finish()
            } else {
                System.err.println("응답 코드 : " + response.code())
                System.err.println(response.body().string())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}