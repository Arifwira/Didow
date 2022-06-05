package com.capstone.didow.UI.profile

import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.didow.R
import com.capstone.didow.databinding.AvatarFragmentBinding
import com.capstone.didow.databinding.ProfileFragmentBinding

class AvatarFragment : Fragment() {

    private var _binding : AvatarFragmentBinding? = null
    private lateinit var readAnimation : AnimationDrawable
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AvatarFragment()
    }

    private lateinit var viewModel: AvatarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AvatarFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var anim = 1
        binding.apply {
            simpanAva.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container_main,ProfileFragment()).commit()
            }
            changeAva.setOnClickListener {
                if (anim == 1) {
                    avatarnih.setBackgroundResource(R.drawable.avadog_animation)
                    readAnimation = binding.avatarnih.background as AnimationDrawable
                    readAnimation.start()
                    anim += 1
                }else{
                    avatarnih.setBackgroundResource(R.drawable.avacat_animation)
                    readAnimation = binding.avatarnih.background as AnimationDrawable
                    readAnimation.start()
                    anim -= 1
                }
            }
            avatarnih.setBackgroundResource(R.drawable.avacat_animation)
        }
            readAnimation = binding.avatarnih.background as AnimationDrawable
            readAnimation.start()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AvatarViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}