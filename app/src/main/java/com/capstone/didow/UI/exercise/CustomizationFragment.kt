package com.capstone.didow.UI.exercise

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.capstone.didow.R
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.databinding.CustomizationFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomizationFragment : BottomSheetDialogFragment() {
    private var _binding: CustomizationFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CustomizationFragment()
    }

    private lateinit var viewModel: CustomizationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomizationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lanjutkustom.setOnClickListener {
            val intent = Intent(activity, ExerciseActivity::class.java)
            intent.putExtra("category", "auto")
            startActivity(intent)
        }
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        Log.d("CHECK", "${binding.four.isChecked}")
        binding.lessthanfour.isChecked = false
        toggleChoice()
        questionCount()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomizationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun toggleChoice() {
        binding.lessthanfour.setOnClickListener {
            binding.four.isChecked = false
            binding.morethanfour.isChecked = false
        }
        binding.four.setOnClickListener {
            binding.lessthanfour.isChecked = false
            binding.morethanfour.isChecked = false
        }
        binding.morethanfour.setOnClickListener {
            binding.lessthanfour.isChecked = false
            binding.four.isChecked = false
        }
    }

    private fun questionCount() {
        var i = 0
        binding.addQuestion.setOnClickListener {
            i++
            binding.questionCount.text = i.toString()
        }

        binding.redQuestion.setOnClickListener {
            if (binding.questionCount.text == "0") {
                Toast.makeText(requireContext(),"Pertanyaan tidak bisa kurang dari nol",Toast.LENGTH_SHORT).show()
                binding.questionCount.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake))
            } else {
                i--
                binding.questionCount.text = i.toString()
            }
        }
    }
}