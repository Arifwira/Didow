package com.capstone.didow.UI.exercise

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.didow.R

class ExerciseMultipleChoiceFragment : Fragment() {

    companion object {
        fun newInstance() = ExerciseMultipleChoiceFragment()
    }

    private lateinit var viewModel: ExerciseMultipleChoiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.exercise_multiple_choice_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseMultipleChoiceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}