package com.capstone.didow.UI.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.databinding.DetailAttemptWrongSoalBinding
import com.capstone.didow.databinding.DetailHistoryLayoutBinding

class AttemptDetailAdapter(private val listDetailAttempts: ArrayList<WrongAttempts>) : RecyclerView.Adapter<AttemptDetailAdapter.ViewHolder>() {
    class ViewHolder(var binding: DetailAttemptWrongSoalBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DetailAttemptWrongSoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDetailAttempts[position]
        Log.d("isReversed", data.isReversed?.toString() ?: "null")
        holder.apply {
            binding.tvAttemptsNumber.text = "Percobaan ke ${data.attemptNumber}"
            binding.tvFalseAnswer.text = data.answer
            if (data.isReversed != null) {
                binding.isReversedContainer.visibility = View.VISIBLE
                if (data.isReversed) {
                    binding.isReversed.text = "Ya"
                } else {
                    binding.isReversed.text = "Tidak"
                }
            }
        }
    }

    override fun getItemCount(): Int = listDetailAttempts.size
}