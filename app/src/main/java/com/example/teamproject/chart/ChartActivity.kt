package com.example.teamproject.chart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.ExerciseDBHelper
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.ActivityChartBinding
import com.example.teamproject.exercise.ExerciseRecord
import com.example.teamproject.exercise.NutritionFacts
import com.example.teamproject.exercise.NutritionFactsRecord
import com.example.teamproject.nutrition.NutritionFactsDBHelper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class ChartActivity : AppCompatActivity() {
    lateinit var chartbinding: ActivityChartBinding
    lateinit var exerciseDBHelper: ExerciseDBHelper
    lateinit var nutritionFactsDBHelper: NutritionFactsDBHelper

    val entries = arrayListOf<Entry>()
    val entriset = arrayListOf<Entry>()
    val useentries = arrayListOf<Int>()
    val gainentries = arrayListOf<Int>()
    var exdataall = mutableListOf<ArrayList<ExerciseRecord>>()
    var vardataall = mutableListOf<ArrayList<NutritionFactsRecord>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chartbinding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(chartbinding.root)
        init()
        setChart()
        initDBall()
    }
    private fun calcgraph(whr:Int){
        if(!useentries.isEmpty()&&!gainentries.isEmpty()) {
            entries.clear()
            entriset.clear()
            for (i in 0..whr) {
                val `val` = useentries[whr - i].toFloat()
                entries.add(Entry(i.toFloat(), `val`))
            }
            for (i in 0..whr) {
                val `val` = gainentries[whr - i].toFloat()
                entriset.add(Entry(i.toFloat(), `val`))
            }
            val set1 = LineDataSet(entries, "소비 칼로리")
            val set2 = LineDataSet(entriset, "섭취 칼로리")
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1)
            dataSets.add(set2)
            val data = LineData(dataSets)
            set1.color = Color.BLUE
            set1.setCircleColor(Color.BLACK)
            set2.color = Color.RED
            set2.setCircleColor(Color.YELLOW)
            try {
                val chart = findViewById<LineChart>(R.id.Chart)
                chart!!.data = data
                chart.invalidate()
                //refreshm();

            } catch (e: NullPointerException) {
                Log.d("ERR", "없")
            }
        }
    }
    private fun calcgoal(){
        val eatal = findViewById<TextView>(R.id.alleatkcal)
        val exal = findViewById<TextView>(R.id.allexkcal)
        val goalt = findViewById<TextView>(R.id.bestset)
        val achieveg = findViewById<TextView>(R.id.setpercent)
        if(eatal.text.toString()!="none"&&exal.text.toString()!="none"){
            val eatint = eatal.text.toString().toInt()
            val exint = exal.text.toString().toInt()
            if(eatint>exint+300){//먹보
                when(goalt.text.toString()){
                    "체중 감소"->{achieveg.setText(";ㅁ;")}
                    "체중 유지"->{achieveg.setText("-ㅅ-")}
                    "체중 증가"->{achieveg.setText("ㅇㅂㅇb")}
                    else->{Log.d("F","목표 오류")}
                }
            }
            else if(eatint<exint+300&&eatint>exint-300){//적당
                when(goalt.text.toString()){
                    "체중 감소"->{achieveg.setText("-ㅅ-")}
                    "체중 유지"->{achieveg.setText("ㅇㅂㅇb")}
                    "체중 증가"->{achieveg.setText("-ㅅ-")}
                    else->{Log.d("F","목표 오류")}
                }
            }
            else if(eatint<exint-300){//운동
                when(goalt.text.toString()){
                    "체중 감소"->{achieveg.setText("ㅇㅂㅇb")}
                    "체중 유지"->{achieveg.setText("-ㅅ-")}
                    "체중 증가"->{achieveg.setText(";ㅁ;")}
                    else->{Log.d("F","목표 오류")}
                }
            }
            else{
                Log.d("F","ATAL ERROR")
            }
        }
    }
    private fun initDBall(){
        val date = intent.getStringExtra("date")
        val cal: Calendar = Calendar.getInstance()
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val datedate : Date = df.parse(date)
        cal.setTime(datedate)
        System.out.println("current: " + df.format(cal.getTime()))
        val sD = LocalDate.parse(df.format(cal.getTime()),DateTimeFormatter.ISO_DATE)
        val exerciseData = exerciseDBHelper.getRecordList(sD)
        val nutritionData = nutritionFactsDBHelper.getRecordList(sD)
        exdataall.add(exerciseData)
        vardataall.add(nutritionData)
        for(i in 1..30){
            cal.add(Calendar.DATE, -1)
            val dD = LocalDate.parse(df.format(cal.getTime()),DateTimeFormatter.ISO_DATE)
            val edata = exerciseDBHelper.getRecordList(dD)
            val ndata = nutritionFactsDBHelper.getRecordList(dD)
            exdataall.add(edata)
            vardataall.add(ndata)
        }
        initRecycler(0)
    }
    private fun initRecycler(whr:Int) {
        val datas = mutableListOf<ExerciseData>()
        val exerciseAdapter = ChartEXAdapter(this)
        val exerciseRecyclerView = findViewById<RecyclerView>(R.id.exerrecyclerView)
        var exall = 0
        var exmini = 0
        var eatall = 0
        var eatmini = 0
        useentries.clear()
        gainentries.clear()
        exerciseRecyclerView.adapter = exerciseAdapter
        datas.apply {
            val alex = findViewById<TextView>(R.id.exalltext)
            for(i in 0..whr) {
                exmini = 0
                for (exData in exdataall[i]) {
                    add(ExerciseData(exData.exercise.ename, exData.etime, exData.totalKcal))
                    exmini = exmini + exData.totalKcal
                }
                useentries.add(exmini)
                exall = exall + exmini
            }
            val exal = findViewById<TextView>(R.id.allexkcal)
            exal.setText(exall.toString())
            calcgoal()
            alex.setText("총 소비 칼로리: " + exall.toString() + "kcal")
            exerciseAdapter.datas = datas
            calcgraph(whr)
            exerciseAdapter.notifyDataSetChanged()
        }

        val datast = mutableListOf<EatingData>()
        val eatAdapter = ChartETAdapter(this)
        val eatRecyclerView = findViewById<RecyclerView>(R.id.eatrecyclerView)
        eatRecyclerView.adapter = eatAdapter
        datast.apply {
            val aleat = findViewById<TextView>(R.id.eatalltext)
            for(i in 0..whr) {
                eatmini = 0
                for (nutData in vardataall[i]) {
                    val kcal = (nutData.nutritionFacts.kcal * nutData.intake / nutData.nutritionFacts.pergram).toInt()
                    eatmini = eatmini + kcal
                    add(EatingData(nutData.nutritionFacts.fname, kcal))
                }
                gainentries.add(eatmini)
                eatall = eatall + eatmini
            }
            val eatal = findViewById<TextView>(R.id.alleatkcal)
            eatal.setText(eatall.toString())
            calcgoal()
            aleat.setText("총 섭취 칼로리: " + eatall.toString() + "kcal")
            eatAdapter.datas = datast
            calcgraph(whr)
            eatAdapter.notifyDataSetChanged()
        }

    }

    private fun init() {
        exerciseDBHelper = ExerciseDBHelper(applicationContext)
        nutritionFactsDBHelper = NutritionFactsDBHelper(applicationContext)

        val bestset: TextView = findViewById(R.id.bestset)
        val todaydate: TextView = findViewById(R.id.dateshowtext)
        val bartext: TextView = findViewById(R.id.bardatetext)
        bestset.text = "설정 안함"
        if(intent.hasExtra("goal")){
            val gstst = intent.getStringExtra("goal")
            if(gstst!="default")bestset.text = gstst
        }
        bartext.text = "오늘 하루"
        if (intent.hasExtra("day")) {
            val setdtdt = intent.getStringExtra("date")
            val setdy = IntRange(19, 20)
            val setmt = IntRange(17, 17)
            val setyr = IntRange(12, 15)
            todaydate.text = setdtdt
        }
        val seekBar: SeekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (seekBar!!.progress) {
                    0 -> bartext.text = "오늘 하루"
                    1 -> bartext.text = "1일 전까지"
                    2 -> bartext.text = "2일 전까지"
                    3 -> bartext.text = "3일 전까지"
                    4 -> bartext.text = "4일 전까지"
                    5 -> bartext.text = "5일 전까지"
                    6 -> bartext.text = "6일 전까지"
                    7 -> bartext.text = "7일 전까지"
                    8 -> bartext.text = "14일 전까지"
                    9 -> bartext.text = "30일 전까지"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                try {
                    val chart = findViewById<LineChart>(R.id.Chart)
                    if (chart != null) {
                        when (seekBar!!.progress) {
                            0 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(1, true)
                                        axisMinimum = 0f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            1 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(2, true)
                                        axisMinimum = 0f
                                        axisMaximum = 1f
                                    }
                                }
                            }
                            2 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(3, true)
                                        axisMinimum = 0f
                                        axisMaximum = 2f
                                    }
                                }
                            }
                            3 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(4, true)
                                        axisMinimum = 0f
                                        axisMaximum = 3f
                                    }
                                }
                            }
                            4 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(5, true)
                                        axisMinimum = 0f
                                        axisMaximum = 4f
                                    }
                                }
                            }
                            5 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(6, true)
                                        axisMinimum = 0f
                                        axisMaximum = 5f
                                    }
                                }
                            }
                            6 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(7, true)
                                        axisMinimum = 0f
                                        axisMaximum = 6f
                                    }
                                }
                            }
                            7 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(8, true)
                                        axisMinimum = 0f
                                        axisMaximum = 7f
                                    }
                                }
                            }
                            8 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(15, true)
                                        axisMinimum = 0f
                                        axisMaximum = 14f
                                    }
                                }
                            }
                            9 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(31, true)
                                        axisMinimum = 0f
                                        axisMaximum = 30f
                                    }
                                }
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    Log.d("ERR", "없")
                }
                if (seekBar!!.progress < 8) {
                    initRecycler(seekBar.progress)
                } else {
                    when (seekBar.progress) {
                        8 -> {
                            initRecycler(14)
                        }
                        9 -> {
                            initRecycler(30)
                        }
                    }
                }
            }
        })
        try {
            val rtbtn = findViewById<Button>(R.id.chartendbtn)
            rtbtn!!.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } catch (e: NullPointerException) {
            Log.d("확인", "없음")
        }
    }

    private fun setChart() {
        if (entries.isEmpty()) {
            entries.add(Entry(2f, 20f))
            entries.add(Entry(3f, 15f))
            entries.add(Entry(5f, 5f))
            entries.add(Entry(8f, 10f))
            entries.add(Entry(10f, 30f))
            entries.add(Entry(12f, 25f))
        }
        val set1 = LineDataSet(entries, "DataSet 1")
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)
        val data = LineData(dataSets)
        set1.color = Color.BLACK
        set1.setCircleColor(Color.BLACK)
        try {
            val chart = findViewById<LineChart>(R.id.Chart)
            if (chart != null) {
                chart.data = data
                chart.run {
                    setDrawGridBackground(false)
                    xAxis.run {
                        setDrawGridLines(false)
                    }
                }
            }
        } catch (e: NullPointerException) {
            Log.d("ERR", "없")
        }
    }
}