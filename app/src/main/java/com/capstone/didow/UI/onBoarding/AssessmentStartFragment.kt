package com.capstone.didow.UI.onBoarding

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.databinding.FragmentAssessmentStartBinding


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
//            it.findNavController().navigate(R.id.action_assessmentStartFragment_to_exerciseActivity)
            val intent = Intent(activity, ExerciseActivity::class.java)
            intent.putExtra("category", "assessment")
            startActivity(intent)
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
        val kembali = ObjectAnimator.ofFloat(binding.kembaliAssesment, View.ALPHA,1f).setDuration(200)
        val mulai = ObjectAnimator.ofFloat(binding.mulaiAsessment, View.ALPHA,1f).setDuration(200)

        val sequences = AnimatorSet().apply {
            playSequentially(kembali,mulai)
        }

        AnimatorSet().apply {
            playTogether(sequences)
            start()
        }
    }
}