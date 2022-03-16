package com.example.til

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.calendar.*

class CalendarFunc : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        floatingActionButton.setOnClickListener({
            val intent = Intent(this, WritingFunc::class.java)
            startActivity(intent)
        })
    }
}