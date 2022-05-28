package com.capstone.didow.UI.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class ExerciseWordsScrambleFragment : Fragment() {
    private var _binding: ExerciseWordsScrambleFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ExerciseWordsScrambleFragment()
    }

    private lateinit var viewModel: ExerciseWordsScrambleViewModel

    private lateinit var adapter: ExerciseWordsScrambleAdapter

    private val listWordScrambleOption = ArrayList<WordScramble>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseWordsScrambleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseWordsScrambleViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private val listWordScrambleArray: ArrayList<WordScramble>
    get(){
        val dataWordScrambleOption = resources.getStringArray(R.array.word_scramble_option)
        Log.d("Option-1", dataWordScrambleOption[0])
        Log.d("Option-2", dataWordScrambleOption[1])
        Log.d("Option-3", dataWordScrambleOption[2])
        Log.d("Option-4", dataWordScrambleOption[3])
        Log.d("Option-5", dataWordScrambleOption[4])
        Log.d("Option-6", dataWordScrambleOption[5])

        val tempListWordScrambleOption = ArrayList<WordScramble>()
        for (i in dataWordScrambleOption.indices){
            val option = WordScramble(dataWordScrambleOption[i])
            tempListWordScrambleOption.add(option)
        }
        return tempListWordScrambleOption
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvWordScramble.setHasFixedSize(true)
        listWordScrambleOption.clear()
        listWordScrambleOption.addAll(listWordScrambleArray)
        adapter = ExerciseWordsScrambleAdapter(listWordScrambleOption)
        showRecyclerViewList()

        playSound()
        useHint()
        openGuide()
        binding.lanjut.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseHandWritingFragment)
        }
    }

    private fun playSound(){
        binding.btnPlay.setOnClickListener {
            Toast.makeText(this@ExerciseWordsScrambleFragment.context,
                "You play the sound", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGuide(){
        binding.btnGuide.setOnClickListener {
            Toast.makeText(this@ExerciseWordsScrambleFragment.context,
                "You open the Guidebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun useHint(){
        binding.btnHint.setOnClickListener {
            Toast.makeText(this@ExerciseWordsScrambleFragment.context,
                "You use hint", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRecyclerViewList(){
        binding.apply{
            val layoutManager = FlexboxLayoutManager(activity)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.SPACE_AROUND
            layoutManager.alignItems = AlignItems.CENTER
            rvWordScramble.layoutManager = layoutManager
            rvWordScramble.adapter =  adapter
        }

        adapter.setOnItemClickCallback(object: ExerciseWordsScrambleAdapter.OnItemClickCallback{
            override fun onItemClicked(data: WordScramble) {
                Toast.makeText(this@ExerciseWordsScrambleFragment.context, "Anda memilih ${data.wordScramble}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}