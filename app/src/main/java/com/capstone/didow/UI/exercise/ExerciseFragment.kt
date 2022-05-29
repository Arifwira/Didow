package com.capstone.didow.UI.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseFragmentBinding
import com.capstone.didow.entities.*

class ExerciseFragment : Fragment() {

    companion object {
        fun newInstance() = ExerciseFragment()
    }

    private val viewModel: ExerciseViewModel by activityViewModels()
    private var _binding: ExerciseFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = this.activity?.intent?.getStringExtra("category")
        viewModel.init(category!!)
        viewModel.isLoaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.startExercise()
                when (viewModel.currentQuestion.value) {
                    is QuestionMultipleChoice -> {
                        findNavController().navigate(R.id.action_exerciseFragment_to_exerciseMultipleChoiceFragment)
                    }
                    is QuestionScrambleWords -> {
                        findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseWordsScrambleFragment)
                    }
                    is QuestionHandwriting -> {
                        findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseHandWritingFragment)
                    }
                }
            }
        })
    }

}
