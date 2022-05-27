package com.capstone.didow.UI.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {

    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var readAnimation : AnimationDrawable

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.daftarMasuk.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.daftarPaw.setBackgroundResource(R.drawable.paw_animation)
        readAnimation = binding.daftarPaw.background as AnimationDrawable
        readAnimation.start()
        playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun playAnimation() {
        val bgLeaves2 =  ObjectAnimator.ofFloat(binding.bgLeaves2, View.ALPHA, 1f).setDuration(1500)
        val bgLeaves3 =  ObjectAnimator.ofFloat(binding.bgLeaves3, View.ALPHA, 1f).setDuration(1500)
        val pawalpha = ObjectAnimator.ofFloat(binding.daftarPaw, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.HeadingRegist, View.ALPHA, 1f).setDuration(300)
        val desclogin = ObjectAnimator.ofFloat(binding.registerDesc, View.ALPHA, 1f).setDuration(300)
        val nama = ObjectAnimator.ofFloat(binding.daftarNama, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.daftarEmail, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.daftarPassword, View.ALPHA, 1f).setDuration(300)
        val masuk = ObjectAnimator.ofFloat(binding.daftarDaftar, View.ALPHA, 1f).setDuration(300)
        val daftar = ObjectAnimator.ofFloat(binding.daftarMasuk, View.ALPHA, 1f).setDuration(300)

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

        val paw = ObjectAnimator.ofFloat(binding.daftarPaw,View.TRANSLATION_Y,1000f,0f).apply {
            duration = 700
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
        val text = AnimatorSet().apply {
            playSequentially(login,desclogin,nama,email,password,masuk,daftar)
        }

        AnimatorSet().apply {
            playTogether(topLeaves,bg1,text)
            play(pawalpha).after(text)
            playTogether(paw,pawalpha)
            play(bg3).after(750)
            playTogether(bg3,bgLeaves3,bottomLeaves)
            play(bg2).after(2000)
            playTogether(bg2,bgLeaves2)
            start()
        }
    }
}