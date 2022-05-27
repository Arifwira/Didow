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
        val pawalpha = ObjectAnimator.ofFloat(binding.daftarPaw, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.HeadingRegist, View.ALPHA, 1f).setDuration(300)
        val desclogin = ObjectAnimator.ofFloat(binding.registerDesc, View.ALPHA, 1f).setDuration(300)
        val nama = ObjectAnimator.ofFloat(binding.daftarNama, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.daftarEmail, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.daftarPassword, View.ALPHA, 1f).setDuration(300)
        val masuk = ObjectAnimator.ofFloat(binding.daftarDaftar, View.ALPHA, 1f).setDuration(300)
        val daftar = ObjectAnimator.ofFloat(binding.daftarMasuk, View.ALPHA, 1f).setDuration(300)

        val paw = ObjectAnimator.ofFloat(binding.daftarPaw,View.TRANSLATION_Y,1000f,0f).apply {
            duration = 700
        }

        val text = AnimatorSet().apply {
            playSequentially(login,desclogin,nama,email,password,masuk,daftar)
        }

        AnimatorSet().apply {
            playTogether(text)
            play(pawalpha).after(text)
            playTogether(paw,pawalpha)
            start()
        }
    }
}