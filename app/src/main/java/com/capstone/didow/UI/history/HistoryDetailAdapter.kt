package com.capstone.didow.UI.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.databinding.DetailHistoryLayoutBinding

class HistoryDetailAdapter(private val listDetailHistory: ArrayList<WrongAnswers>) : RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder>() {
    class ViewHolder(var binding: DetailHistoryLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var attemptsAdapter : AttemptDetailAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DetailHistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDetailHistory[position]
        val attemptData = data.attempts
        attemptsAdapter = AttemptDetailAdapter(attemptData as ArrayList<WrongAttempts> /* = java.util.ArrayList<com.capstone.didow.UI.history.WrongAttempts> */)
        var visibility = false
        holder.apply {
            binding.tvSoalNumber.text = "Soal Nomor ${data.number}"
            binding.tvTrueAnswer.text = data.word
            binding.tvTypeInput.text = when(data.type){
                "multipleChoice" -> "Multiple Choice"
                "handwriting" -> "Hand Writing"
                "scrambleWords" -> "Scramble Words"
                else -> ""
            }
            binding.rvDetailSoal.layoutManager = LinearLayoutManager(holder.itemView.context)
            binding.rvDetailSoal.adapter = attemptsAdapter

            binding.consDetailLayout.setOnClickListener{
                visibility = !visibility
                binding.expandableDetailLayout.visibility = if(visibility) View.VISIBLE else View.GONE
//                notifyItemChanged(position)
            }

        }
    }

    override fun getItemCount(): Int = listDetailHistory.size
}