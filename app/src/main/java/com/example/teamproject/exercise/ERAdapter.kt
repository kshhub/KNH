package com.example.teamproject.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.databinding.NfrRowBinding

//운동정보 기록 RecyclerView에 대한 어댑터.
class ERAdapter(val items: ArrayList<ExerciseRecord>) :
    RecyclerView.Adapter<ERAdapter.MyViewHolder>() {
    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnItemClick(holder: MyViewHolder, view: View, data: ExerciseRecord, position: Int)
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
            convertDateFormat(item.recordtime) + " / " + item.exercise.ename + " / " + item.etime.toString() + " (분) / " + item.totalKcal.toString() + " (kcal)"
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    //시간 정보를 간략하게 바꿔줍니다.
    private fun convertDateFormat(date: String): String {
        var result = date.substring(11, 16)
        result = result.substring(0, 2) + "시 " + result.substring(3, 5) + "분"

        return result
    }
}
