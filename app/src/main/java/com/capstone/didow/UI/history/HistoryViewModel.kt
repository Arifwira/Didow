package com.capstone.didow.UI.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.didow.api.GetExercisesResponse
import com.capstone.didow.api.RetrofitInstance
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    private val _history = MutableLiveData<List<History>>()
    private val _userId = MutableLiveData<String>()

    val history: LiveData<List<History>> = _history

    fun init(userId: String, startDate: String, endDate: String, timeZone: Int){
        _userId.value = userId!!

        val client = RetrofitInstance.getApiService()
        viewModelScope.launch {

            val response: GetExercisesResponse?

            response = client.getExercises(_userId.value, null, startDate, endDate, timeZone)
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
            Log.d("Histories Data", histories.toString())
            _history.value = histories
        }
    }
}