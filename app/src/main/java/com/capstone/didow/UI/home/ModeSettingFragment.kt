package com.capstone.didow.UI.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.capstone.didow.R
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.databinding.CustomizeLayoutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModeSettingFragment: BottomSheetDialogFragment() {

    private var _binding: CustomizeLayoutBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomizeLayoutBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val enableBackground = ContextCompat.getDrawable(requireContext(), R.drawable.enable_mode_background) as Drawable
            val disableBackground = ContextCompat.getDrawable(requireContext(), R.drawable.disable_mode_background) as Drawable
            when (arguments?.get("mode")) {
                "normal" -> {
                    normalModeContainer.background = enableBackground
                    customModeContainer.background = disableBackground
                }
                "kustom" -> {
                    normalModeContainer.background = disableBackground
                    customModeContainer.background = enableBackground
                }
            }

            val homeFragment = parentFragment as HomeFragment
            normalModeContainer.setOnClickListener {
                normalModeContainer.background = enableBackground
                customModeContainer.background = disableBackground
                homeFragment.setResultAsMode("normal")
//                dismiss()
            }
            customModeContainer.setOnClickListener {
                normalModeContainer.background = disableBackground
                customModeContainer.background = enableBackground
                homeFragment.setResultAsMode("kustom")
//                dismiss()
            }
        }

    }
}