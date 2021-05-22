package com.example.teamproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.teamproject.calender.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
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
            val data = CustomizingData(option[i], setting[i])
            customizingDBHelper.insertCustomizing(data)
        }
    }

    private fun init() {
        customizingDBHelper = CustomizingDBHelper(this)

        val btn = findViewById<Button>(R.id.settingBtn)
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarViewMain)
        val today = CalendarDay.today()

        calendarView.selectedDate = today
        Log.d("date_test",calendarView.selectedDate.date.toString())

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
            calendar.setTitleFormatter {
                SimpleDateFormat("yyyy MMMM", Locale.KOREA).format(it.date)
            }
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
            "GRAY"-> calendar.selectionColor = Color.GRAY
            "WHITE"-> calendar.selectionColor = Color.WHITE
            "RED"-> calendar.selectionColor = Color.RED
            "MAGENTA"-> calendar.selectionColor = Color.MAGENTA
            "YELLOW"-> calendar.selectionColor = Color.YELLOW
            "GREEN"-> calendar.selectionColor = Color.GREEN
            "BLUE"-> calendar.selectionColor = Color.BLUE
            "CYAN"-> calendar.selectionColor = Color.CYAN
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