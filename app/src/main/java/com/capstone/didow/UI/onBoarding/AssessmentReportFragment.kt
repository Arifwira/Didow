package com.capstone.didow.UI.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.capstone.didow.R
import com.capstone.didow.UI.OnBoarding
import com.capstone.didow.databinding.FragmentAssessmentCompleteBinding
import com.capstone.didow.databinding.FragmentAssessmentReportBinding


class AssessmentReportFragment : Fragment() {
    private var _binding: FragmentAssessmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAssessmentReportBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            daftar.setOnClickListener{
                val intent = Intent(activity,OnBoarding::class.java)
                intent.putExtra("TARGET","RegisterFragment")
                startActivity(intent)
                activity?.finish()
            }
            keluar.setOnClickListener {
                activity?.finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}