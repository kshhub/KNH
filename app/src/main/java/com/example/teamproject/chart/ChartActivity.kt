package com.example.teamproject.chart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.ActivityChartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class ChartActivity : AppCompatActivity() {
    lateinit var chartbinding: ActivityChartBinding

    val entries = arrayListOf<Entry>()
    val entriset = arrayListOf<Entry>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chartbinding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(chartbinding.root)
        init()
        setChart()
        initrecycler()
    }

    private fun initrecycler() {
        val datas = mutableListOf<ExerciseData>()
        var exerciseAdapter = ChartEXAdapter(this)
        val exerRecyclerView = findViewById<RecyclerView>(R.id.exerrecyclerView)
        exerRecyclerView.adapter = exerciseAdapter
        datas.apply {
            add(ExerciseData(exname = "수영", extime = 30, exkcal = 56))
            exerciseAdapter.datas = datas
            exerciseAdapter.notifyDataSetChanged()
        }

        val datast = mutableListOf<EatingData>()
        val eatAdapter = ChartETAdapter(this)
        val eatRecyclerView = findViewById<RecyclerView>(R.id.eatrecyclerView)
        eatRecyclerView.adapter = eatAdapter
        datast.apply {
            add(EatingData(ename = "햇반", ekcal = 190))
            eatAdapter.datas = datast
            eatAdapter.notifyDataSetChanged()
        }
    }

    private fun init() {
        val bestset: TextView = findViewById(R.id.bestset)
        val setpercent: TextView = findViewById(R.id.setpercent)
        val todaydate: TextView = findViewById(R.id.dateshowtext)
        val bartext: TextView = findViewById(R.id.bardatetext)
        bestset.text = "체중 감소"
        setpercent.text = "NULL"
        bartext.text = "오늘 하루"
        if (intent.hasExtra("day")) {
            val setdtdt = intent.getStringExtra("date")
            val setdy = IntRange(19, 20)
            val setmt = IntRange(17, 17)
            val setyr = IntRange(12, 15)
            todaydate.text = "0000/00/00"
            if (setdtdt != null) {
                Log.d("DATE", setdtdt)
                var helpsetdate = setdtdt.slice(setmt).toInt()
                helpsetdate = helpsetdate + 1
                val setdatestr =
                    setdtdt.slice(setyr) + "/" + helpsetdate.toString() + "/" + setdtdt.slice(setdy)
                todaydate.text = setdatestr
            }
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
                entries.clear()
                entriset.clear()
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
                    for (i in 0..seekBar.progress) {
                        val `val` = (Math.random() * 10).toFloat()
                        entries.add(Entry(i.toFloat(), `val`))
                    }
                    for (i in 0..seekBar.progress) {
                        val `val` = (Math.random() * 10).toFloat()
                        entriset.add(Entry(i.toFloat(), `val`))
                    }
                } else {
                    when (seekBar.progress) {
                        8 -> {
                            for (i in 0..14) {
                                val `val` = (Math.random() * 10).toFloat()
                                entries.add(Entry(i.toFloat(), `val`))
                            }
                            for (i in 0..14) {
                                val `val` = (Math.random() * 10).toFloat()
                                entriset.add(Entry(i.toFloat(), `val`))
                            }
                        }
                        9 -> {
                            for (i in 0..30) {
                                val `val` = (Math.random() * 10).toFloat()
                                entries.add(Entry(i.toFloat(), `val`))
                            }
                            for (i in 0..30) {
                                val `val` = (Math.random() * 10).toFloat()
                                entriset.add(Entry(i.toFloat(), `val`))
                            }
                        }
                    }
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