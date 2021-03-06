package com.capstone.didow.UI.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewbinding.ViewBindings
import com.capstone.didow.R
import com.capstone.didow.UI.ExerciseActivity
import com.capstone.didow.databinding.HomeFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var bottomSheetDialogFragment: ModeSettingFragment
    private var mode = "normal"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        mode = sharedPref.getString("mode", "normal").toString()
        binding.apply {
            btnModeSetting.text = mode
            play.setOnClickListener {
                when (mode) {
                    "normal" -> {
                        val intent = Intent(activity, ExerciseActivity::class.java)
                        intent.putExtra("category", "auto")
                        intent.putExtra("allowRetry", true)
                        startActivity(intent)
                    }
                    "kustom" -> {
                        val popupCustom = CustomizationFragment()
                        popupCustom.show(childFragmentManager, "PopupCustomization Fragment")
                    }
                }
            }
        }
        bottomSheetDialogFragment = ModeSettingFragment()
        selectMode()
        playAnimation()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }



    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.play, View.SCALE_X, 1f, 1.1f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        ObjectAnimator.ofFloat(binding.play, View.SCALE_Y, 1f, 1.1f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun selectMode() {
        val bundle = Bundle()
        bundle.putString("mode", mode)
        bottomSheetDialogFragment.arguments = bundle
        binding.btnModeSetting.setOnClickListener {
            bottomSheetDialogFragment.show(childFragmentManager, "Mode Setting Dialog")
        }
    }

    fun setResultAsMode(mode: String) {
        this.mode = mode
        sharedPref.edit().putString("mode", mode).apply()
        binding.btnModeSetting.text = mode
        selectMode()
    }
}