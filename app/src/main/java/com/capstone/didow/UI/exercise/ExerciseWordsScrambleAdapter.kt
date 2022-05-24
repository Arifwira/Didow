package com.capstone.didow.UI.exercise

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.databinding.WordsScrambleOptionBinding

class ExerciseWordsScrambleAdapter(private val listWordScramble: ArrayList<WordScramble>): RecyclerView.Adapter<ExerciseWordsScrambleAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: WordsScrambleOptionBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WordsScrambleOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (wordScramble) = listWordScramble[position]
        Log.d("Word Scramble", listWordScramble.size.toString())

        holder.apply {
            binding.btnWordsOption.text = wordScramble

            binding.btnWordsOption.setOnClickListener {
                onItemClickCallback.onItemClicked(listWordScramble[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = listWordScramble.size

    interface OnItemClickCallback{
        fun onItemClicked(data: WordScramble)
    }
}