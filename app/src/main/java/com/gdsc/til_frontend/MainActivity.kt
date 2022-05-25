package com.gdsc.til_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.view.View
//import us.feras.mdv.MarkdownView
import com.gdsc.til_frontend.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private var mBinding : ActivityMainBinding? = null
	private val binding get() = mBinding!!
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mBinding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.moveButton.setOnClickListener {
			val intent = Intent(this, WritingFunc::class.java)
			startActivity(intent)
		}
	}
	override fun onDestroy() {
		mBinding = null
		super.onDestroy()
	}

}
