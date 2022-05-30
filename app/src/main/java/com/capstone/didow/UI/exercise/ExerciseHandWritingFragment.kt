package com.capstone.didow.UI.exercise

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseHandWritingFragmentBinding
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords

class ExerciseHandWritingFragment : Fragment() {
    private var _binding: ExerciseHandWritingFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = ExerciseHandWritingFragment()
    }

    private lateinit var viewModel: ExerciseHandWritingViewModel
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    private var hintImg: String? = null
    private var hintHangman: String? = null

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
            exerciseViewModel.nextQuestion()
        }

        exerciseViewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            Log.d("ganti", it.word)
            when (it) {
                is QuestionMultipleChoice -> {
                    findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseMultipleChoiceFragment)
                }
                is QuestionScrambleWords -> {
                    findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseWordsScrambleFragment)
                }
                is QuestionHandwriting -> {
                    Log.d("Hangman List", it.hintHangman.toString())
                    this.hintImg = it.hintImg
                    Log.d("Hint hangman should be", hintHangman.toString())
                    this.hintHangman = it.hintHangman.joinToString(" ")

                    useHint()
                }
            }
        })

        exerciseViewModel.isRetry.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(this@ExerciseHandWritingFragment.context,
                    "Maaf jawaban kamu salah, silahkan untuk jawab ulang.", Toast.LENGTH_SHORT).show()
            }
        })

        exerciseViewModel.isFinished.observe(viewLifecycleOwner, Observer {
            if (it) {
                when (exerciseViewModel.getExerciseCategory()) {
                    "auto" -> findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseCompleteFragment)
                    "custom" -> findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_exerciseCompleteFragment)
                    "assessment" -> findNavController().navigate(R.id.action_exerciseHandWritingFragment_to_assessmentCompleteFragment)
                }
            }
        })
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
            var args = Bundle()
            args.putString("hint", hintHangman)
            Log.d("hint hangman must", hintHangman.toString())
            args.putString("imageUrl", hintImg.toString())
            Log.d("image argument be", hintImg.toString())

            val popupHintFragment = PopupHintFragment()
            binding.btnHint.setOnClickListener {
                popupHintFragment.arguments = args
                popupHintFragment.show(childFragmentManager, "PopUpHintDialog Fragment")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}