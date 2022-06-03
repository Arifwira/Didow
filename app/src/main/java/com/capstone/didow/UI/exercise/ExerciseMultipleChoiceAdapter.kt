package com.capstone.didow.UI.exercise

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.didow.R
import com.capstone.didow.databinding.MultipleChoiceOptionBinding
import kotlin.String

class ExerciseMultipleChoiceAdapter(private val listOption: ArrayList<String>): RecyclerView.Adapter<ExerciseMultipleChoiceAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: MultipleChoiceOptionBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MultipleChoiceOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = listOption[position]
        Log.d("Option", option)
        holder.apply {
            binding.btnOption.text = option
            binding.btnOption.setTextColor(binding.btnOption.context.resources.getColor(R.color.white))
            when(position) {
                0 -> binding.btnOption.setBackgroundColor(binding.btnOption.context.resources.getColor(R.color.purple_button))
                1 -> binding.btnOption.setBackgroundColor(binding.btnOption.context.resources.getColor(R.color.blue_button))
                2 -> binding.btnOption.setBackgroundColor(binding.btnOption.context.resources.getColor(R.color.green_button))
                3 -> binding.btnOption.setBackgroundColor(binding.btnOption.context.resources.getColor(R.color.orange_button))
            }

        }

        holder.binding.btnOption.setOnClickListener {
            onItemClickCallback.onItemClicked(listOption[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listOption.size

    interface OnItemClickCallback{
        fun onItemClicked(data: String)
    }
}