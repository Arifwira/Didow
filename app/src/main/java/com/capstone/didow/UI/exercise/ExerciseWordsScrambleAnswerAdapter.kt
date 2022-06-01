package com.capstone.didow.UI.exercise

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.databinding.MultipleChoiceOptionBinding
import com.capstone.didow.databinding.WordsScrambleAnswerBinding
import kotlin.String

class ExerciseWordsScrambleAnswerAdapter(private val listAnswer: List<String>): RecyclerView.Adapter<ExerciseWordsScrambleAnswerAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: WordsScrambleAnswerBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WordsScrambleAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = listAnswer[position]
        Log.d("Option", option)
        holder.apply {
            binding.btnWordsAnswer.text = option
        }

        holder.binding.btnWordsAnswer.setOnClickListener {
            onItemClickCallback.onItemClicked(listAnswer[holder.adapterPosition])
        }
    }


    override fun getItemCount(): Int = listAnswer.size
    interface OnItemClickCallback{
        fun onItemClicked(data: String)
    }
}