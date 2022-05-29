package com.capstone.didow.UI.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords
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
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    private lateinit var adapter: ExerciseWordsScrambleAdapter

    private val listWordScrambleOption = ArrayList<String>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerViewList()
        binding.rvWordScramble.setHasFixedSize(true)

        playSound()
        useHint()
        openGuide()
        binding.lanjut.setOnClickListener {
            exerciseViewModel.nextQuestion()
        }

        exerciseViewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            Log.d("ganti", it.word)
            when (it) {
                is QuestionMultipleChoice -> {
                    findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseMultipleChoiceFragment)
                }
                is QuestionScrambleWords -> {
                    Log.d("scramble", it.letters.toString())
                    listWordScrambleOption.clear()
                    listWordScrambleOption.addAll(it.letters)
                    adapter = ExerciseWordsScrambleAdapter(listWordScrambleOption)
                    binding.rvWordScramble.adapter = adapter
                }
                is QuestionHandwriting -> {
                    findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseHandWritingFragment)
                }
            }
        })

        exerciseViewModel.isRetry.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(this.context,
                    "Maaf jawaban kamu salah, silahkan untuk jawab ulang.", Toast.LENGTH_SHORT).show()
            }
        })

        exerciseViewModel.isFinished.observe(viewLifecycleOwner, Observer {
            if (it) {
                when (exerciseViewModel.getExerciseCategory()) {
                    "auto" -> findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseCompleteFragment)
                    "custom" -> findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseCompleteFragment)
                    "assessment" -> findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_assessmentCompleteFragment)
                }
            }
        })
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}