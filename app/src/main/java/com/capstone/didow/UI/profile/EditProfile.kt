package com.capstone.didow.UI.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.didow.R
import com.capstone.didow.api.ApiService
import com.capstone.didow.api.RetrofitInstance
import com.capstone.didow.databinding.EditProfileFragmentBinding
import com.capstone.didow.databinding.ProfileFragmentBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class EditProfile : Fragment() {

    private var _binding : EditProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var client: ApiService
    val PREF = "SharedPreference"
    val UID = "userID"
    lateinit var  sharedPreferences: SharedPreferences


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
        val bundle = arguments
        sharedPreferences = requireActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString(UID,null)
        Log.d("EDITPROFILE","${sharedPreferences.getString(UID, null)}")
        client = RetrofitInstance.getApiService()

        binding.apply {
            simpanProfile.setOnClickListener {
                if (uid != null) {
                    editProfile(uid)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }

    private fun editProfile(id: String){
        lifecycleScope.launch {
            try {
                val radioGroup = binding.radioGroup
                val selected = radioGroup.checkedRadioButtonId
                val button = view?.findViewById<RadioButton>(selected)
                val gender = button?.text
                val jenis : String = when(gender){
                    "Laki-laki" -> "male"
                    else -> {
                        "female"
                    }
                }
                if (binding.editTextDate.text.isNotEmpty() && binding.namaProfile.text.isNotEmpty() && jenis.isNotEmpty()){

                Log.d("Edit", gender.toString())
                println(binding.editTextDate.text.toString())
                Log.d("Edit", binding.editTextDate.text.toString())
                val jsonObject = JSONObject()
                jsonObject.put("nickname", binding.namaProfile.text)
                jsonObject.put("birthDate", binding.editTextDate.text)
                jsonObject.put("gender", jenis)
                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                client.editUser(id,requestBody)
                binding.apply {
                    darkBg.visibility = View.GONE
                    catLogin.visibility = View.GONE
                }
                val args = Bundle().apply {
                    putString("ID",id)
                    putString("NAMA","${binding.namaProfile.text}")
                }
                val profileFragment = ProfileFragment()
                profileFragment.arguments = args
                parentFragmentManager.beginTransaction().replace(R.id.container_main, profileFragment).commit()
                }else{
                    Toast.makeText(this@EditProfile.context,"ISI SEMUA KOLOM DAHULU",Toast.LENGTH_SHORT).show()
                }

            } catch (error: Error) {
                binding.apply {
                    darkBg.visibility = View.GONE
                    catLogin.visibility = View.GONE
                }
                Log.w("EDITPROFILE", "editUserWithEmail:failure", error)
                Toast.makeText(context, "EDIT failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}