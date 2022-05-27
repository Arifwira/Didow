package com.capstone.didow.UI.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.didow.R
import com.capstone.didow.databinding.AvatarFragmentBinding
import com.capstone.didow.databinding.ChangePasswordFragmentBinding

class ChangePasswordFragment : Fragment() {

    private var _binding : ChangePasswordFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChangePasswordFragmentBinding.inflate(inflater,container,false)
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
                parentFragmentManager.beginTransaction().replace(R.id.container_main, ProfileFragment()).commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}