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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseMultipleChoiceFragmentBinding
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords
import com.google.android.flexbox.*
import java.util.*


class ExerciseMultipleChoiceFragment : Fragment() {

    private var _binding: ExerciseMultipleChoiceFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var tts : TextToSpeech

    companion object {
        fun newInstance() = ExerciseMultipleChoiceFragment()
    }

    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    private lateinit var adapter: ExerciseMultipleChoiceAdapter
    private val listMultipleChoiceOption = ArrayList<String>()

    private var hintImg : String? = null
    private var hintHangman : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciseMultipleChoiceFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPilgan.setHasFixedSize(true)
        showRecyclerViewList()
        playSound()
        openGuide()
//        binding.lanjut.setOnClickListener {
//            exerciseViewModel.nextQuestion()
//        }

        exerciseViewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            Log.d("ganti", it.word)
            Log.d("hintImg from observe", it.hintImg)
            when (it) {
                is QuestionMultipleChoice -> {
                    val currentNumber = exerciseViewModel.currentQuestion.value?.number
                    val totalNumber = exerciseViewModel.exercise.value?.questions?.size
                    hintImg = it.hintImg
                    listMultipleChoiceOption.clear()
                    listMultipleChoiceOption.addAll(it.choices)
                    adapter = ExerciseMultipleChoiceAdapter(listMultipleChoiceOption)
                    binding.rvPilgan.adapter = adapter
                    binding.tvNomorSoal.text = "$currentNumber/$totalNumber"
                    adapter.setOnItemClickCallback(object: ExerciseMultipleChoiceAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: String) {
                            val isCorrect = exerciseViewModel.answer(data, null)
                            when(isCorrect.toString()){
                                "true" -> {
                                    trueDialog()
                                }
                                "false" -> {
                                    falseDialog()
                                }
                            }
                        }
                    })
                    useHint()
                }
                is QuestionScrambleWords -> {
                    findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseWordsScrambleFragment)
                }
                is QuestionHandwriting -> {
                    findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseHandWritingFragment)
                }
            }
        })

        exerciseViewModel.isRetry.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(this.context,
                    "Maaf jawaban kamu salah, silahkan untuk jawab ulang.", Toast.LENGTH_SHORT).show()
            }
        })

        exerciseViewModel.isFinished.observe(viewLifecycleOwner, Observer {
            if (it) {
                when (exerciseViewModel.getExerciseCategory()) {
                    "auto" -> findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseCompleteFragment)
                    "custom" -> findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseCompleteFragment)
                    "assessment" -> findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_assessmentCompleteFragment)
                }
            }
        })
    }

    private fun trueDialog(){
        val view = View.inflate(this@ExerciseMultipleChoiceFragment.context, R.layout.dialog_true_view, null)
        val builder = AlertDialog.Builder(this@ExerciseMultipleChoiceFragment.context)
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
        val view = View.inflate(this@ExerciseMultipleChoiceFragment.context, R.layout.dialog_false_view, null)
        val builder = AlertDialog.Builder(this@ExerciseMultipleChoiceFragment.context)
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

    private fun openGuide(){
        val guideMultipleChoice = GuideMultipleChoiceFragment()
        binding.btnGuide.setOnClickListener {
            guideMultipleChoice.show(childFragmentManager, "Multiple Choice Panduan")
//            Toast.makeText(this@ExerciseMultipleChoiceFragment.context,
//                "You open the Guidebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playSound(){
        binding.btnPlay.setOnClickListener {
            binding.btnPlay.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake))
            tts = TextToSpeech(requireContext(),TextToSpeech.OnInitListener {
                if(it==TextToSpeech.SUCCESS){
                    tts.setLanguage(Locale.forLanguageTag("in"))
                    tts.setSpeechRate(1.0f)
                    tts.speak("${exerciseViewModel.currentQuestion.value?.word}",TextToSpeech.QUEUE_ADD,null)
                }
                Log.d("SOUND", "PILIHAN GANDA")
            })
        }
    }

    private fun useHint(){
        var args = Bundle()
        args.putString("hint", hintHangman)
        args.putString("imageUrl", hintImg.toString())
        Log.d("image argument be", hintImg.toString())

        val popupHintFragment = PopupHintFragment()
        binding.btnHint.setOnClickListener {
            popupHintFragment.arguments = args
            popupHintFragment.show(childFragmentManager, "PopUpHintDialog Fragment")
        }
    }


    private fun showRecyclerViewList(){
        binding.apply {
            val layoutManager = FlexboxLayoutManager(activity)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.flexWrap = FlexWrap.WRAP
            layoutManager.justifyContent = JustifyContent.SPACE_EVENLY
            layoutManager.alignItems = AlignItems.CENTER
            rvPilgan.layoutManager = layoutManager
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}