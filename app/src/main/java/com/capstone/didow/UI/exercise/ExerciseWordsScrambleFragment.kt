package com.capstone.didow.UI.exercise

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseWordsScrambleFragmentBinding
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords
import com.google.android.flexbox.*
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

    private lateinit var choiceAdapter: ExerciseWordsScrambleAdapter

    private lateinit var answerAdapter: ExerciseWordsScrambleAnswerAdapter

    private var hintImg : String? = null
    private var hintHangman : String? = null

    private var listWordScrambleOption = HashMap<String, Int>()

    private var listWordScrambleAnswer = ArrayList<String>()

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
        showAnswerList()
        binding.rvWordScramble.setHasFixedSize(true)
        Glide.with(this).load(R.drawable.wiggle).into(binding.tekanAku)
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
                    val currentNumber = exerciseViewModel.currentQuestion.value?.number
                    val totalNumber = exerciseViewModel.exercise.value?.questions?.size
                    binding.tvNomorSoal.text = "$currentNumber/$totalNumber"
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
            choiceAdapter = ExerciseWordsScrambleAdapter(availableLetters)
            binding.rvWordScramble.adapter = choiceAdapter
            choiceAdapter.setOnItemClickCallback(object: ExerciseWordsScrambleAdapter.OnItemClickCallback {
                override fun onItemClicked(data: String) {
                    scrambleWordsViewModel.selectLetter(data)
                }
            })
        })


        scrambleWordsViewModel.selectedLetters.observe(viewLifecycleOwner, Observer { selectedLetters ->
            val answer = selectedLetters.joinToString("")
            val splitWords = exerciseViewModel.currentQuestion.value?.word?.length
            Log.d("Split word", splitWords.toString())
            val mappingAnswer = mutableListOf<String>()
            for(i in 1..splitWords!!) {
                mappingAnswer.add("")
            }

            selectedLetters.forEachIndexed { index, element ->
                mappingAnswer[index] = element
            }


            answerAdapter = ExerciseWordsScrambleAnswerAdapter(mappingAnswer)
            binding.rvAnswer.adapter = answerAdapter

            answerAdapter.setOnItemClickCallback(object: ExerciseWordsScrambleAnswerAdapter.OnItemClickCallback {
                override fun onItemClicked(data: String) {
                    if(selectedLetters.isEmpty()){
                        Toast.makeText(this@ExerciseWordsScrambleFragment.context,"Jawaban masih Kosong",Toast.LENGTH_SHORT).show()
                        Log.d("answer rv remove", "Jawaban masih Kosong")
                    }else{
                    scrambleWordsViewModel.undoLetter()
                    Log.d("answer rv remove", selectedLetters.toString())
                    }
                }
            })
            val word = exerciseViewModel.currentQuestion.value?.word
            if (selectedLetters.size == word?.length) {
                val isCorrect = exerciseViewModel.answer(answer)

                when(isCorrect.toString()){
                    "true" -> {
                        trueDialog()
                    }
                    "false" -> {
                        falseDialog()
                    }
                }
//                Toast.makeText(this.context,
//                    "Anda $isCorrect",Toast.LENGTH_SHORT).show()
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
                    tts.language = Locale.forLanguageTag("in")
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
        val guideWordsScramble = GuideWordsScrambleFragment()
        binding.btnGuide.setOnClickListener {
            guideWordsScramble.show(childFragmentManager, "Words Scramble Panduan")
//            Toast.makeText(this@ExerciseWordsScrambleFragment.context,
//                "You open the Guidebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun useHint() {
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

    private fun showRecyclerViewList() {
        binding.apply {
            val layoutManager = FlexboxLayoutManager(activity)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.CENTER
            rvWordScramble.layoutManager = layoutManager
        }
    }

    private fun showAnswerList(){
        binding.apply {
            val layoutManager = FlexboxLayoutManager(activity)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.flexWrap = FlexWrap.WRAP
            layoutManager.justifyContent = JustifyContent.SPACE_AROUND
            layoutManager.alignItems = AlignItems.CENTER
            rvAnswer.layoutManager = layoutManager

        }
    }

    private fun trueDialog(){
        val view = View.inflate(this@ExerciseWordsScrambleFragment.context, R.layout.dialog_true_view, null)
        val builder = AlertDialog.Builder(this@ExerciseWordsScrambleFragment.context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            exerciseViewModel.nextQuestion()
        }, 3000)
    }

    private fun falseDialog(){
        val view = View.inflate(this@ExerciseWordsScrambleFragment.context, R.layout.dialog_false_view, null)
        val builder = AlertDialog.Builder(this@ExerciseWordsScrambleFragment.context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            exerciseViewModel.nextQuestion()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}