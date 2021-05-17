package com.example.teamproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var customizingDBHelper: CustomizingDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCustomizingDB()
        init()
    }

    private fun initCustomizingDB() {
        customizingDBHelper = CustomizingDBHelper(this)
        val option = arrayListOf<String>("saturday","sunday","today","date","select","color","format","background")
        val setting = arrayListOf<String>("off","off","off","on","GRAY","off","#FFFFFF","#FFFFFF")
        for(i in 0 until option.size){
            var data = CustomizingData(option[i],setting[i])
            customizingDBHelper.insertCustomizing(data)
        }
    }

    private fun init() {
        customizingDBHelper = CustomizingDBHelper(this)

        val btn = findViewById<Button>(R.id.buttonMain)

        btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
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

        if(customizingDBHelper.findCustomizing("saturday")=="on"){
            calendar.addDecorator(CustomizingSaturdayOn())
        }else{
            calendar.addDecorator(CustomizingSaturdayOff())
        }
        if(customizingDBHelper.findCustomizing("sunday")=="on"){
            calendar.addDecorator(CustomizingSundayOn())
        }else{
            calendar.addDecorator(CustomizingSundayOff())
        }
        if(customizingDBHelper.findCustomizing("today")=="on"){
            calendar.addDecorator(CustomizingTodayOn())
        }else{
            calendar.removeDecorators()
            if(customizingDBHelper.findCustomizing("saturday")=="on"){
                calendar.addDecorator(CustomizingSaturdayOn())
            }else{
                calendar.addDecorator(CustomizingSaturdayOff())
            }
            if(customizingDBHelper.findCustomizing("sunday")=="on"){
                calendar.addDecorator(CustomizingSundayOn())
            }else{
                calendar.addDecorator(CustomizingSundayOff())
            }
        }
        calendar.topbarVisible = customizingDBHelper.findCustomizing("date")=="on"
        if(customizingDBHelper.findCustomizing("format")=="on"){
            calendar.setTitleFormatter(TitleFormatter {
                SimpleDateFormat("yyyy MMMM", Locale.KOREA).format(it.date)
            })
        }else{
            calendar.setTitleFormatter(null)
        }
        when(customizingDBHelper.findCustomizing("color")){
            "#FFFFFF"->calendar.setBackgroundColor(Color.parseColor("#FFFFFF"))
            "#FBE4E4"->calendar.setBackgroundColor(Color.parseColor("#FBE4E4"))
            "#DDF0F3"->calendar.setBackgroundColor(Color.parseColor("#DDF0F3"))
            "#D1F3D2"->calendar.setBackgroundColor(Color.parseColor("#D1F3D2"))
            "#F8F5DA"->calendar.setBackgroundColor(Color.parseColor("#F8F5DA"))
            "#E7DDFA"->calendar.setBackgroundColor(Color.parseColor("#E7DDFA"))
        }
        when(customizingDBHelper.findCustomizing("select")){
            "GRAY"-> calendar.setSelectionColor(Color.GRAY)
            "WHITE"-> calendar.setSelectionColor(Color.WHITE)
            "RED"-> calendar.setSelectionColor(Color.RED)
            "MAGENTA"-> calendar.setSelectionColor(Color.MAGENTA)
            "YELLOW"-> calendar.setSelectionColor(Color.YELLOW)
            "GREEN"-> calendar.setSelectionColor(Color.GREEN)
            "BLUE"-> calendar.setSelectionColor(Color.BLUE)
            "CYAN"-> calendar.setSelectionColor(Color.CYAN)
        }
        when(customizingDBHelper.findCustomizing("background")){
            "#FFFFFF"->constraint.setBackgroundColor(Color.parseColor("#FFFFFF"))
            "#FBE4E4"->constraint.setBackgroundColor(Color.parseColor("#FBE4E4"))
            "#DDF0F3"->constraint.setBackgroundColor(Color.parseColor("#DDF0F3"))
            "#D1F3D2"->constraint.setBackgroundColor(Color.parseColor("#D1F3D2"))
            "#F8F5DA"->constraint.setBackgroundColor(Color.parseColor("#F8F5DA"))
            "#E7DDFA"->constraint.setBackgroundColor(Color.parseColor("#E7DDFA"))
        }
    }
}