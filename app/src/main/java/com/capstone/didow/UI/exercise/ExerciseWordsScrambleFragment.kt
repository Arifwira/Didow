package com.capstone.didow.UI.exercise

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseMultipleChoiceFragmentBinding
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding

class ExerciseWordsScrambleFragment : Fragment() {
    private var _binding: ExerciseWordsScrambleFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = ExerciseWordsScrambleFragment()
    }

    private lateinit var viewModel: ExerciseWordsScrambleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseWordsScrambleFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseWordsScrambleViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lanjut.setOnClickListener{
            it.findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseHandWritingFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}