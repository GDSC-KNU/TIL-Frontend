package com.example.til

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.detail_page.*

class DetailContent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)

        val tmp=intent.getIntExtra("Detail_ID", 0)
        content_id.setText(tmp.toString())

        return_button.setOnClickListener() {
            finish()
        }
    }
}