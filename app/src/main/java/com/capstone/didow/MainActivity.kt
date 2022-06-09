package com.capstone.didow

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.UI.history.HistoryFragment
import com.capstone.didow.UI.home.HomeFragment
import com.capstone.didow.UI.profile.ProfileFragment
import com.capstone.didow.api.GetUserResponse
import com.capstone.didow.api.RetrofitInstance
import com.capstone.didow.databinding.ActivityMainBinding
import com.capstone.didow.databinding.RegisterFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var data: FirebaseFirestore
    private var musicPlayer: MediaPlayer? = null
    val PREF = "SharedPreference"
    val UID = "userID"
    val ANIM = "1"
    lateinit var  sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        startActivity(Intent(this, OnBoarding::class.java))
//        finish()
        sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, OnBoarding::class.java))
            finish()
        }
        val uid = auth.currentUser?.uid
        sharedPreferences.edit().putString(UID,"$uid").apply()
        data = Firebase.firestore

        val firstFragment=HomeFragment()
        val secondFragment=HistoryFragment()
        val thirdFragment=ProfileFragment()

        if (uid != null) {
            lifecycleScope.launch {
                val client = RetrofitInstance.getApiService()
                var userInfo: GetUserResponse? = null
                if (auth.currentUser != null) {
                    val userToken = auth.currentUser!!.getIdToken(true).await().token
                    userInfo = client.getUser(uid,null, userToken!!)
                    Log.d("TOKEN", "${userToken}")
                    val bundle = Bundle().apply {
                        putString("ID","$uid")
                        putString("NAMA","${userInfo.data?.nickname}")
                    }
                    thirdFragment.arguments = bundle
                }
                Log.d("NICKNAME", "${userInfo?.data?.nickname}")
            }
        }
        setCurrentFragment(firstFragment)


        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_1 -> {
                    setCurrentFragment(firstFragment)
                    true
                }
                R.id.page_2 -> {
                    setCurrentFragment(secondFragment)
                    true
                }

                R.id.page_3 -> {
                    setCurrentFragment(thirdFragment)
                    true
                }
                else -> false
            }
        }
        playAnimation()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"asdas", Toast.LENGTH_SHORT).show()
    }
    override fun onPause() {
        super.onPause()
        if(musicPlayer != null){
            musicPlayer!!.release()
            musicPlayer = null
        }
    }

    override fun onResume() {
        super.onResume()
        playMusic()
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main,fragment)
            commit()
        }

    private fun playMusic() {
        musicPlayer = MediaPlayer.create(this, R.raw.lo_fi)
        musicPlayer?.apply {
            setVolume(0.1f, 0.1f)
            start()
        }
        binding.musicToggle.setOnClickListener {
            if (musicPlayer!!.isPlaying) {
                binding.musicToggle.setBackgroundResource(R.drawable.musicpause)
                musicPlayer!!.pause()
            } else {
                binding.musicToggle.setBackgroundResource(R.drawable.musicplay)
                musicPlayer!!.start()
            }
        }
    }

    private fun playAnimation() {
        val bgLeaves2 = ObjectAnimator.ofFloat(binding.bgLeaves2, View.ALPHA, 1f).setDuration(1500)
        val bgLeaves3 = ObjectAnimator.ofFloat(binding.bgLeaves3, View.ALPHA, 1f).setDuration(1500)
        ObjectAnimator.ofFloat(binding.musicToggle, View.TRANSLATION_Y, -10f,10f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
        val bg_X1 =
            ObjectAnimator.ofFloat(binding.bgLeaves1, View.TRANSLATION_X, -1050f, 1050f).apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
            }

        val bg_Y1 =
            ObjectAnimator.ofFloat(binding.bgLeaves1, View.TRANSLATION_Y, -350f, 350f).apply {
                duration = 1500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        val bg_X2 =
            ObjectAnimator.ofFloat(binding.bgLeaves2, View.TRANSLATION_X, -1050f, 1050f).apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
            }

        val bg_Y2 =
            ObjectAnimator.ofFloat(binding.bgLeaves2, View.TRANSLATION_Y, -350f, 350f).apply {
                duration = 1500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        val bg_X3 =
            ObjectAnimator.ofFloat(binding.bgLeaves3, View.TRANSLATION_X, -1050f, 1050f).apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
            }
        val bg_Y3 =
            ObjectAnimator.ofFloat(binding.bgLeaves3, View.TRANSLATION_Y, 350f, -350f).apply {
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
            playTogether(bottom_X, bottom_Y)
        }
        val bg1 = AnimatorSet().apply {
            playTogether(bg_X1, bg_Y1)
        }
        val bg2 = AnimatorSet().apply {
            playTogether(bg_X2, bg_Y2)
        }
        val bg3 = AnimatorSet().apply {
            playTogether(bg_X3, bg_Y3)
        }

        AnimatorSet().apply {
            playTogether(topLeaves, bg1)
            play(bg3).after(750)
            playTogether(bg3, bgLeaves3, bottomLeaves)
            play(bg2).after(2000)
            playTogether(bg2, bgLeaves2)
            start()
        }
    }
}
