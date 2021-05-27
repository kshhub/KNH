package com.example.teamproject

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.teamproject.custom.CustomDBHelper

class MemoActivity : AppCompatActivity() {
    lateinit var customDBHelper: CustomDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
        init()
    }
    private fun init() {
        customDBHelper = CustomDBHelper(this)

        val dateText = findViewById<TextView>(R.id.dateText)

        val intent = intent
        val date = intent.getStringExtra("date").toString()
        dateText.text = date
        customizing()
    }

    private fun customizing() {
        val constraint2 = findViewById<ConstraintLayout>(R.id.memoConstraint)
        val titleEdit = findViewById<EditText>(R.id.titleEdit)
        val contentEdit = findViewById<EditText>(R.id.contentEdit)
        val dateText = findViewById<TextView>(R.id.dateText)
        dateText.setBackgroundColor(Color.parseColor("#FFFFFF"))
        customBackgroundColor(customDBHelper.findCustomizing("background"), constraint2)
        customTextViewColor(customDBHelper.findCustomizing("memo"), titleEdit)
        customTextViewColor(customDBHelper.findCustomizing("memo"), contentEdit)
    }

    private fun customBackgroundColor(str: String, const:ConstraintLayout){
        const.setBackgroundColor(Color.parseColor(str))
    }
    private fun customTextViewColor(str: String, textView: EditText){
        when(str){
            "#FFFFFF"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ffffff))
            "#FBE4E4"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_fbe4e4))
            "#DDF0F3"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ddf0f3))
            "#D1F3D2"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_d1f3d2))
            "#F8F5DA"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_f8f5da))
            "#E7DDFA"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_e7ddfa))
            "#F44336"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_f44336))
            "#FF9800"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ff9800))
            "#FFEB3B"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ffeb3b))
            "#673AB7"->textView.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_673ab7))
        }
    }
    override fun onRestart() {
        super.onRestart()
        customizing()
    }

}