package com.capstone.didow.UI.exercise

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.ExerciseMultipleChoiceFragmentBinding
import com.google.android.flexbox.*


class ExerciseMultipleChoiceFragment : Fragment() {

    private var _binding: ExerciseMultipleChoiceFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ExerciseMultipleChoiceFragment()
    }

    private lateinit var viewModel: ExerciseMultipleChoiceViewModel

    private lateinit var adapter: ExerciseMultipleChoiceAdapter
    private val listMultipleChoiceOption = ArrayList<MultipleChoice>()

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
        listMultipleChoiceOption.clear()
        listMultipleChoiceOption.addAll(listMultipleChoiceOptions)
        showRecyclerViewList()

        playSound()
        openGuide()
        useHint()

        binding.lanjut.setOnClickListener {
            it.findNavController().navigate(R.id.action_exerciseMultipleChoiceFragment_to_exerciseWordsScrambleFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseMultipleChoiceViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private val listMultipleChoiceOptions: ArrayList<MultipleChoice>
    get(){
        val dataOption = resources.getStringArray(R.array.multiple_choice_option)
        Log.d("Option-1", dataOption[0])
        Log.d("Option-2", dataOption[1])
        Log.d("Option-3", dataOption[2])
        Log.d("Option-4", dataOption[3])

        val listMultipleChoiceOption = ArrayList<MultipleChoice>()
        for (i in dataOption.indices) {
            val option = MultipleChoice(dataOption[i])
            listMultipleChoiceOption.add(option)
        }
        return listMultipleChoiceOption
    }

    private fun openGuide(){
        binding.btnGuide.setOnClickListener {
            Toast.makeText(this@ExerciseMultipleChoiceFragment.context,
                "You open the Guidebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playSound(){
        binding.btnPlay.setOnClickListener {
            Toast.makeText(this@ExerciseMultipleChoiceFragment.context,
                "You play the sound", Toast.LENGTH_SHORT).show()
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
            adapter = ExerciseMultipleChoiceAdapter(listMultipleChoiceOption)
            rvPilgan.adapter = adapter

            adapter.setOnItemClickCallback(object: ExerciseMultipleChoiceAdapter.OnItemClickCallback {
                override fun onItemClicked(data: MultipleChoice) {
                    Toast.makeText(this@ExerciseMultipleChoiceFragment.context,
                        "Anda memilih ${data.option}",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}