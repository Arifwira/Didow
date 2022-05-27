package com.capstone.didow.UI.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.databinding.DetailHistoryLayoutBinding

class HistoryDetailAdapter(private val listDetailHistory: ArrayList<DetailHistory>) : RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder>() {
    class ViewHolder(var binding: DetailHistoryLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DetailHistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDetailHistory[position]
        holder.apply {
            binding.tvSoalNumber.text = "Soal Nomor ${data.numberSoal}"
            binding.tvDetailDescription.text = data.description

            val isVisible: Boolean = data.visibility
            binding.expandableDetailLayout.visibility = if(isVisible) View.VISIBLE else View.GONE

            binding.tvSoalNumber.setOnClickListener{
                data.visibility = !data.visibility
                notifyItemChanged(position)
            }

        }
    }

    override fun getItemCount(): Int = listDetailHistory.size
}