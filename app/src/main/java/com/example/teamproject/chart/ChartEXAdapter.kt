package com.example.teamproject.chart

import android.content.Context
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
            ename.text = item.exname
            etime.text = item.extime.toString()
            ekcal.text = item.exkcal.toString()

        }
    }
}