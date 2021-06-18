package com.example.teamproject.chart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.R

class ChartEXAdapter(private val context: Context) :
    RecyclerView.Adapter<ChartEXAdapter.ViewHolder>() {

    var datas = mutableListOf<ExerciseData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chart_ex, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ename: TextView = itemView.findViewById(R.id.RFtext)
        private val etime: TextView = itemView.findViewById(R.id.RTtext)
        private val ekcal: TextView = itemView.findViewById(R.id.RHtext)
        fun bind(item: ExerciseData) {
            Log.d("X",item.exname.length.toString())
            if(item.exname.length>6){
                val cxn = item.exname.subSequence(0,6).toString()
                var csxn = cxn + ".."
                if(item.exname.length>8)csxn = csxn + "."
                ename.text = csxn
            }
            else ename.text = item.exname
            val ghour = item.extime/60
            val gmin = item.extime%60
            var fnaltime =  ghour.toString()+":"
            if(gmin<10)fnaltime = fnaltime + "0"
            fnaltime = fnaltime + gmin.toString()
            ekcal.text = fnaltime + "/" + item.exkcal.toString() + "kcal"

        }
    }
}