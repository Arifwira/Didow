package com.capstone.didow.UI

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.UI.register.RegisterFragment
import com.capstone.didow.databinding.ActivityOnBoardingBinding

class OnBoarding : AppCompatActivity() {
    private var _binding : ActivityOnBoardingBinding? = null
    private val binding get() = _binding!!

    val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ExerciseActivity.RESULT_CODE && result.data != null) {
            saveAssessmentReportToSharedPref(result.data)
            findNavController(R.id.container_onBoarding).navigate(R.id.action_assessmentStartFragment_to_assessmentCompleteFragment2)
        }
    }

    private fun saveAssessmentReportToSharedPref(intent: Intent?) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putFloat("multipleChoice", intent!!.getFloatExtra("multipleChoice", 0F))
            putFloat("scrambleWords", intent.getFloatExtra("scrambleWords", 0F))
            putFloat("handwriting", intent.getFloatExtra("handwriting", 0F))
            putInt("score", intent.getIntExtra("score", 0))
            putBoolean("assessmentIsCompleted", true)
            apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding?.root)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val assessmentIsCompleted = sharedPref.getBoolean("assessmentIsCompleted", false)
        if (assessmentIsCompleted) {
            findNavController(R.id.container_onBoarding).navigate(R.id.action_onBoardingFragment_to_registerFragment)
        }
        playAnimation()
    }

//    override fun onResume() {
//        Log.i("TEST","${supportFragmentManager.fragments.size}")
//        super.onResume()
//        for (n in 0..supportFragmentManager.fragments.size){
//            if (n==supportFragmentManager.fragments.size-1) break
//            else supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments[n]).commit()
//        }
//    }
private fun playAnimation() {
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
        playTogether(topLeaves,bg1)
        play(bg3).after(750)
        playTogether(bg3,bgLeaves3,bottomLeaves)
        play(bg2).after(2000)
        playTogether(bg2,bgLeaves2)
        start()
    }
}
}