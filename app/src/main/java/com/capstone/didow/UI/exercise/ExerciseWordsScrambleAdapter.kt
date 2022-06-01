package com.capstone.didow.UI.exercise

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.R
import com.capstone.didow.databinding.WordsScrambleOptionBinding

class ExerciseWordsScrambleAdapter(private val listWordScramble: HashMap<String, Int>): RecyclerView.Adapter<ExerciseWordsScrambleAdapter.ViewHolder>() {

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
        val letter = listWordScramble.keys.toTypedArray()[position]
        val qty = listWordScramble[listWordScramble.keys.toTypedArray()[position]]
        Log.d("Word Scramble", listWordScramble.size.toString())

        holder.apply {
            binding.btnWordsOption.text = letter
            binding.letterQty.text = qty.toString()
            if (qty == 0) {
                binding.btnWordsOption.isClickable= false
                binding.letterQty.visibility = View.INVISIBLE
                binding.btnWordsOption.backgroundTintList = holder.itemView.resources.getColorStateList(R.color.buttonOff)
            } else{
                binding.btnWordsOption.setOnClickListener {
                    onItemClickCallback.onItemClicked(listWordScramble.keys.toTypedArray()[holder.adapterPosition])
                }
            }
        }
    }

    override fun getItemCount(): Int = listWordScramble.size

    interface OnItemClickCallback{
        fun onItemClicked(data: String)
    }
}