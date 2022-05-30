package com.example.til

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.example.til.databinding.SignUpBinding
import com.example.til.jwt.AuthRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import kotlinx.android.synthetic.main.sign_up.*


class SignUpFunc : AppCompatActivity() {

    val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")

    private var mBinding : SignUpBinding? = null
    private val binding get() = mBinding!!

    private val checkLoginTextView: TextView by lazy {
        findViewById(R.id.tologinbtn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
        mBinding = SignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        signup_submit_btn.setOnClickListener {
            signup()
        }

        tologinbtn.setOnClickListener {
            finish()
        }

        val context = SpannableStringBuilder("계정이 이미 있나요? 로그인")
        context.apply {
            // 커스텀 컬러를 넣는 과정에서 에러가 생겨서 우선 색상코드로 대체함
            setSpan(ForegroundColorSpan(Color.rgb(248, 95, 106)), 12, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        checkLoginTextView.text = context
    }

    private fun signup() {
        val account = binding.signupEditAccount.text.toString()
        val password = binding.signupEditPassword.text.toString()

        try {
            val url = "http://gdsc-knu-til.herokuapp.com/auth/signup"
            val client = OkHttpClient()

            val mapper  = jacksonObjectMapper()
            val json = mapper.writeValueAsString(AuthRequest(account, password))

            val body = RequestBody.create(JSON, json)
            val builder = Request.Builder().url(url).post(body)
            val request = builder.build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                finish()
            } else if (response.code() == 409) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.signupEditAccount.windowToken, 0)
                Toast.makeText(this, "이미 해당 계정이 존재합니다.", Toast.LENGTH_SHORT).show()
            } else{
                System.err.println("응답 코드 : " + response.code())
                System.err.println(response.body().string())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}