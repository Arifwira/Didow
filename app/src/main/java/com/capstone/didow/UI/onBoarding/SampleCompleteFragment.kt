package com.capstone.didow.UI.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.UI.exercise.ExerciseViewModel
import com.capstone.didow.databinding.FragmentSampleCompleteBinding

class SampleCompleteFragment : Fragment() {
    private var _binding: FragmentSampleCompleteBinding? = null
    private val binding get() = _binding!!
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSampleCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.assessment.setOnClickListener {
            val intent = Intent(activity, OnBoarding::class.java)
            intent.putExtra("fromSample", true)
            activity?.setResult(110, intent)
            activity?.finish()
        }

        binding.retry.setOnClickListener {
            val intent = Intent(activity, ExerciseActivity::class.java)
            intent.putExtras(exerciseViewModel.bundle.value!!)
            startActivity(intent)
            activity?.finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}