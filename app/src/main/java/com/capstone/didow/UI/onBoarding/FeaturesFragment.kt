package com.capstone.didow.UI.onBoarding

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.FragmentFeaturesBinding


class FeaturesFragment : Fragment() {
    private var _binding: FragmentFeaturesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: OnBoardingViewModel
    private lateinit var readAnimation : AnimationDrawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFeaturesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kembali.setOnClickListener {
            it.findNavController().navigate(R.id.action_featuresFragment_to_onBoardingFragment2)
        }
        binding.coba.setOnClickListener{
            it.findNavController().navigate(R.id.action_featuresFragment_to_exerciseActivity)
        }
        binding.lewati.setOnClickListener{
            it.findNavController().navigate(R.id.action_featuresFragment_to_assessmentStartFragment)
        }
        binding.readingAnimation.setBackgroundResource(R.drawable.book_animation_list)
        readAnimation = binding.readingAnimation.background as AnimationDrawable
        readAnimation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}