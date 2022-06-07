package com.capstone.didow.UI.exercise

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.databinding.FragmentExerciseCompleteBinding

class ExerciseCompleteFragment : Fragment() {
    private var _binding: FragmentExerciseCompleteBinding? = null
    private val binding get() = _binding!!
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentExerciseCompleteBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.restart.setOnClickListener {
            val intent = Intent(activity, ExerciseActivity::class.java)
            intent.putExtras(exerciseViewModel.bundle.value!!)
            startActivity(intent)
            activity?.finish()
        }

        binding.backHome.setOnClickListener{
            this.activity?.finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}