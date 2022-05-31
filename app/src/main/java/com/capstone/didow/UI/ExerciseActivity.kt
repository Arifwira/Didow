package com.capstone.didow.UI

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.capstone.didow.R
import com.capstone.didow.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var _binding : ActivityExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"asdas",Toast.LENGTH_SHORT).show()
    }


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