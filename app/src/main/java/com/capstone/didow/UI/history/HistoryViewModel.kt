package com.capstone.didow.UI.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.didow.api.GetExercisesResponse
import com.capstone.didow.api.RetrofitInstance
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HistoryViewModel : ViewModel() {
    private val _history = MutableLiveData<List<History>>()
    private val _userId = MutableLiveData<String>()

    val history: LiveData<List<History>> = _history

    private val auth = Firebase.auth
    private var currentUser: FirebaseUser? = null

    fun init(userId: String, startDate: String, endDate: String, timeZone: Int){

        Log.d("startDate", startDate)
        Log.d("endDate", endDate)

        currentUser = auth.currentUser

        if (currentUser != null) {
            _userId.value = currentUser!!.uid
        }

        val client = RetrofitInstance.getApiService()
        viewModelScope.launch {

            val response: GetExercisesResponse?

            val userToken = currentUser!!.getIdToken(true).await().token
            response = client.getExercises(_userId.value, null, startDate, endDate, timeZone, userToken!!)
            Log.d("response", response.data.toString())

            val data = response?.data
            val histories = mutableListOf<History>()
            data?.forEach {
                var history = History(
                    it?.userId!!,
                    it?.endTime!!,
                    it?.avgSyllables!!,
                    it?.questionsQty!!,
                    it?.wrongAnswers!!.map{ wrongAnswerData ->
                        var wrongAnswers = WrongAnswers(
                            wrongAnswerData?.number!!,
                            wrongAnswerData?.word!!,
                            wrongAnswerData?.type!!,
                            wrongAnswerData.attempts!!.map{ attemptData ->
                                val attempt = WrongAttempts(
                                    attemptData?.attemptNumber!!,
                                    attemptData?.answer!!
                                )
                                attempt
                            }
                        )
                        wrongAnswers
                    }
                )
                histories.add(history)
            }
            _history.value = histories
            Log.d("Histories Data", histories.toString())
        }
    }
}