package com.capstone.didow.UI.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.capstone.didow.databinding.PopupHintBinding

class PopupHintFragment: DialogFragment() {
    private var _binding: PopupHintBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PopupHintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val mHint = arguments?.getString("hint")
            val mImageUrl = arguments?.getString("imageUrl")
            tvHint.text = mHint
            Log.d("ImageUrl", mImageUrl.toString())
            Glide.with(ivHint).load(mImageUrl).centerCrop().into(ivHint)
        }
    }
}