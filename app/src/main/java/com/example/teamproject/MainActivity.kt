package com.example.teamproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.teamproject.custom.CustomDBHelper
import com.example.teamproject.custom.CustomData
import com.example.teamproject.custom.CustomDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var customDBHelper: CustomDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCustomizingDB()
        init()
    }

    private fun initCustomizingDB() {
        customDBHelper = CustomDBHelper(this)
        val coption = arrayListOf<String>("saturday","sunday","today","date", "color","select",
            "format","background","memo","record")
        val csetting = arrayListOf<String>("off","off","off","on","#FFFFFF","GRAY",
            "off","#FFFFFF","#FFFFFF","#FFFFFF")
        for(i in 0 until coption.size){
            var cdata = CustomData(coption[i],csetting[i])
            customDBHelper.insertCustomizing(cdata)
        }
    }

    private fun init() {
        customDBHelper = CustomDBHelper(this)

        val btn = findViewById<Button>(R.id.settingBtn)
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarViewMain)
        val today = CalendarDay.today()

        btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        calendarView.setOnDateChangedListener { _, date, _ ->
            Log.d("date_selected_test", date.date.toString())
        }
        customizing()
    }

    override fun onRestart() {
        super.onRestart()
        customizing()
    }

    private fun customizing(){

        val constraint = findViewById<ConstraintLayout>(R.id.constraintLayoutMain)
        val calendar = findViewById<MaterialCalendarView>(R.id.calendarViewMain)
        val memo = findViewById<TextView>(R.id.memoView)
        val record = findViewById<TextView>(R.id.dietView)

        if(customDBHelper.findCustomizing("saturday")=="on"){
            calendar.addDecorator(CustomDecorator("saturday","on"))
        }else{
            calendar.addDecorator(CustomDecorator("saturday","off"))
        }
        if(customDBHelper.findCustomizing("sunday")=="on"){
            calendar.addDecorator(CustomDecorator("sunday","on"))
        }else{
            calendar.addDecorator(CustomDecorator("sunday","off"))
        }
        if(customDBHelper.findCustomizing("today")=="on"){
            calendar.addDecorator(CustomDecorator("today","on"))
        }else{
            calendar.removeDecorators()
            if(customDBHelper.findCustomizing("saturday")=="on"){
                calendar.addDecorator(CustomDecorator("saturday","on"))
            }else{
                calendar.addDecorator(CustomDecorator("saturday","off"))
            }
            if(customDBHelper.findCustomizing("sunday")=="on"){
                calendar.addDecorator(CustomDecorator("sunday","on"))
            }else{
                calendar.addDecorator(CustomDecorator("sunday","off"))
            }
        }
        calendar.topbarVisible = customDBHelper.findCustomizing("date")=="on"
        if(customDBHelper.findCustomizing("format")=="on"){
            calendar.setTitleFormatter(TitleFormatter {
                SimpleDateFormat("yyyy MMMM", Locale.KOREA).format(it.date)
            })
        }else{
            calendar.setTitleFormatter(null)
        }
        customCalendarColor(customDBHelper.findCustomizing("color"), calendar)
        customCalendarSelectColor(customDBHelper.findCustomizing("select"), calendar)
        customBackgroundColor(customDBHelper.findCustomizing("background"), constraint)
        customTextViewColor(customDBHelper.findCustomizing("memo"), memo)
        customTextViewColor(customDBHelper.findCustomizing("record"), record)
    }

    private fun customBackgroundColor(str: String, const:ConstraintLayout){
        when(str){
            "#FFFFFF"->const.setBackgroundColor(Color.parseColor("#FFFFFF"))
            "#FBE4E4"->const.setBackgroundColor(Color.parseColor("#FBE4E4"))
            "#DDF0F3"->const.setBackgroundColor(Color.parseColor("#DDF0F3"))
            "#D1F3D2"->const.setBackgroundColor(Color.parseColor("#D1F3D2"))
            "#F8F5DA"->const.setBackgroundColor(Color.parseColor("#F8F5DA"))
            "#E7DDFA"->const.setBackgroundColor(Color.parseColor("#E7DDFA"))
            "#F44336"->const.setBackgroundColor(Color.parseColor("#F44336"))
            "#FF9800"->const.setBackgroundColor(Color.parseColor("#FF9800"))
            "#FFEB3B"->const.setBackgroundColor(Color.parseColor("#FFEB3B"))
            "#673AB7"->const.setBackgroundColor(Color.parseColor("#673AB7"))
        }
    }

    private fun customCalendarSelectColor(str: String, calendar: MaterialCalendarView){
        when(str){
            "GRAY"-> calendar.setSelectionColor(Color.GRAY)
            "WHITE"-> calendar.setSelectionColor(Color.WHITE)
            "RED"-> calendar.setSelectionColor(Color.RED)
            "MAGENTA"-> calendar.setSelectionColor(Color.MAGENTA)
            "YELLOW"-> calendar.setSelectionColor(Color.YELLOW)
            "GREEN"-> calendar.setSelectionColor(Color.GREEN)
            "BLUE"-> calendar.setSelectionColor(Color.BLUE)
            "CYAN"-> calendar.setSelectionColor(Color.CYAN)
        }
    }

    private fun customCalendarColor(str: String, calendar: MaterialCalendarView){
        when(str){
            "#FFFFFF"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ffffff))
            "#FBE4E4"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_fbe4e4))
            "#DDF0F3"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ddf0f3))
            "#D1F3D2"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_d1f3d2))
            "#F8F5DA"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_f8f5da))
            "#E7DDFA"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_e7ddfa))
            "#F44336"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_f44336))
            "#FF9800"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ff9800))
            "#FFEB3B"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_ffeb3b))
            "#673AB7"->calendar.setBackground(ContextCompat.getDrawable(this, R.drawable.edgesmooth_673ab7))
        }
    }

    private fun customTextViewColor(str: String, textView: TextView){
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
}