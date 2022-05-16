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

        changetoc.setOnClickListener {
            val intent = Intent(this, CalendarFunc::class.java)
            startActivity(intent)
        }

        mypage.setOnClickListener {
            val intent = Intent(this, MyPageFunc::class.java)
            startActivity(intent)
        }

        var list = dataAdd()
        val adapter = RecycleAdapter(list)
        recycleView.adapter = adapter
    }

    fun dataAdd(): ArrayList<Data> {
        val list = ArrayList<Data>()
        list.add(Data(1, "title", "2022-05-08", "hello"))
        list.add(Data(2, "title2", "2022-05-09","hello2"))
        list.add(Data(3, "title3", "2022-05-10","hello3"))
        list.add(Data(4, "title4", "2022-05-10","hello4"))
        list.add(Data(5, "title5", "2022-05-16","hello5"))

        return list
    }
}