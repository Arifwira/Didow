package com.capstone.didow.UI.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.didow.R
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.UI.history.HistoryDetailFragment
import com.capstone.didow.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val message = bundle!!.getString("NAMA")
            binding.textView2.text = message
        }
        binding.apply {
            auth = FirebaseAuth.getInstance()
            editProfile.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container_main, EditProfile())
                    .commit()
            }
            customization.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_main, AvatarFragment()).commit()
            }
            changePassword.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_main, ChangePasswordFragment()).commit()
            }
            logout.setOnClickListener {
                logout()
                startActivity(Intent(activity, OnBoarding::class.java))
                activity?.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun logout() {
        auth.signOut()
    }

}