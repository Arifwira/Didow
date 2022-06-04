package com.capstone.didow.UI.home

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
            val easy = binding.lessthanfour.isChecked
            val medium = binding.four.isChecked
            val hard = binding.morethanfour.isChecked
            val qty = binding.questionCount.text.toString().toInt()
            val intent = Intent(activity, ExerciseActivity::class.java)
            intent.putExtra("category", "custom")
            intent.putExtra("easy", easy)
            intent.putExtra("medium", medium)
            intent.putExtra("hard", hard)
            intent.putExtra("qty", qty)
            startActivity(intent)
            dismiss()
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
//            binding.four.isChecked = false
//            binding.morethanfour.isChecked = false
            enableButton()
        }
        binding.four.setOnClickListener {
//            binding.lessthanfour.isChecked = false
//            binding.morethanfour.isChecked = false
            enableButton()
        }
        binding.morethanfour.setOnClickListener {
//            binding.lessthanfour.isChecked = false
//            binding.four.isChecked = false
            enableButton()
        }
    }

    private fun enableButton() {
        val count1 = binding.lessthanfour.isChecked
        val count2 = binding.four.isChecked
        val count3 = binding.morethanfour.isChecked
        binding.lanjutkustom.isEnabled =
            count1 == true || count2 == true || count3 == true
        if (binding.lanjutkustom.isEnabled) {
            binding.lanjutkustom.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))
        } else {
            binding.lanjutkustom.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
        }
    }

    private fun questionCount() {
        var i = 1
        binding.addQuestion.setOnClickListener {
            i++
            binding.questionCount.text = i.toString()
        }
        binding.redQuestion.setOnClickListener {
            if (binding.questionCount.text == "1") {
                Toast.makeText(activity, "Pertanyaan tidak bisa nol", Toast.LENGTH_SHORT)
                    .show()
                binding.questionCount.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.shake
                    )
                )
            } else {
                i--
                binding.questionCount.text = i.toString()
            }
        }
    }
}