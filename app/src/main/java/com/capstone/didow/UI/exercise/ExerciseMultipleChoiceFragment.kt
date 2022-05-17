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
import com.capstone.didow.databinding.RegisterFragmentBinding

class ExerciseMultipleChoiceFragment : Fragment() {

    private var _binding: ExerciseMultipleChoiceFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ExerciseMultipleChoiceFragment()
    }

    private lateinit var viewModel: ExerciseMultipleChoiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseMultipleChoiceFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lanjut.setOnClickListener {
            it.findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseWordsScrambleFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseMultipleChoiceViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}