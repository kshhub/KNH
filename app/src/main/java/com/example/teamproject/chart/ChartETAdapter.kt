package com.example.teamproject.chart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.R

class ChartETAdapter(private val context: Context) :
    RecyclerView.Adapter<ChartETAdapter.ViewHolder>() {

    var datas = mutableListOf<EatingData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chart_et, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tname: TextView = itemView.findViewById(R.id.RFtext)
        private val tkcal: TextView = itemView.findViewById(R.id.RTtext)

        fun bind(item: EatingData) {
            Log.d("B",item.ekcal.toString())
            if(item.ename.length>4){
                val cen = item.ename.subSequence(0,4).toString()
                var cstn = cen + ".."
                if(item.ename.length>6)cstn = cstn + "."
                tname.text = cstn
            }
            else tname.text = item.ename
            tkcal.text = item.ekcal.toString()+"kcal"

        }
    }
}