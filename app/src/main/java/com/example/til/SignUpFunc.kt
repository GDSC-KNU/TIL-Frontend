package com.example.til

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.sign_up.*

class SignUpFunc  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        tocalendarbtn.setOnClickListener {
            val intent= Intent(this, CalendarFunc::class.java)
            startActivity(intent)
        }

        tologinbtn.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}