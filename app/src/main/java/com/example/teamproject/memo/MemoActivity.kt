package com.example.teamproject.memo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.teamproject.R
import com.example.teamproject.custom.CustomDBHelper
import com.example.teamproject.database.AppDataBase
import com.example.teamproject.database.Memo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        val cancelBtn = findViewById<Button>(R.id.cancelBtn)
        val submitBtn = findViewById<Button>(R.id.subminBtn)
        val contentEdit = findViewById<EditText>(R.id.contentEdit)
        val titleEdit = findViewById<EditText>(R.id.titleEdit)
        val intent = intent
        val title = intent.getStringExtra("title").toString()
        val date = intent.getStringExtra("date").toString()
        val content = intent.getStringExtra("content").toString()
        val isMemo = intent.getStringExtra("isMemo").toBoolean()

        cancelBtn.setOnClickListener {
            finish()
        }

        submitBtn.setOnClickListener {
            //Log.d("co before","co before")
            CoroutineScope(Dispatchers.IO).launch {
                //Log.d("co after", "co after")
                val memo = Memo(date, titleEdit.text.toString(), contentEdit.text.toString())
                if(!isMemo)
                    AppDataBase.getInstance(applicationContext).memoDao().insert(memo)
                else
                    AppDataBase.getInstance(applicationContext).memoDao().updateMemo(memo)
                finish()
            }
        }

        titleEdit.setText(title)
        contentEdit.setText(content)
        dateText.text = date
        customizing()
    }

    private fun customizing() {
        val constraint2 = findViewById<ConstraintLayout>(R.id.memoConstraint)
        val titleEdit = findViewById<EditText>(R.id.titleEdit)
        val contentEdit = findViewById<EditText>(R.id.contentEdit)
        val dateText = findViewById<TextView>(R.id.dateText)
        val colorStr = customDBHelper.findCustomizing("background")
        dateText.setBackgroundColor(Color.parseColor(colorStr))
        val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(colorStr)
        val strColor = String.format("#%06X", (0xFFFFFF and intColor))
        dateText.setTextColor(Color.parseColor(strColor))
        customBackgroundColor(colorStr, constraint2)
        customTextViewColor(customDBHelper.findCustomizing("memo"), titleEdit)
        customTextViewColor(customDBHelper.findCustomizing("memo"), contentEdit)
    }

    private fun customBackgroundColor(str: String, const: ConstraintLayout) {
        const.setBackgroundColor(Color.parseColor(str))
    }

    private fun customTextViewColor(str: String, textView: TextView) {
        when (str) {
            "#FFFFFF" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_ffffff
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#FBE4E4" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_fbe4e4
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#E3F6F8" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_e3f6f8
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#D1F3D2" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_d1f3d2
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#F8F5DA" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_f8f5da
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#E7DDFA" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_e7ddfa
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = "#" + Integer.toHexString(intColor)
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#FF9800" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_ff9800
                )
                val intColor: Int = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#FFEB3B" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_ffeb3b
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#673AB7" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_673ab7
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        customizing()
    }
}