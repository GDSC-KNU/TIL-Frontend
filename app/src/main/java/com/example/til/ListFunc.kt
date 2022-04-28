package com.example.til

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.list.*
import java.util.ArrayList

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

        var list = dataAdd()
        val adapter = RecycleAdapter(list)
        recycleView.adapter = adapter
    }

    fun dataAdd(): ArrayList<Data> {
        val list = ArrayList<Data>()
        list.add(Data("hi", "hello"))
        list.add(Data("hi2", "hello2"))
        list.add(Data("hi3", "hello3"))
        list.add(Data("hi4", "hello4"))
        list.add(Data("hi5", "hello5"))

        return list
    }
}