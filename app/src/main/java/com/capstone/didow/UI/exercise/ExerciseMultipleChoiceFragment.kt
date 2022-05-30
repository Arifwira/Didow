package com.capstone.didow.UI.exercise

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseMultipleChoiceFragmentBinding
import com.capstone.didow.entities.QuestionHandwriting
import com.capstone.didow.entities.QuestionMultipleChoice
import com.capstone.didow.entities.QuestionScrambleWords
import com.google.android.flexbox.*
import org.w3c.dom.Text
import java.util.*
import kotlin.String
import kotlin.collections.ArrayList


class ExerciseMultipleChoiceFragment : Fragment() {

    private var _binding: ExerciseMultipleChoiceFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var tts : TextToSpeech

    companion object {
        fun newInstance() = ExerciseMultipleChoiceFragment()
    }

    private lateinit var viewModel: ExerciseMultipleChoiceViewModel
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    private lateinit var adapter: ExerciseMultipleChoiceAdapter
    private val listMultipleChoiceOption = ArrayList<String>()

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
        useHint()

        binding.lanjut.setOnClickListener {
            exerciseViewModel.nextQuestion()
        }

        exerciseViewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            Log.d("ganti", it.word)
            when (it) {
                is QuestionMultipleChoice -> {
                    listMultipleChoiceOption.clear()
                    listMultipleChoiceOption.addAll(it.choices)
                    adapter = ExerciseMultipleChoiceAdapter(listMultipleChoiceOption)
                    binding.rvPilgan.adapter = adapter
                    adapter.setOnItemClickCallback(object: ExerciseMultipleChoiceAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: String) {
                            val isCorrect = exerciseViewModel.answer(data)
                            Log.d("isCorrect", isCorrect.toString())
                            Toast.makeText(this@ExerciseMultipleChoiceFragment.context,
                                "Anda $isCorrect",Toast.LENGTH_SHORT).show()
                        }
                    })
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

    private fun openGuide(){
        binding.btnGuide.setOnClickListener {
            Toast.makeText(this@ExerciseMultipleChoiceFragment.context,
                "You open the Guidebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playSound(){
        binding.btnPlay.setOnClickListener {
            tts = TextToSpeech(requireContext(),TextToSpeech.OnInitListener {
                if(it==TextToSpeech.SUCCESS){
                    tts.setSpeechRate(1.0f)
                    tts.speak("${exerciseViewModel.currentQuestion.value?.word}",TextToSpeech.QUEUE_ADD,null)
                }
                Log.d("SOUND", "PILIHAN GANDA")
            })
        }
    }

    private fun useHint(){
        var args = Bundle()
        args.putString("hint", "_U_U")
        args.putString("imageUrl", "https://tafsirweb.com/wp-content/uploads/2018/09/book-open-icon.png")
        val popupHintFragment = PopupHintFragment()
        binding.btnHint.setOnClickListener {
//            Toast.makeText(this@ExerciseMultipleChoiceFragment.context,
//                "You use hint", Toast.LENGTH_SHORT).show()
            popupHintFragment.arguments = args
            popupHintFragment.show(childFragmentManager, "PopUpHintDialog Fragment")
        }
    }


    private fun showRecyclerViewList(){
        binding.apply {
            val layoutManager = FlexboxLayoutManager(activity)
            layoutManager.flexDirection = FlexDirection.COLUMN
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