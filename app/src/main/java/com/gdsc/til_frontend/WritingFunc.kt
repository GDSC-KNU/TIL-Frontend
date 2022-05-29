package com.gdsc.til_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import com.gdsc.til_frontend.databinding.WritingBinding
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
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//setContentView(R.layout.activity_main)
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
	private fun sendPost(binding: WritingBinding, id: Int) {
		val title = binding.editTitle.text.toString()
		val content = binding.editText.text.toString()
		val date = LocalDate.now().toString()
		val JSON = MediaType.parse("application/json; charset=utf-8")
		Log.d("upload", "upload start")

//		val body =
//			"{\n" + "\"title\": \"" + title + "\",\n\"date\":\"" + date + "\",\n\"content\":\"" + content + "\"\n}"
		val url = "http://gdsc-knu-til.herokuapp.com/posts"

		val client = OkHttpClient()

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

			return
		}
	}
}
