package com.example.til

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.calendar.*
import kotlinx.android.synthetic.main.list.*
import kotlinx.android.synthetic.main.list.mypage
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


        list_search.setOnKeyListener {v, keyCode, event ->
            val imm=this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(this, list_search.text, Toast.LENGTH_LONG).show()
                imm.hideSoftInputFromWindow(list_search.windowToken, 0)

                var list = search_dataAdd(list_search.text.toString())
                val adapter = RecycleAdapter(list)
                recycleView.adapter = adapter
                return@setOnKeyListener true
            }
            false
        }
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

    fun search_dataAdd(search_item : String): ArrayList<Data> {
        val list = ArrayList<Data>()
        list.add(Data(1, "title", "2022-05-08", "hello"))
        list.add(Data(2, "title2", "2022-05-09","hello2"))
        list.add(Data(3, "title3", "2022-05-10","hello3"))

        return list
    }
}