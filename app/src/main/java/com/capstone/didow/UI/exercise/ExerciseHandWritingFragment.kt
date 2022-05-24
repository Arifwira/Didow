package com.capstone.didow.UI.exercise

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseHandWritingFragmentBinding
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding

class ExerciseHandWritingFragment : Fragment() {
    private var _binding: ExerciseHandWritingFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = ExerciseHandWritingFragment()
    }

    private lateinit var viewModel: ExerciseHandWritingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= ExerciseHandWritingFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseHandWritingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playSound()
        useHint()
        openGuide()

        binding.btnCamera.setOnClickListener {
            Toast.makeText(this@ExerciseHandWritingFragment.context,
                "You open Camera", Toast.LENGTH_SHORT).show()
        }

        binding.btnGallery.setOnClickListener {
            Toast.makeText(this@ExerciseHandWritingFragment.context,
                "You open Gallery", Toast.LENGTH_SHORT).show()
        }

        binding.lanjut.setOnClickListener{
            it.findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_assessmentCompleteFragment)
        }
    }

    private fun openGuide(){
        binding.btnGuide.setOnClickListener {
            Toast.makeText(this@ExerciseHandWritingFragment.context,
                "You open the Guidebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playSound(){
        binding.btnPlay.setOnClickListener {
            Toast.makeText(this@ExerciseHandWritingFragment.context,
                "You play the sound", Toast.LENGTH_SHORT).show()
        }
    }

    private fun useHint(){
        binding.btnHint.setOnClickListener {
            Toast.makeText(this@ExerciseHandWritingFragment.context,
                "You use hint", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}