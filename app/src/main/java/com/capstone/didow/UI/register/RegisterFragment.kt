package com.capstone.didow.UI.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.api.ApiService
import com.capstone.didow.api.RetrofitInstance
import com.capstone.didow.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RegisterFragment : Fragment() {

    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var readAnimation : AnimationDrawable
    private lateinit var auth: FirebaseAuth
    private lateinit var client: ApiService

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
        binding.daftarNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.daftarEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.daftarPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.daftarPaw.setBackgroundResource(R.drawable.paw_animation)
        readAnimation = binding.daftarPaw.background as AnimationDrawable
        readAnimation.start()
        playAnimation()
        auth = FirebaseAuth.getInstance()
        client = RetrofitInstance.getApiService()
        binding.apply {
            daftarDaftar.setOnClickListener {
                val email = daftarEmail.text.toString()
                val password = daftarPassword.text.toString()
                signUp(email, password)
            }
        }
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
        val name = binding.daftarNama.text
        val email = binding.daftarEmail.text
        val pass = binding.daftarPassword.text
        binding.daftarDaftar.isEnabled =
            pass != null && isValidPassword(pass.toString()) && email != null && isValidEmail(email.toString()) && name.isNotEmpty()
        if(binding.daftarDaftar.isEnabled){
            binding.daftarDaftar.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))
        }else{
            binding.daftarDaftar.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
        }
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

    private fun signUp(email: String, password: String) {
        binding.apply {
            darkBg.visibility = View.VISIBLE
            catRegister.visibility = View.VISIBLE
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGN_UP", "createUserWithEmail:success")
                    val user = auth.currentUser
                    lifecycleScope.launch {
                        try {
                            val jsonObject = JSONObject()
                            jsonObject.put("id", user!!.uid)
                            jsonObject.put("username", binding.daftarNama.text.toString())
                            jsonObject.put("weightPoint", 100)
                            val requestBody =jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                            // Create user data in db
                            client.createUser(requestBody)
                            findNavController().navigate(R.id.action_registerFragment_to_mainActivity)
                            binding.apply {
                                darkBg.visibility = View.GONE
                                catRegister.visibility = View.GONE
                            }
                            activity?.finish()
                        } catch (error: Error) {
                            binding.apply {
                                darkBg.visibility = View.GONE
                                catRegister.visibility = View.GONE
                            }
                            Log.w("SIGN_UP", "createUserWithEmail:failure", error)
                            Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    binding.apply {
                        darkBg.visibility = View.GONE
                        catRegister.visibility = View.GONE
                    }
                    // If sign in fails, display a message to the user.
                    Log.w("SIGN_UP", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}