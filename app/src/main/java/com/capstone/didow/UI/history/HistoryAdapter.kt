package com.capstone.didow.UI.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.databinding.HistoryOptionBinding

class HistoryAdapter(private val listHistory: ArrayList<History>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

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
        val(date, averageSukuKata, tipe) = listHistory[position]
        Log.d("Date", date)
        Log.d("Suku Kata", averageSukuKata)
        Log.d("Tipe", tipe)

        holder.apply {
            binding.tvDate.text = date
            binding.tvSukuKata.text = "$averageSukuKata Suku Kata"
            binding.tvJenis.text = tipe
        }

        holder.binding.btnNext.setOnClickListener {
            onItemClickCallback.onItemClicked(listHistory[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int = listHistory.size

    interface OnItemClickCallback{
        fun onItemClicked(data:History)
    }
}