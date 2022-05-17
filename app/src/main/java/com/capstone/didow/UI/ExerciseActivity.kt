package com.capstone.didow.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.capstone.didow.R

class ExerciseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"asdas",Toast.LENGTH_SHORT).show()
    }
}