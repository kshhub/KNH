package com.example.teamproject.popup

import android.app.Activity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.Window
import android.widget.TextView
import com.example.teamproject.R

class PopupActivity : Activity() {
    var txtText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_popup)
        init()
    }

    fun init() {
        val titleTxt = findViewById<TextView>(R.id.titleTxt)
        txtText = findViewById<View>(R.id.txtText) as TextView
        val intent = intent
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        txtText!!.text = date
        titleTxt.text = title
        val textView = findViewById<TextView>(R.id.contentText)
        val content = intent.getStringExtra("content")
        textView.movementMethod = ScrollingMovementMethod()
        textView.text = content
    }

    fun mOnClose(v: View?) {
        finish()
    }

    override fun onBackPressed() {
        return
    }
}