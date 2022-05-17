package com.capstone.didow.UI

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.UI.register.RegisterFragment
import com.capstone.didow.databinding.ActivityOnBoardingBinding

class OnBoarding : AppCompatActivity() {
    private var _binding : ActivityOnBoardingBinding? = null
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val intent = getIntent().extras?.getString("TARGET")
        Log.i("TEST","$intent")
        if (intent=="RegisterFragment") {
            Log.i("TESTIF", "$intent")
            findNavController(R.id.container_onBoarding).navigate(R.id.action_onBoardingFragment_to_registerFragment)
        }
    }

//    override fun onResume() {
//        Log.i("TEST","${supportFragmentManager.fragments.size}")
//        super.onResume()
//        for (n in 0..supportFragmentManager.fragments.size){
//            if (n==supportFragmentManager.fragments.size-1) break
//            else supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments[n]).commit()
//        }
//    }
}