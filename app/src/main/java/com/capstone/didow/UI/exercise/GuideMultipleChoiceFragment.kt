package com.capstone.didow.UI.exercise

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.capstone.didow.R
import com.capstone.didow.databinding.PanduanMultipleChoiceBinding
import com.capstone.didow.databinding.PopupHintBinding

class GuideMultipleChoiceFragment: DialogFragment() {
    private var _binding: PanduanMultipleChoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PanduanMultipleChoiceBinding.inflate(inflater, container, false)
        return binding.root
        
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDismiss.setOnClickListener {
            dismiss()
        }
    }
}