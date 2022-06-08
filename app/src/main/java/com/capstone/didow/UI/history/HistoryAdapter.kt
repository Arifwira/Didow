package com.capstone.didow.UI.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.databinding.HistoryOptionBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val listHistory: List<History>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: HistoryOptionBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listHistory[position]
        Log.d("Size", data.wrongAnswer.size.toString())
        Log.d("Date", data.endTime.toString())
        Log.d("Soal", data.qty.toString())
        Log.d("Suku Kata", data.avgSyllables.toString())

        holder.apply {

            val date = convertLongToTime(data.endTime)
            binding.tvDate.text = date
            binding.tvSukuKata.text = "${data.avgSyllables} Suku Kata"
            binding.tvJenis.text = "Normal"
        }

        holder.binding.historyOption.setOnClickListener {
            onItemClickCallback.onItemClicked(listHistory[holder.adapterPosition])
        }

    }

    private fun convertLongToTime(time: Long): String{
        val date = Date(time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(date)
    }

    override fun getItemCount(): Int = listHistory.size

    interface OnItemClickCallback{
        fun onItemClicked(data:History)
    }
}