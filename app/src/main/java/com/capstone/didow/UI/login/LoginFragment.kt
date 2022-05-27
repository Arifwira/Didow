package com.capstone.didow.UI.login

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
import com.capstone.didow.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var readAnimation : AnimationDrawable
    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.Daftar.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.masuk.setOnClickListener{
            it.findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        }
        binding.paw.setBackgroundResource(R.drawable.paw_animation)
        readAnimation = binding.paw.background as AnimationDrawable
        readAnimation.start()
        playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun playAnimation() {
        val pawalpha = ObjectAnimator.ofFloat(binding.paw, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.HeadingLogin, View.ALPHA, 1f).setDuration(300)
        val desclogin = ObjectAnimator.ofFloat(binding.loginDesc, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.editTextTextEmailAddress, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.editTextTextPassword, View.ALPHA, 1f).setDuration(300)
        val masuk = ObjectAnimator.ofFloat(binding.masuk, View.ALPHA, 1f).setDuration(300)
        val daftar = ObjectAnimator.ofFloat(binding.Daftar, View.ALPHA, 1f).setDuration(300)
        val paw = ObjectAnimator.ofFloat(binding.paw,View.TRANSLATION_Y,1000f,0f).apply {
            duration = 700
        }
        val text = AnimatorSet().apply {
            playSequentially(login,desclogin,email,password,masuk,daftar)
        }
        AnimatorSet().apply {
            playTogether(text)
            play(pawalpha).after(text)
            playTogether(paw,pawalpha)
            start()
        }
    }
}