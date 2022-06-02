package com.capstone.didow.UI.exercise

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.util.*
import kotlin.collections.HashMap

class ExerciseWordsScrambleFragment : Fragment() {
    private var _binding: ExerciseWordsScrambleFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var tts: TextToSpeech

    companion object {
        fun newInstance() = ExerciseWordsScrambleFragment()
    }

    private lateinit var scrambleWordsViewModel: ExerciseWordsScrambleViewModel
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    private lateinit var adapter: ExerciseWordsScrambleAdapter

    private var hintImg : String? = null
    private var hintHangman : String? = null

    private var listWordScrambleOption = HashMap<String, Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseWordsScrambleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrambleWordsViewModel = ViewModelProvider(this).get(ExerciseWordsScrambleViewModel::class.java)

        showRecyclerViewList()
        binding.rvWordScramble.setHasFixedSize(true)

        playSound()
        openGuide()
        binding.lanjut.setOnClickListener {
            exerciseViewModel.nextQuestion()
        }

        binding.tvSoal.setOnClickListener {
            scrambleWordsViewModel.undoLetter()
        }

        exerciseViewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            Log.d("ganti", it.word)
            when (it) {
                is QuestionMultipleChoice -> {
                    findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseMultipleChoiceFragment)
                }
                is QuestionScrambleWords -> {
                    Log.d("scramble", it.letters.toString())
                    Log.d("Hangman List", it.hintHangman.toString())
                    hintHangman = it.hintHangman.joinToString(" ")
                    hintImg = it.hintImg
                    Log.d("Hint hangman should be", hintHangman.toString())
                    scrambleWordsViewModel.init(it.letters)
                    useHint()
                }
                is QuestionHandwriting -> {
                    findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseHandWritingFragment)
                }
            }
        })

        scrambleWordsViewModel.availableLetters.observe(viewLifecycleOwner, Observer { availableLetters ->
            adapter = ExerciseWordsScrambleAdapter(availableLetters)
            binding.rvWordScramble.adapter = adapter
            adapter.setOnItemClickCallback(object: ExerciseWordsScrambleAdapter.OnItemClickCallback {
                override fun onItemClicked(data: String) {
                    scrambleWordsViewModel.selectLetter(data)
                }
            })
        })

        scrambleWordsViewModel.selectedLetters.observe(viewLifecycleOwner, Observer { selectedLetters ->
            val answer = selectedLetters.joinToString("")
            binding.tvSoal.text = answer
            val word = exerciseViewModel.currentQuestion.value?.word
            if (selectedLetters.size == word?.length) {
                val isCorrect = exerciseViewModel.answer(answer)

                Log.d("isCorrect", isCorrect.toString())
                Toast.makeText(this.context,
                    "Anda $isCorrect",Toast.LENGTH_SHORT).show()
            }
        })

        exerciseViewModel.isRetry.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(
                    this.context,
                    "Maaf jawaban kamu salah, silahkan untuk jawab ulang.", Toast.LENGTH_SHORT
                ).show()
            }
        })

        exerciseViewModel.isFinished.observe(viewLifecycleOwner, Observer {
            if (it) {
                when (exerciseViewModel.getExerciseCategory()) {
                    "auto" -> findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseCompleteFragment)
                    "custom" -> findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_exerciseCompleteFragment)
                    "assessment" -> findNavController().navigate(R.id.action_exerciseWordsScrambleFragment_to_assessmentCompleteFragment)
                }
            }
        })
    }

    private fun playSound() {
        binding.btnPlay.setOnClickListener {
            binding.btnPlay.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake))
            tts = TextToSpeech(requireContext(), TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.forLanguageTag("in"))
                    tts.setSpeechRate(1.0f)
                    tts.speak(
                        "${exerciseViewModel.currentQuestion.value?.word}",
                        TextToSpeech.QUEUE_ADD, null
                    )
                }
                    Log.d("SOUND", "KATA ACAK")
            })
        }
    }

    private fun openGuide() {
        binding.btnGuide.setOnClickListener {
            Toast.makeText(
                this@ExerciseWordsScrambleFragment.context,
                "You open the Guidebook", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun useHint() {
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

    private fun showRecyclerViewList() {
        binding.apply {
            val layoutManager = FlexboxLayoutManager(activity)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.SPACE_AROUND
            layoutManager.alignItems = AlignItems.CENTER
            rvWordScramble.layoutManager = layoutManager
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}