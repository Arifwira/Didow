package com.capstone.didow.UI.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding
import com.capstone.didow.databinding.FragmentAssessmentCompleteBinding
import com.capstone.didow.databinding.FragmentExerciseCompleteBinding

class ExerciseCompleteFragment : Fragment() {
    private var _binding: FragmentExerciseCompleteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentExerciseCompleteBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backHome.setOnClickListener{
            this.activity?.finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}