package com.example.til

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.til.databinding.WritingBinding
import com.example.til.jwt.AuthInterceptor
import com.squareup.okhttp.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class WritingFunc : AppCompatActivity() {
    private var mBinding : WritingBinding? = null
    private val binding get() = mBinding!!

    private var viewMode = false

    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = WritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        binding.markdownView.setBackgroundColor(0)

        binding.btnDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                binding.textDate.text = getString(R.string.send_date_format, year, month + 1, day)
            }, year, month, day)

            datePickerDialog.show()
        }
        binding.applyButton.setOnClickListener { toggleMarkdown() }
        binding.backButton.setOnClickListener { finish() }
        binding.uploadButton.setOnClickListener { sendPost() }

    }
    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendPost() {
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

        val JSON = MediaType.parse("application/json; charset=utf-8")
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
        Log.d("upload post", "request Body: $jsonInput")

        val builder = Request.Builder().url(url).post(body)
        val request = builder.build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val postId = response.body().string()
            val intent= Intent(this, DetailContent::class.java)
            intent.putExtra("Detail_ID", Integer.parseInt(postId))
            ContextCompat.startActivity(this, intent, null)
            finish()
            return
        }
        else {
            Log.e("upload post", "code: ${response.code()}, message: ${response.body().string()}")
        }
    }

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
