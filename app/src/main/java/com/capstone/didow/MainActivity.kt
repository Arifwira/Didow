package com.capstone.didow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.UI.history.HistoryFragment
import com.capstone.didow.UI.home.HomeFragment
import com.capstone.didow.UI.profile.ProfileFragment
import com.capstone.didow.databinding.ActivityMainBinding
import com.capstone.didow.databinding.RegisterFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        startActivity(Intent(this, OnBoarding::class.java))
//        finish()

        val firstFragment=HomeFragment()
        val secondFragment=HistoryFragment()
        val thirdFragment=ProfileFragment()

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
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main,fragment)
            commit()
        }

}
