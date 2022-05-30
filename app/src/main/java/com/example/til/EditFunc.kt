package com.example.til

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.til.databinding.EditPageBinding
import com.example.til.jwt.AuthInterceptor
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import kotlinx.android.synthetic.main.edit_page.*
import org.json.JSONObject
import java.util.*

class EditFunc : AppCompatActivity() {
    private var mBinding : EditPageBinding? = null
    private val binding get() = mBinding!!

    private var viewMode = false

    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = EditPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val tmp = intent.getIntExtra("Edit_ID", 0).toString()
        val id = Integer.parseInt(tmp)

        renderPage(id)

        binding.markdownView.setBackgroundColor(0)

        binding.btnDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                binding.textDate.text = getString(R.string.send_date_format, year, month + 1, day)
            }, year, month, day)

            datePickerDialog.show()
        }

        applyButton.setOnClickListener { toggleMarkdown() }
        backButton.setOnClickListener { finish() }
        uploadButton.setOnClickListener { post(id) }
    }
    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
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
            println("START")
            if (response.isSuccessful) {
                // 응답 받아서 처리
                val body = response.body()
                if (body != null) {
                    val responseStr = body.string()
                    println(responseStr)
                    val json = JSONObject(responseStr)
                    binding.editTitle.setText(json.getJSONObject("data").getString("title"))
                    binding.textDate.text = json.getJSONObject("data").getString("date")
                    binding.editText.setText(json.getJSONObject("data").getString("content"))
                }
            } else System.err.println("Error Occurred")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun post(id: Int){
        try {
            val title = binding.editTitle.text.toString()
            val content = binding.editText.text.toString()
            val date = binding.textDate.text.toString()

            if (title == "") {
                Toast.makeText(this, "제목을 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else if (content == "") {
                Toast.makeText(this, "게시글 본문을 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else if (date == "") {
                Toast.makeText(this, "날짜를 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                return
            }

            val url = "http://gdsc-knu-til.herokuapp.com/posts/"+id

            val jsonObject = JSONObject()
            jsonObject.put("title", title)
            jsonObject.put("date", date)
            jsonObject.put("content", content)

            Log.d("test", jsonObject.toString())

            val client=OkHttpClient()
            client.interceptors().add(AuthInterceptor())

            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())
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

                    val intent= Intent(this, DetailContent::class.java)
                    intent.putExtra("Detail_ID", Integer.parseInt(responseStr))
                    ContextCompat.startActivity(this, intent, null)
                }
            } else System.err.println("Error Occurred")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class EditContent (val title : String, val date : String, val content : String)

    fun toggleMarkdown() {
        val markdownView = binding.markdownView
        val editText = binding.editText
        val applyButton = binding.applyButton

        if (viewMode) {
            editText.visibility = View.VISIBLE
            markdownView.visibility = View.GONE
            applyButton.text = "미리보기"
        }
        else {
            editText.visibility = View.GONE
            markdownView.visibility = View.VISIBLE
            applyButton.text = "수정하기"

            markdownView.loadMarkdown(editText.text.toString())
        }

        viewMode = !viewMode
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}