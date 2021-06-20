package com.example.teamproject.chart

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
 import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.ExerciseDBHelper
import com.example.teamproject.MainActivity
import com.example.teamproject.R
import com.example.teamproject.databinding.ActivityChartBinding
import com.example.teamproject.exercise.ExerciseRecord
import com.example.teamproject.exercise.NutritionFactsRecord
import com.example.teamproject.nutrition.NutritionFactsDBHelper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.io.File
import java.io.FileOutputStream
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
                entries.add(Entry((i-whr).toFloat(), `val`))
            }
            for (i in 0..whr) {
                val `val` = gainentries[whr - i].toFloat()
                entriset.add(Entry((i-whr).toFloat(), `val`))
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
        val noeat = findViewById<TextView>(R.id.noeattext)
        val noex = findViewById<TextView>(R.id.noextext)
        val noall = findViewById<TextView>(R.id.noalldatatext)
        val eatal = findViewById<TextView>(R.id.alleatkcal)
        val exal = findViewById<TextView>(R.id.allexkcal)
        val goalt = findViewById<TextView>(R.id.bestset)
        val achievi = findViewById<ImageView>(R.id.percentimage)
        if(eatal.text.toString()!="none"&&exal.text.toString()!="none"){
            noeat.setVisibility(View.INVISIBLE)
            noex.setVisibility(View.INVISIBLE)
            noall.setVisibility(View.INVISIBLE)
            val eatint = eatal.text.toString().toInt()
            val exint = exal.text.toString().toInt()
            if(eatint==0&&exint==0){//정보없음
                achievi.setImageResource(R.drawable.waiting)
                noall.setVisibility(View.VISIBLE)
            }
            else if(eatint==0){
                achievi.setImageResource(R.drawable.waiting)
                noeat.setVisibility(View.VISIBLE)
            }
            else if(exint==0){
                achievi.setImageResource(R.drawable.waiting)
                noex.setVisibility(View.VISIBLE)
            }
            else if(eatint>exint+500){
                when(goalt.text.toString()){
                    "체중 감소"->{
                        achievi.setImageResource(R.drawable.bad)
                    }
                    "체중 유지"->{
                        achievi.setImageResource(R.drawable.sad)
                    }
                    "체중 증가"->{
                        achievi.setImageResource(R.drawable.thumbup)
                    }
                    else->{Log.d("F","목표 오류")}
                }
            }
            else if(eatint<=exint+500&&eatint>exint-500){//적당
                when(goalt.text.toString()){
                    "체중 감소"->{
                        achievi.setImageResource(R.drawable.cheerup)
                    }
                    "체중 유지"->{
                        achievi.setImageResource(R.drawable.thumbup)
                    }
                    "체중 증가"->{
                        achievi.setImageResource(R.drawable.cheerup)
                    }
                    else->{Log.d("F","목표 오류")}
                }
            }
            else if(eatint<=exint-500){//운동
                when(goalt.text.toString()){
                    "체중 감소"->{
                        achievi.setImageResource(R.drawable.thumbup)
                    }
                    "체중 유지"->{
                        achievi.setImageResource(R.drawable.sad)
                    }
                    "체중 증가"->{
                        achievi.setImageResource(R.drawable.sad)
                    }
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
            if(exall==0)alex.setText("총 소비 칼로리: 정보 없음")
            else alex.setText("총 소비 칼로리: " + exall.toString() + "kcal")
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
            if(eatall==0)aleat.setText("총 섭취 칼로리: 정보 없음")
            else aleat.setText("총 섭취 칼로리: " + eatall.toString() + "kcal")
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
                                        axisMinimum = -1f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            2 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(3, true)
                                        axisMinimum = -2f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            3 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(4, true)
                                        axisMinimum = -3f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            4 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(5, true)
                                        axisMinimum = -4f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            5 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(6, true)
                                        axisMinimum = -5f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            6 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(7, true)
                                        axisMinimum = -6f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            7 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(8, true)
                                        axisMinimum = -7f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            8 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(15, true)
                                        axisMinimum = -14f
                                        axisMaximum = 0f
                                    }
                                }
                            }
                            9 -> {
                                chart.apply {
                                    xAxis.apply {
                                        setLabelCount(31, true)
                                        axisMinimum = -30f
                                        axisMaximum = 0f
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
        val sharebtn = findViewById<Button>(R.id.nope)
        val chartbtn = findViewById<Button>(R.id.chartendbtn)
        sharebtn.setOnClickListener {
            val layout = findViewById<ConstraintLayout>(R.id.chartLayout)
            sharebtn.visibility = View.INVISIBLE
            chartbtn.visibility = View.INVISIBLE
            val bitmap = getBitmapFromView(layout)
            sharebtn.visibility = View.VISIBLE
            chartbtn.visibility = View.VISIBLE

            shareImageandText(bitmap!!)
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
    open fun getBitmapFromView(view: View): Bitmap? {
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun shareImageandText(bitmap: Bitmap) {
        val uri = getImageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_STREAM, uri)

        intent.putExtra(Intent.EXTRA_TEXT, getMessageBody())

        intent.type = "image/png"

        startActivity(Intent.createChooser(intent, "차트 공유하기"))
    }

    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(cacheDir, "images")
        var uri: Uri? = null

        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "com.example.teamproject.fileprovider", file)
        } catch (e: java.lang.Exception) {
            //Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }

    fun getMessageBody():String
    {
        var body = "현재 건강관리 등급은 "

        val achievi = findViewById<ImageView>(R.id.percentimage)

        when(achievi.getDrawable().getConstantState())
        {
            getResources().getDrawable( R.drawable.thumbup).getConstantState() -> body += "'매우 좋음' 입니다.\n최고예요! 이대로만 유지하세요!";
            getResources().getDrawable( R.drawable.cheerup).getConstantState() -> body += "'좋음' 입니다.\n노력하고 계시네요! 조금만 더 힘내세요!";
            getResources().getDrawable( R.drawable.waiting).getConstantState() -> body += "'보통' 입니다.";
            getResources().getDrawable( R.drawable.sad).getConstantState() -> body += "'나쁨' 입니다.\n설정한 목표까지 힘내세요!";
            getResources().getDrawable( R.drawable.bad).getConstantState() -> body += "'매우 나쁨' 입니다.\n목표 달성도가 매우 낮습니다! 분발하세요!";
        }

        return body
    }
}