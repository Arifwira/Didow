package com.capstone.didow.UI.profile

import android.util.Log
import androidx.lifecycle.*
import com.capstone.didow.api.GetUserResponse
import com.capstone.didow.api.RetrofitInstance
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private val _nickname = MutableLiveData<String?>()
    val nick : LiveData<String?> = _nickname


    private val auth = Firebase.auth

    fun getUserNickname(id: String) {
        viewModelScope.launch {
            val client = RetrofitInstance.getApiService()
            var userInfo: GetUserResponse? = null
            if (auth.currentUser != null) {
                val userToken = auth.currentUser!!.getIdToken(true).await().token
                userInfo = client.getUser(id, null, userToken!!)
                Log.d("NICKNAME", "${userToken}")
            }
            val nick = userInfo?.data?.nickname
            if (nick != null) {
                _nickname.value = nick
            }
            Log.d("NICKNAME", "${userInfo?.data?.nickname}")
        }
    }


}