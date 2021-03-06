package com.example.teamproject.nutrition

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.databinding.NfrRowBinding
import com.example.teamproject.exercise.NutritionFactsRecord
import kotlin.math.round

//사용자의 영양정보 기록 RecyclerView의 어댑터
class NFRAdapter(val items: ArrayList<NutritionFactsRecord>) :
    RecyclerView.Adapter<NFRAdapter.MyViewHolder>() {
    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnItemClick(holder: MyViewHolder, view: View, data: NutritionFactsRecord, position: Int)
    }

    inner class MyViewHolder(val binding: NfrRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.nutritionFactsRecord.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = NfrRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.binding.nutritionFactsRecord.text =
            convertDateFormat(item.recordtime) + " / " + item.nutritionFacts.fname + " " + item.intake.toString() + " (g) / " + calculateKcal(
                item
            ) + " (kcal)"
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun calculateKcal(data: NutritionFactsRecord): Double {
        val rawKcal =
            data.intake.toDouble() / data.nutritionFacts.pergram * data.nutritionFacts.kcal
        val formattedKcal = round(rawKcal * 10) / 10

        return formattedKcal
    }

    private fun convertDateFormat(date: String): String {
        var result = date.substring(11, 16)
        result = result.substring(0, 2) + "시 " + result.substring(3, 5) + "분"

        return result
    }
}
