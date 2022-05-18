package com.example.til

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.TypedArrayUtils.getText
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.list.*
import java.util.ArrayList

class SearchFunc: AppCompatActivity() {
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

        val search_edit=""
        var list = searchList(search_edit)
        print(search_edit)
        val adapter = RecycleAdapter(list)
        recycleView.adapter = adapter
    }
}

fun searchList(search_edit : String): ArrayList<Data> {
    val list=ArrayList<Data>()
//    Toast.makeText(this, getText(), Toast.LENGTH_LONG).show()
    list.add(Data(1, "title", "2022-05-08", "hello"))
    list.add(Data(2, "title2", "2022-05-09","hello2"))
    list.add(Data(3, "title3", "2022-05-10","hello3"))

    return list
}