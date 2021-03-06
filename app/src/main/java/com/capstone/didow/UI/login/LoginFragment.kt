package com.capstone.didow.UI.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var readAnimation : AnimationDrawable
    private lateinit var auth: FirebaseAuth



    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPref: SharedPreferences

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

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val assessmentIsCompleted = sharedPref.getBoolean("assessmentIsCompleted", false)
        if (!assessmentIsCompleted) {
            binding.Daftar.visibility = View.INVISIBLE
        }

        binding.Daftar.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.editTextTextEmailAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.editTextTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.masuk.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            sharedPref.edit().clear().commit()
            signIn(email, password)
        }
        binding.paw.setBackgroundResource(R.drawable.paw_animation)
        readAnimation = binding.paw.background as AnimationDrawable
        readAnimation.start()
        playAnimation()
        auth = FirebaseAuth.getInstance()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun isValidPassword(str: String): Boolean {
        return str.length >= 6
    }

    private fun enableButton() {
        val email = binding.editTextTextEmailAddress.text
        val pass = binding.editTextTextPassword.text
        binding.masuk.isEnabled =
            pass != null && isValidPassword(pass.toString()) && email != null && isValidEmail(email.toString())
        if(binding.masuk.isEnabled){
            binding.masuk.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))
        }else{
            binding.masuk.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
        }
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

    private fun signIn(email: String, password: String) {
        binding.apply {
            darkBg.visibility = View.VISIBLE
            catLogin.visibility = View.VISIBLE
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.apply {
                        darkBg.visibility = View.GONE
                        catLogin.visibility = View.GONE
                    }
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGN_IN", "signInWithEmail:success")
                    val user = auth.currentUser
                    findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    this.activity?.finish()
                } else {
                    binding.apply {
                        darkBg.visibility = View.GONE
                        catLogin.visibility = View.GONE
                    }
                    // If sign in fails, display a message to the user.
                    Log.w("SIGN_IN", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this.context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}