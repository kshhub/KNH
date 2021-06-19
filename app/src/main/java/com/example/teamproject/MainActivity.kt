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
import com.example.teamproject.chart.ChartActivity
import com.example.teamproject.custom.CustomDBHelper
import com.example.teamproject.custom.CustomData
import com.example.teamproject.custom.CustomDecorator
import com.example.teamproject.database.AppDataBase
import com.example.teamproject.memo.MemoActivity
import com.example.teamproject.nutrition.NutritionFactsDBHelper
import com.example.teamproject.nutrition.RecordActivity
import com.example.teamproject.popup.PopupActivity
import com.example.teamproject.setting.SettingActivity
import com.example.teamproject.userinfo.UserInfoDBHelper
import com.example.teamproject.userinfo.UserInfoData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.*
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {
    val DEFAULT_DATE = "Tue Jun 01 00:00:00 GMT+09:00 1990"
    var isInMemo = false
    var memoContent = ""
    var currentDate = LocalDate.now()
    lateinit var customDBHelper: CustomDBHelper
    lateinit var userInfoDBHelper: UserInfoDBHelper
    lateinit var exerciseDBHelper: ExerciseDBHelper
    lateinit var nutritionFactsDBHelper: NutritionFactsDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCustomizingDB()
        initUserInfoDB()
        initDB()
        init()
    }

    private fun initCustomizingDB() {
        val dbfile = getDatabasePath("customizingdb.db")
        if (!dbfile.exists()) {
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
    }

    private fun initUserInfoDB() {
        val dbfile = getDatabasePath("userInfodb.db")
        if (!dbfile.exists()) {
            userInfoDBHelper = UserInfoDBHelper(this)
            val uoption = arrayListOf("age", "gender", "height", "weight", "goal")
            val usetting = arrayListOf("default", "default", "default", "default", "default")
            for (i in 0 until uoption.size) {
                val udata = UserInfoData(uoption[i], usetting[i])
                userInfoDBHelper.insertUserInfo(udata)
            }
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initDB() {
        exerciseDBHelper = ExerciseDBHelper(this)

        val dbfile = this.getDatabasePath("exercise.db")

        if (!dbfile.parentFile.exists()) {
            dbfile.parentFile.mkdir()
        }

        if (!dbfile.exists()) {
            val file = resources.openRawResource(com.example.teamproject.R.raw.exercise)
            val fileSize = file.available()
            val buffer = ByteArray(fileSize)

            file.read(buffer)
            file.close()

            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }

        nutritionFactsDBHelper = NutritionFactsDBHelper(this)

        val dbFile = this.getDatabasePath("nutritionFacts.db")

        if (!dbFile.parentFile.exists()) {
            dbFile.parentFile.mkdir()
        }

        if (!dbFile.exists()) {
            val file = resources.openRawResource(R.raw.nutritionfacts)
            val fileSize = file.available()
            val buffer = ByteArray(fileSize)

            file.read(buffer)
            file.close()

            dbFile.createNewFile()
            val output = FileOutputStream(dbFile)
            output.write(buffer)
            output.close()
        }
    }

    private fun initBoolean(date: LocalDate): Array<Boolean> = runBlocking {
        //val today = LocalDate.now().toString()
        var isMemo = false
        var isDiet = false
        val job = CoroutineScope(Dispatchers.IO).launch {
            val memoData = AppDataBase.getInstance(applicationContext).memoDao().getAllMemo()
            for (memo in memoData) {
                Log.d("init boolean test", memo.date)
                if (memo.date == date.toString()) {
                    isMemo = true
                    break
                }
            }
            val dietData = AppDataBase.getInstance(applicationContext).dietDao().getAllDiet()
            for (diet in dietData) {
                if (diet.date == date.toString()) {
                    isDiet = true
                    break
                }
            }
        }
        job.join()
        isInMemo = isMemo
        return@runBlocking arrayOf(isMemo, isDiet)
    }

    private fun init() {
        customDBHelper = CustomDBHelper(this)
        userInfoDBHelper = UserInfoDBHelper(this)

        val btn = findViewById<Button>(R.id.settingBtn)
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarViewMain)
        val today = CalendarDay.today()
        val memo = findViewById<Button>(R.id.memoBtn)
        val memoTextView = findViewById<TextView>(R.id.memoView)
        val dietView = findViewById<TextView>(R.id.dietView)
        val dietBtn = findViewById<Button>(R.id.button2)
        val exerciseBtn = findViewById<Button>(R.id.exerciseBtn)

        calendarView.selectedDate = today
        memo.setOnClickListener {
            val selectedDate = LocalDate.of(
                calendarView.selectedDate.year,
                calendarView.selectedDate.month + 1,
                calendarView.selectedDate.day
            )
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("title", memoTextView.text.toString())
            intent.putExtra("date", selectedDate.toString())
            intent.putExtra("content", memoContent)
            intent.putExtra("isMemo", isInMemo.toString())
            startActivity(intent)
        }

        memoTextView.movementMethod = ScrollingMovementMethod()
        memoTextView.setOnClickListener {
            val selectedDate = LocalDate.of(
                calendarView.selectedDate.year,
                calendarView.selectedDate.month + 1,
                calendarView.selectedDate.day
            )
            val intent = Intent(this, PopupActivity::class.java)
            intent.putExtra("title", memoTextView.text.toString())
            intent.putExtra("date", selectedDate.toString())
            intent.putExtra("content", memoContent)
            startActivity(intent)
        }

        dietView.movementMethod = ScrollingMovementMethod()
        dietView.setOnClickListener {
            val selectedDate = LocalDate.of(
                calendarView.selectedDate.year,
                calendarView.selectedDate.month + 1,
                calendarView.selectedDate.day
            )
            val intent = Intent(this, PopupActivity::class.java)
            intent.putExtra("title", "EXERCISE")
            intent.putExtra("date", selectedDate.toString())
            intent.putExtra("content", dietView.text.toString())
            startActivity(intent)
        }

        btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        calendarView.setOnDateChangedListener { _, date, _ ->
//            Log.d("date_selected_test", date.date.toString())
            val selectedDate = LocalDate.of(
                date.year,
                date.month + 1,
                date.day
            )
            currentDate = selectedDate
            Log.d("date_selected_test", selectedDate.toString())
            renewView(selectedDate)
        }
        val chartBtn = findViewById<Button>(R.id.chartbtn)

        chartBtn.setOnClickListener {
            val selectedDate = LocalDate.of(
                calendarView.selectedDate.year,
                calendarView.selectedDate.month + 1,
                calendarView.selectedDate.day
            )
            val intent = Intent(this, ChartActivity::class.java)
            intent.putExtra("day", today.day.toString())
            intent.putExtra("month", today.month.toString())
            intent.putExtra("year", today.year.toString())
            //intent.putExtra("date", calendarView.selectedDate.toString())
            intent.putExtra("date", selectedDate.toString())
            Log.d("A", userInfoDBHelper.findUserInfo("goal"))
            intent.putExtra("goal", userInfoDBHelper.findUserInfo("goal"))
            startActivity(intent)
        }

        dietBtn.setOnClickListener {
            val selectedDate = LocalDate.of(
                calendarView.selectedDate.year,
                calendarView.selectedDate.month + 1,
                calendarView.selectedDate.day
            )
            val intent = Intent(this, RecordActivity::class.java)
            intent.putExtra("type", "diet")
            intent.putExtra("date", selectedDate.toString())
            startActivity(intent)
        }

        exerciseBtn.setOnClickListener {
            val selectedDate = LocalDate.of(
                calendarView.selectedDate.year,
                calendarView.selectedDate.month + 1,
                calendarView.selectedDate.day
            )
            val intent = Intent(this, RecordActivity::class.java)
            intent.putExtra("type", "exercise")
            intent.putExtra("date", selectedDate.toString())
            startActivity(intent)
        }

        customizing()
        initView()
    }

    override fun onRestart() {
        super.onRestart()
        customizing()
        renewView(currentDate)
    }

    override fun onResume() {
        super.onResume()
        customizing()
        renewView(currentDate)
    }

    private fun renewView(selectedDate: LocalDate) {
        val memoTxt = findViewById<TextView>(R.id.memoView)
        val dietTxt = findViewById<TextView>(R.id.dietView)
        val bools = initBoolean(selectedDate)
        Log.d("renew_test", bools[0].toString())
        if (bools[0]) {
            CoroutineScope(Dispatchers.IO).launch {
                val memo =
                    AppDataBase.getInstance(applicationContext).memoDao()
                        .getMemoByDate(selectedDate.toString())
                val text = memo[memo.lastIndex].title
                memoTxt.text = text
                memoContent = memo[memo.lastIndex].content.toString()
            }
        } else {
            memoTxt.text = ""
            memoContent = ""
        }
        val exerciseList = exerciseDBHelper.getRecordList(selectedDate)
        if (exerciseList.size != 0) {
            var text = ""
            for (record in exerciseList) {
                text =
                    text + record.exercise.ename + "/" + record.etime + "분/" + record.totalKcal + "kcal\n"
            }
            dietTxt.text = text
        } else {
            dietTxt.text = ""
        }
    }


    private fun initView() {
        val memoTxt = findViewById<TextView>(R.id.memoView)
        val dietTxt = findViewById<TextView>(R.id.dietView)
        val today = LocalDate.now()
        val bools = initBoolean(today)
        Log.d("edit_test", bools[0].toString())
        if (bools[0]) {
            CoroutineScope(Dispatchers.IO).launch {
                val memo =
                    AppDataBase.getInstance(applicationContext).memoDao()
                        .getMemoByDate(today.toString())
                memoTxt.text = memo[memo.lastIndex].title
                memoContent = memo[memo.lastIndex].content.toString()
            }
        } else {
            memoTxt.text = ""
            memoContent = ""
        }
        val exerciseList = exerciseDBHelper.getRecordList(today)
        if (exerciseList.size != 0) {
            var text = ""
            for (record in exerciseList) {
                text =
                    text + record.exercise.ename + "/" + record.etime + "분/" + record.totalKcal + "kcal\n"
            }
            dietTxt.text = text
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
            calendar.addDecorator(CustomDecorator("saturday", "on", Color.BLUE))
        } else {
            calendar.addDecorator(CustomDecorator("saturday", "off", Color.BLACK))
        }
        if (customDBHelper.findCustomizing("sunday") == "on") {
            calendar.addDecorator(CustomDecorator("sunday", "on", Color.RED))
        } else {
            calendar.addDecorator(CustomDecorator("sunday", "off", Color.BLACK))
        }
        if (customDBHelper.findCustomizing("today") == "on") {
            calendar.addDecorator(CustomDecorator("today", "on", Color.GREEN))
        } else {
            calendar.removeDecorators()
            if (customDBHelper.findCustomizing("saturday") == "on") {
                calendar.addDecorator(CustomDecorator("saturday", "on", Color.BLUE))
            } else {
                calendar.addDecorator(CustomDecorator("saturday", "off", Color.BLACK))
            }
            if (customDBHelper.findCustomizing("sunday") == "on") {
                calendar.addDecorator(CustomDecorator("sunday", "on", Color.RED))
            } else {
                calendar.addDecorator(CustomDecorator("sunday", "off", Color.BLACK))
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
            "#E3F6F8" -> const.setBackgroundColor(Color.parseColor("#E3F6F8"))
            "#D1F3D2" -> const.setBackgroundColor(Color.parseColor("#D1F3D2"))
            "#F8F5DA" -> const.setBackgroundColor(Color.parseColor("#F8F5DA"))
            "#E7DDFA" -> const.setBackgroundColor(Color.parseColor("#E7DDFA"))
            "#FF9800" -> const.setBackgroundColor(Color.parseColor("#FF9800"))
            "#FFEB3B" -> const.setBackgroundColor(Color.parseColor("#FFEB3B"))
            "#673AB7" -> const.setBackgroundColor(Color.parseColor("#673AB7"))
        }
    }

    private fun customCalendarSelectColor(str: String, calendar: MaterialCalendarView) {
        when (str) {
            "GRAY" -> {
                calendar.selectionColor = Color.GRAY
                //calendar.addDecorator(CustomDecorator("today", "on", Color.WHITE))
            }
            "WHITE" -> {
                calendar.selectionColor = Color.WHITE
                //calendar.addDecorator(CustomDecorator("today", "on", Color.GRAY))
            }
            "RED" -> {
                calendar.selectionColor = Color.parseColor("#F68D83")
                //calendar.addDecorator(CustomDecorator("today", "on", Color.CYAN))
            }
            "MAGENTA" -> {
                calendar.selectionColor = Color.MAGENTA
                //calendar.addDecorator(CustomDecorator("today", "on", Color.GREEN))
            }
            "YELLOW" -> {
                calendar.selectionColor = Color.YELLOW
                //calendar.addDecorator(CustomDecorator("today", "on", Color.BLUE))
            }
            "GREEN" -> {
                calendar.selectionColor = Color.GREEN
                //calendar.addDecorator(CustomDecorator("today", "on", Color.MAGENTA))
            }
            "BLUE" -> {
                calendar.selectionColor = Color.parseColor("#00BCD4")
                //calendar.addDecorator(CustomDecorator("today", "on", Color.YELLOW))
            }
            "CYAN" -> {
                calendar.selectionColor = Color.CYAN
                //calendar.addDecorator(CustomDecorator("today", "on", Color.RED))
            }
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
            "#E3F6F8" -> calendar.background = ContextCompat.getDrawable(
                this,
                R.drawable.edgesmooth_e3f6f8
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
                val strColor = String.format("#%06X", (0xFFFFFF and intColor))
                textView.setTextColor(Color.parseColor(strColor))
            }
            "#FF9800" -> {
                textView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edgesmooth_ff9800
                )
                val intColor = Color.parseColor("#FFFFFF") - Color.parseColor(str)
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
}