package com.example.til

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.os.Build
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.til.databinding.WritingBinding
import com.example.til.jwt.AuthInterceptor
import com.squareup.okhttp.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Array.get
import java.nio.file.Paths.get
import java.sql.DriverManager.println
import java.time.LocalDate

class WritingFunc : AppCompatActivity() {
    private var mBinding : WritingBinding? = null
    private val binding get() = mBinding!!
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = WritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val markdownView = binding.markdownView
        var uploadText :String
        var uploadTitle: String
        var id = 1
        markdownView.loadMarkdown("## Input Markdown")
        binding.applyButton.setOnClickListener {
            uploadText = binding.editText.text.toString()
            uploadTitle = binding.editTitle.text.toString()
            markdownView.loadMarkdown(uploadText)
            Log.d("markdown", "markdown uploaded")
        }
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.uploadButton.setOnClickListener {
            sendPost(binding, id)
            id+=1
        }

    }
    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendPost(binding: WritingBinding, id: Int) {
        val title = binding.editTitle.text.toString()
        val content = binding.editText.text.toString()
        val date = LocalDate.now().toString()
        println(date)
        println("0---------")
        val JSON = MediaType.parse("application/json; charset=utf-8")
        Log.d("upload", "upload start")

//		val body =
//			"{\n" + "\"title\": \"" + title + "\",\n\"date\":\"" + date + "\",\n\"content\":\"" + content + "\"\n}"
        val url = "http://gdsc-knu-til.herokuapp.com/posts"

        val client = OkHttpClient()
        client.interceptors().add(AuthInterceptor())

        //POST 요청 객체 생성
        val jsonInput = JSONObject()
        try{
            jsonInput.put("title", title)
            jsonInput.put("date", date)
            jsonInput.put("content", content)
        } catch(e:JSONException){
            e.printStackTrace();
            return;
        }
        val body = RequestBody.create(JSON, jsonInput.toString())
        Log.d("put","json input done")

        val builder = Request.Builder().url(url).post(body)
        builder.addHeader("Content-Type", "application/json; charset=utf-8")
        val request = builder.build()
        Log.d("build","build done")

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val postId = response.body().string()
            val intent= Intent(this, DetailContent::class.java)
            intent.putExtra("Detail_ID", Integer.parseInt(postId))
            ContextCompat.startActivity(this, intent, null)
            finish()
            return
        }
    }
}
