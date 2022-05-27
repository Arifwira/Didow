package com.capstone.didow.UI.onBoarding

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.OnBoardingFragmentBinding

class OnBoardingFragment : Fragment() {

    private var _binding: OnBoardingFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = OnBoardingFragment()
    }

    private lateinit var viewModel: OnBoardingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OnBoardingFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startTour.setOnClickListener {
            it.findNavController().navigate(R.id.action_onBoardingFragment_to_featuresFragment)
        }
        binding.login.setOnClickListener {
            it.findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }

        playAnimation()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.headIcon, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        ObjectAnimator.ofFloat(binding.headIcon, View.ROTATION, -5f, 5f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        ObjectAnimator.ofFloat(binding.headIcon, View.TRANSLATION_Y, -15f, 15f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}