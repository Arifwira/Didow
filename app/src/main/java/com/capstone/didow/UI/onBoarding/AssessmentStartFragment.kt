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
import com.capstone.didow.databinding.FragmentAssessmentStartBinding
import com.capstone.didow.databinding.OnBoardingFragmentBinding


class AssessmentStartFragment : Fragment() {
    private var _binding: FragmentAssessmentStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var readAnimation : AnimationDrawable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAssessmentStartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mulaiAsessment.setOnClickListener {
            it.findNavController().navigate(R.id.action_assessmentStartFragment_to_exerciseActivity)
        }
        binding.kembaliAssesment.setOnClickListener {
            it.findNavController().navigate(R.id.action_assessmentStartFragment_to_featuresFragment)
        }
        binding.animasi.setBackgroundResource(R.drawable.read_animation_list)
        readAnimation = binding.animasi.background as AnimationDrawable
        readAnimation.start()
        playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun playAnimation() {
//        val heading = ObjectAnimator.ofFloat(binding.HeadingAsessment, View.ALPHA,1f).setDuration(50)
//        val deskribsi = ObjectAnimator.ofFloat(binding.deskribsi, View.ALPHA,1f).setDuration(200)
//        val gambar = ObjectAnimator.ofFloat(binding.animasi, View.ALPHA,1f).setDuration(200)
        val kembali = ObjectAnimator.ofFloat(binding.kembaliAssesment, View.ALPHA,1f).setDuration(200)
        val mulai = ObjectAnimator.ofFloat(binding.mulaiAsessment, View.ALPHA,1f).setDuration(200)
        val bgLeaves2 =  ObjectAnimator.ofFloat(binding.bgLeaves2, View.ALPHA, 1f).setDuration(1500)
        val bgLeaves3 =  ObjectAnimator.ofFloat(binding.bgLeaves3, View.ALPHA, 1f).setDuration(1500)

        val bg_X1 = ObjectAnimator.ofFloat(binding.bgLeaves1, View.TRANSLATION_X, -1050f, 1050f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }

        val bg_Y1 = ObjectAnimator.ofFloat(binding.bgLeaves1, View.TRANSLATION_Y, -350f, 350f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val bg_X2 = ObjectAnimator.ofFloat(binding.bgLeaves2, View.TRANSLATION_X, -1050f, 1050f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }

        val bg_Y2 = ObjectAnimator.ofFloat(binding.bgLeaves2, View.TRANSLATION_Y, -350f, 350f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val bg_X3 = ObjectAnimator.ofFloat(binding.bgLeaves3, View.TRANSLATION_X, -1050f, 1050f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }
        val bg_Y3 = ObjectAnimator.ofFloat(binding.bgLeaves3, View.TRANSLATION_Y, 350f, -350f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val top_X = ObjectAnimator.ofFloat(binding.topLeaves, View.TRANSLATION_Y, -15f, 0f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val top_Y = ObjectAnimator.ofFloat(binding.topLeaves, View.TRANSLATION_X, -15f, -0f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val bottom_X =
            ObjectAnimator.ofFloat(binding.bottomLeaves, View.TRANSLATION_Y, -0f, 15f).apply {
                duration = 1500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        val bottom_Y =
            ObjectAnimator.ofFloat(binding.bottomLeaves, View.TRANSLATION_X, -0f, 15f).apply {
                duration = 1500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        val sequences = AnimatorSet().apply {
            playSequentially(kembali,mulai)
        }
        val topLeaves = AnimatorSet().apply {
            playTogether(top_X, top_Y)
        }
        val bottomLeaves = AnimatorSet().apply {
            playTogether(bottom_X,bottom_Y)
        }
        val bg1 = AnimatorSet().apply {
            playTogether(bg_X1,bg_Y1)
        }
        val bg2 = AnimatorSet().apply {
            playTogether(bg_X2,bg_Y2)
        }
        val bg3 = AnimatorSet().apply {
            playTogether(bg_X3,bg_Y3)
        }

        AnimatorSet().apply {
            playTogether(topLeaves,bg1,sequences)
            play(bg3).after(750)
            playTogether(bg3,bgLeaves3,bottomLeaves)
            play(bg2).after(2000)
            playTogether(bg2,bgLeaves2)
            start()
        }
    }
}