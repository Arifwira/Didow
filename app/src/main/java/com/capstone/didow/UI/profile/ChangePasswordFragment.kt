package com.capstone.didow.UI.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.databinding.AvatarFragmentBinding
import com.capstone.didow.databinding.ChangePasswordFragmentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordFragment : Fragment() {

    private var _binding: ChangePasswordFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChangePasswordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            simpanPassword.setOnClickListener {
                val passlama = binding.passwordLama.text.toString()
                val passbaru = binding.passwordBaru.text.toString()
                Log.d("CHANGE", passbaru)
                if (passbaru.isNotEmpty() && passlama.isNotEmpty()) {
                    changePass(passlama, passbaru)
                } else {
                    Toast.makeText(
                        this@ChangePasswordFragment.context,
                        "ISI KOLOM PASSWORD",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun changePass(passlama: String?, passbaru: String?) {
        val user = Firebase.auth.currentUser
        val auth = Firebase.auth
        binding.apply {
            darkBg.visibility = View.VISIBLE
            catRegister.visibility = View.VISIBLE
        }
        if (user != null) {
            user.email?.let {
                if (passlama != null) {
                    auth.signInWithEmailAndPassword(it, passlama)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGN_IN", "signInWithEmail:success")
                                if (passbaru != null) {
                                    user.updatePassword(passbaru)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                binding.apply {
                                                    darkBg.visibility = View.GONE
                                                    catRegister.visibility = View.GONE
                                                }
                                                Log.d("UBAH", "User password updated.")
                                                parentFragmentManager.beginTransaction()
                                                    .replace(R.id.container_main, ProfileFragment())
                                                    .commit()
                                            } else {
                                                Log.d("UBAH", "User password GAGAL.")
                                            }
                                        }
                                }
                            } else {
                                binding.apply {
                                    darkBg.visibility = View.GONE
                                    catRegister.visibility = View.GONE
                                }
                                // If sign in fails, display a message to the user.
                                Log.w("SIGN_IN", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    this@ChangePasswordFragment.context, "Password lama salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}