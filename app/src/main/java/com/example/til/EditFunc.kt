package com.example.til

//import android.os.Bundle
//import android.os.PersistableBundle
//import android.support.v7.app.AppCompatActivity
//
//class EditFunc : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.edit_page)
//    }
//}

//import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.til.databinding.EditPageBinding
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import kotlinx.android.synthetic.main.edit_page.*
import org.json.JSONObject

class EditFunc : AppCompatActivity() {
    private var mBinding : EditPageBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        mBinding = EditPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val tmp=intent.getIntExtra("Edit_ID", 0).toString()
        val id=Integer.parseInt(tmp)

        renderPage(id)

        val markdownView = markdownView
        var uploadText :String = ""
        var uploadDate : String = ""
        var uploadTitle: String = ""
        markdownView.loadMarkdown("## Input Markdown")
        applyButton.setOnClickListener {
            uploadText = editText.text.toString()
            uploadDate = editDate.text.toString()
            uploadTitle = editTitle.text.toString()
            markdownView.loadMarkdown(uploadText)
            Log.d("markdown", "markdown uploaded")
        }
        backButton.setOnClickListener {
            finish()
        }
        uploadButton.setOnClickListener {
            post(id, uploadTitle, uploadDate, uploadText)
        }

    }
    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    private fun renderPage(id: Int) {
        try {
            val url = "http://gdsc-knu-til.herokuapp.com/posts/"+id
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
                    val json = JSONObject(responseStr)
                    editTitle.setText(json.getJSONObject("data").getString("title"))
                    editDate.setText(json.getJSONObject("data").getString("date"))
                    editText.setText(json.getJSONObject("data").getString("content"))
                }
            } else System.err.println("Error Occurred")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun post(id: Int, uploadTitle: String, uploadDate: String, uploadText: String){
        try {
            val url = "http://gdsc-knu-til.herokuapp.com/posts/"+id
            val post=""+"{"+"\"title\": "+"\""+uploadTitle+"\","+ "\"date\": "+"\""+uploadDate+"\","+
            "\"content\": "+"\""+uploadText+"\""+"}"
            println(post)

            val client = OkHttpClient()
            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), post)
            // GET 요청 객체 생성
            val builder = Request.Builder().url(url).put(requestBody)
            val request = builder.build()

            // OkHttp 클라이언트로 GET 요청 객체 전송
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                // 응답 받아서 처리
                val body = response.body()
                if (body != null) {
                    val responseStr = body.string()
                    println(responseStr)

                    val intent= Intent(this, DetailContent::class.java)
                    intent.putExtra("Detail_ID", Integer.parseInt(responseStr))
                    ContextCompat.startActivity(this, intent, null)
                }
            } else System.err.println("Error Occurred")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}