package com.capstone.didow.UI.exercise

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import com.capstone.didow.R

class ExerciseFragment : Fragment() {

    companion object {
        fun newInstance() = ExerciseFragment()
    }

    private lateinit var viewModel: ExerciseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.exercise_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.d("BACKPRESS", "Fragment back pressed invoked")
        }
        callback
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)

    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        requireActivity()
//            .onBackPressedDispatcher
//            .addCallback(this, object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    Log.d("BACKPRESS", "Fragment back pressed invoked")
//                    // Do custom work here
//
//                    // if you want onBackPressed() to be called as normal afterwards
//                    if (isEnabled) {
//                        isEnabled = false
//                        requireActivity().onBackPressed()
//                    }
//                }
//            }
//            )
//    }


}