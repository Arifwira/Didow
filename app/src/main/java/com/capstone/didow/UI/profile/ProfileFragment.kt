package com.capstone.didow.UI.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.didow.R
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.UI.history.HistoryDetailFragment
import com.capstone.didow.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {

    private var _binding : ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var readAnimation : AnimationDrawable

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            editProfile.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container_main, EditProfile()).commit()
            }
            customization.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container_main, AvatarFragment()).commit()
            }
            changePassword.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container_main, ChangePasswordFragment()).commit()
            }
            logout.setOnClickListener {
                startActivity(Intent(activity, OnBoarding::class.java))
                activity?.finish()
            }
        }
        binding.aum.setBackgroundResource(R.drawable.lion_list)
        readAnimation = binding.aum.background as AnimationDrawable
        readAnimation.start()
        playAnimation()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun playAnimation() {

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
            playTogether(bg3,bottomLeaves)
            play(bg2).after(2000)
            start()
        }
    }
}