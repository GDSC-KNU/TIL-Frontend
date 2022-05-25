package com.gdsc.til_frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.view.View
import us.feras.mdv.MarkdownView
import com.gdsc.til_frontend.databinding.WritingBinding

class WritingFunc : AppCompatActivity() {
	private var mBinding : WritingBinding? = null
	private val binding get() = mBinding!!
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//setContentView(R.layout.activity_main)
		mBinding = WritingBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val markdownView = binding.markdownView
		var uploadText :String
		var uploadTitle: String
		markdownView.loadMarkdown("## Input Markdown")
		binding.applyButton.setOnClickListener {
			uploadText = binding.editText.text.toString()
			uploadTitle = binding.editTitle.text.toString()
			markdownView.loadMarkdown(uploadText)
			Log.d("markdown", "markdown uploaded")
		}
		binding.backButton.setOnClickListener {
			finish()
		}
		binding.uploadButton.setOnClickListener {

		}

	}
	override fun onDestroy() {
		mBinding = null
		super.onDestroy()
	}

}
