package com.example.til

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.til.databinding.ActivityMainBinding
import com.example.til.jwt.AuthInterceptor
import com.example.til.jwt.AuthRequest
import com.example.til.jwt.Prefs
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")

    private var mBinding : ActivityMainBinding? = null
    private val binding get() = mBinding!!

    companion object{
        lateinit var prefs: Prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        // jwt Token 보관용
        prefs=Prefs(applicationContext)
        if (isLogin(prefs)) {
            val intent = Intent(this, CalendarFunc::class.java)
            startActivity(intent)

            finish()
        }

        login_submit_btn.setOnClickListener {
            if (login()) {
                val intent = Intent(this, CalendarFunc::class.java)
                startActivity(intent)

                finish()
            }
        }

        tosignupbtn.setOnClickListener {
            val intent=Intent(this, SignUpFunc::class.java)
            startActivity(intent)
        }
    }

    private fun isLogin(prefs: Prefs) : Boolean {
        if (prefs.token == null) {
            return false;
        }

        val client = OkHttpClient()
        client.interceptors().add(AuthInterceptor())

        val url = "http://gdsc-knu-til.herokuapp.com"
        val request = Request.Builder().url(url).get().build()

        val response = client.newCall(request).execute()
        if (response.code() == 401) {
            return false
        }

        return true
    }

    private fun login() : Boolean {
        val account = binding.loginEditAcconut.text.toString()
        val password = binding.loginEditPassword.text.toString()

        try {
            val url = "http://gdsc-knu-til.herokuapp.com/auth/login"
            val client = OkHttpClient()

            val mapper  = jacksonObjectMapper()
            val json = mapper.writeValueAsString(AuthRequest(account, password))

            val body = RequestBody.create(JSON, json)
            val builder = Request.Builder().url(url).post(body)
            val request = builder.build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                prefs.token = response.body().string()

                response.body().close()
                return true
            } else if (response.code() == 401) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.loginEditAcconut.windowToken, 0)
                Toast.makeText(this, "이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
            } else{
                System.err.println("응답 코드 : " + response.code())
                System.err.println(response.body().string())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}