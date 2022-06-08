package com.capstone.didow.UI.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.capstone.didow.R
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.UI.history.HistoryDetailFragment
import com.capstone.didow.UI.history.HistoryFragment
import com.capstone.didow.UI.home.HomeFragment
import com.capstone.didow.api.GetUserResponse
import com.capstone.didow.api.RetrofitInstance
import com.capstone.didow.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var readAnimation: AnimationDrawable
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private val viewModel: ProfileViewModel by activityViewModels()
    val PREF = "SharedPreference"
    val UID = "userID"
    val ANIM = "1"
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        fun newInstance() = ProfileFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE)
        auth = Firebase.auth
        val uid = sharedPreferences.getString(UID, null)
        binding.textView2.text = uid
        var anim = sharedPreferences.getString(ANIM, null).toString()
        Log.d("ANIM", sharedPreferences.getString(ANIM, null).toString())
        Log.d("ANIM", anim)
        when (anim) {
            "kucing" -> binding.avatar.setBackgroundResource(R.drawable.cathead_animation)
            else -> {
                binding.avatar.setBackgroundResource(R.drawable.doghead_animation)
            }
        }
        readAnimation = binding.avatar.background as AnimationDrawable
        readAnimation.start()

        if (uid != null) {
            viewModel.getUserNickname(uid)
            viewModel.nick.observe(viewLifecycleOwner, Observer {
                binding.textView2.text = it
            })
        }

        val firstFragment = EditProfile()
        val secondFragment = AvatarFragment()
        val thirdFragment = ChangePasswordFragment()

        binding.apply {
            auth = FirebaseAuth.getInstance()
            editProfile.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container_main, firstFragment)
                    .commit()
            }
            customization.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_main, secondFragment).commit()
            }
            changePassword.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_main, thirdFragment).commit()
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