package com.example.til

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.list.*

class ListFunc: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)

        changetoc.setOnClickListener({
            val intent = Intent(this, CalendarFunc::class.java)
            startActivity(intent)
        })

        mypage.setOnClickListener({
            val intent = Intent(this, MyPageFunc::class.java)
            startActivity(intent)
        })
    }
}