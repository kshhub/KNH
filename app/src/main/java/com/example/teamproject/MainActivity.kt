package com.example.teamproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.teamproject.custom.CustomDBHelper
import com.example.teamproject.custom.CustomData
import com.example.teamproject.custom.CustomDecorator
import com.example.teamproject.database.AppDataBase
import com.example.teamproject.userinfo.UserInfoDBHelper
import com.example.teamproject.userinfo.UserInfoData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val DEFAULT_DATE = "Tue Jun 01 00:00:00 GMT+09:00 1990"
    lateinit var customDBHelper: CustomDBHelper
    lateinit var userInfoDBHelper: UserInfoDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCustomizingDB()
        initUserInfoDB()
        init()
    }

    private fun initCustomizingDB() {
        customDBHelper = CustomDBHelper(this)
        val coption = arrayListOf(
            "saturday", "sunday", "today", "date", "color", "select",
            "format", "background", "memo", "record"
        )
        val csetting = arrayListOf(
            "off", "off", "off", "on", "#FFFFFF", "GRAY",
            "off", "#FFFFFF", "#FFFFFF", "#FFFFFF"
        )
        for (i in 0 until coption.size) {
            val cdata = CustomData(coption[i], csetting[i])
            customDBHelper.insertCustomizing(cdata)
        }
    }

    private fun initUserInfoDB() {
        userInfoDBHelper = UserInfoDBHelper(this)
        val uoption = arrayListOf("age", "gender", "height", "weight", "goal")
        val usetting = arrayListOf("default", "default", "default", "default", "default")
        for (i in 0 until uoption.size) {
            val udata = UserInfoData(uoption[i], usetting[i])
            userInfoDBHelper.insertUserInfo(udata)
        }
    }

    private fun initBoolean(): Array<Boolean> = runBlocking {
        val today = CalendarDay.today().date.toString()
        var isMemo = false
        var isDiet = false
        val job = CoroutineScope(Dispatchers.IO).launch {
            val memoData = AppDataBase.getInstance(applicationContext).memoDao().getAllMemo()
            for (memo in memoData) {
                if (memo.date == today) {
                    isMemo = true
                    break
                }
            }
            val dietData = AppDataBase.getInstance(applicationContext).dietDao().getAllDiet()
            for (diet in dietData) {
                if (diet.date == today) {
                    isDiet = true
                    break
                }
            }
        }
        job.join()
        return@runBlocking arrayOf(isMemo, isDiet)
    }

    private fun init() {
        customDBHelper = CustomDBHelper(this)

        val btn = findViewById<Button>(R.id.settingBtn)
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarViewMain)
        val today = CalendarDay.today()
        val memo = findViewById<Button>(R.id.memoBtn)
        val memoTextView = findViewById<TextView>(R.id.memoView)
        calendarView.selectedDate = today
        memo.setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("date", today.date.toString())
            startActivity(intent)
        }

        memoTextView.movementMethod = ScrollingMovementMethod()

        memoTextView.setOnClickListener {
            val intent = Intent(this, PopupActivity::class.java)
            intent.putExtra("date", today.date.toString())
            Log.d("memo_test", memoTextView.text.toString())
            intent.putExtra("content", memoTextView.text.toString())
            startActivity(intent)
        }

        btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        calendarView.setOnDateChangedListener { _, date, _ ->
            Log.d("date_selected_test", date.date.toString())
        }
        val cbtn = findViewById<Button>(R.id.chartbtn)

        cbtn.setOnClickListener {
            val intent = Intent(this, ChartActivity::class.java)
            intent.putExtra("day", today.day.toString())
            intent.putExtra("month", today.month.toString())
            intent.putExtra("year", today.year.toString())
            intent.putExtra("date",calendarView.selectedDate.toString())
            startActivity(intent)
        }
        customizing()
        initView()
    }

    override fun onRestart() {
        super.onRestart()
        customizing()
        initView()
    }

    private fun initView() {
        val memoTxt = findViewById<TextView>(R.id.memoView)
        val dietTxt = findViewById<TextView>(R.id.dietView)
        val today = CalendarDay.today().date.toString()
        val bools = initBoolean()
        Log.d("edit_test", bools.toString())
        if (bools[0]) {
            CoroutineScope(Dispatchers.IO).launch {
                val memo =
                    AppDataBase.getInstance(applicationContext).memoDao().getMemoByDate(today)
                memoTxt.text = memo[memo.lastIndex].content
            }
        } else {
            memoTxt.text = ""
        }
        if (bools[1]) {
            CoroutineScope(Dispatchers.IO).launch {
                val diet =
                    AppDataBase.getInstance(applicationContext).dietDao().getDietByDate(today)
                dietTxt.text = diet[diet.lastIndex].diet
            }
        } else {
            dietTxt.text = ""
        }
    }

    private fun customizing() {
        val constraint = findViewById<ConstraintLayout>(R.id.constraint)
        val calendar = findViewById<MaterialCalendarView>(R.id.calendarViewMain)
        val memo = findViewById<TextView>(R.id.memoView)
        val record = findViewById<TextView>(R.id.dietView)

        if (customDBHelper.findCustomizing("saturday") == "on") {
            calendar.addDecorator(CustomDecorator("saturday", "on"))
        } else {
            calendar.addDecorator(CustomDecorator("saturday", "off"))
        }
        if (customDBHelper.findCustomizing("sunday") == "on") {
            calendar.addDecorator(CustomDecorator("sunday", "on"))
        } else {
            calendar.addDecorator(CustomDecorator("sunday", "off"))
        }
        if (customDBHelper.findCustomizing("today") == "on") {
            calendar.addDecorator(CustomDecorator("today", "on"))
        } else {
            calendar.removeDecorators()
            if (customDBHelper.findCustomizing("saturday") == "on") {
                calendar.addDecorator(CustomDecorator("saturday", "on"))
            } else {
                calendar.addDecorator(CustomDecorator("saturday", "off"))
            }
            if (customDBHelper.findCustomizing("sunday") == "on") {
                calendar.addDecorator(CustomDecorator("sunday", "on"))
            } else {
                calendar.addDecorator(CustomDecorator("sunday", "off"))
            }
        }
        calendar.topbarVisible = customDBHelper.findCustomizing("date") == "on"
        if (customDBHelper.findCustomizing("format") == "on") {
            calendar.setTitleFormatter {
                SimpleDateFormat("yyyy MMMM", Locale.KOREA).format(it.date)
            }
        } else {
            calendar.setTitleFormatter(null)
        }
        customCalendarColor(customDBHelper.findCustomizing("color"), calendar)
        customCalendarSelectColor(customDBHelper.findCustomizing("select"), calendar)
        customBackgroundColor(customDBHelper.findCustomizing("background"), constraint)
        customTextViewColor(customDBHelper.findCustomizing("memo"), memo)
        customTextViewColor(customDBHelper.findCustomizing("record"), record)
    }

    private fun customBackgroundColor(str: String, const: ConstraintLayout) {
        when (str) {
            "#FFFFFF" -> const.setBackgroundColor(Color.parseColor("#FFFFFF"))
            "#FBE4E4" -> const.setBackgroundColor(Color.parseColor("#FBE4E4"))
            "#DDF0F3" -> const.setBackgroundColor(Color.parseColor("#DDF0F3"))
            "#D1F3D2" -> const.setBackgroundColor(Color.parseColor("#D1F3D2"))
            "#F8F5DA" -> const.setBackgroundColor(Color.parseColor("#F8F5DA"))
            "#E7DDFA" -> const.setBackgroundColor(Color.parseColor("#E7DDFA"))
            "#F44336" -> const.setBackgroundColor(Color.parseColor("#F44336"))
            "#FF9800" -> const.setBackgroundColor(Color.parseColor("#FF9800"))
            "#FFEB3B" -> const.setBackgroundColor(Color.parseColor("#FFEB3B"))
            "#673AB7" -> const.setBackgroundColor(Color.parseColor("#673AB7"))
        }
    }

    private fun customCalendarSelectColor(str: String, calendar: MaterialCalendarView) {
        when (str) {
            "GRAY" -> calendar.selectionColor = Color.GRAY
            "WHITE" -> calendar.selectionColor = Color.WHITE
            "RED" -> calendar.selectionColor = Color.RED
            "MAGENTA" -> calendar.selectionColor = Color.MAGENTA
            "YELLOW" -> calendar.selectionColor = Color.YELLOW
            "GREEN" -> calendar.selectionColor = Color.GREEN
            "BLUE" -> calendar.selectionColor = Color.BLUE
            "CYAN" -> calendar.selectionColor = Color.CYAN
        }
    }

    private fun customCalendarColor(str: String, calendar: MaterialCalendarView) {
        when (str) {
            "#FFFFFF" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ffffff
            )
            "#FBE4E4" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_fbe4e4
            )
            "#DDF0F3" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ddf0f3
            )
            "#D1F3D2" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_d1f3d2
            )
            "#F8F5DA" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_f8f5da
            )
            "#E7DDFA" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_e7ddfa
            )
            "#F44336" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_f44336
            )
            "#FF9800" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ff9800
            )
            "#FFEB3B" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ffeb3b
            )
            "#673AB7" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_673ab7
            )
        }
    }

    private fun customTextViewColor(str: String, textView: TextView) {
        when (str) {
            "#FFFFFF" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ffffff
            )
            "#FBE4E4" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_fbe4e4
            )
            "#DDF0F3" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ddf0f3
            )
            "#D1F3D2" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_d1f3d2
            )
            "#F8F5DA" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_f8f5da
            )
            "#E7DDFA" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_e7ddfa
            )
            "#F44336" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_f44336
            )
            "#FF9800" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ff9800
            )
            "#FFEB3B" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_ffeb3b
            )
            "#673AB7" -> textView.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_673ab7
            )
        }
    }
}