package com.capstone.didow.UI.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.didow.R
import com.capstone.didow.databinding.EditProfileFragmentBinding
import com.capstone.didow.databinding.ProfileFragmentBinding

class EditProfile : Fragment() {

    private var _binding : EditProfileFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = EditProfile()
    }

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            simpan.setOnClickListener {

                parentFragmentManager.beginTransaction().replace(R.id.container_main, ProfileFragment()).commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }

}