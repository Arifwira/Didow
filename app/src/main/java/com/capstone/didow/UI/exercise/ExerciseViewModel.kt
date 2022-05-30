package com.capstone.didow.UI.exercise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.didow.api.RetrofitInstance
import com.capstone.didow.entities.*
import com.capstone.didow.api.QuestionsResponse
import kotlinx.coroutines.launch

class ExerciseViewModel : ViewModel() {
    private val _exercise = MutableLiveData<Exercise>()
    private val _currentQuestion = MutableLiveData<Question>()
    private val _isLoaded = MutableLiveData<Boolean>(false)
    private val _currentAttempt = MutableLiveData<Attempt>()
    private val _isRetry = MutableLiveData(false)
    private val _isFinished = MutableLiveData(false)

    val exercise: LiveData<Exercise> = _exercise
    val currentQuestion: LiveData<Question> = _currentQuestion
    val isLoaded: LiveData<Boolean> = _isLoaded
    val currentAttempt: LiveData<Attempt> = _currentAttempt
    val isRetry: LiveData<Boolean> = _isRetry
    val isFinished: LiveData<Boolean> = _isFinished


    fun init(category: String, userId: String) {
        val client = RetrofitInstance.getApiService()
        viewModelScope.launch {
            val userInfo = client.getUser(userId, null)
            var response: QuestionsResponse? = null

            when (category) {
                "auto" -> response = client.getQuestions(category, userInfo.data?.weightPoint)
                "assessment" -> response = client.getQuestions(category, null)
            }

            val data = response?.data
            val questions = mutableListOf<Question>()
            data?.forEach {
                val type = it?.type
                var question: Question? = null
                when(type) {
                    "multipleChoice" -> question = QuestionMultipleChoice(
                        it.word!!,
                        it.syllables!!,
                        it.hintImg!!,
                        it.multipleChoice!!.choices!! as List<String>
                    )
                    "scrambleWords" -> question = QuestionScrambleWords(
                        it.word!!,
                        it.syllables!!,
                        it.hintImg!!,
                        it.scrambleWords!!.letters!! as List<String>,
                        it.scrambleWords.hintHangman!! as List<String>
                    )
                    "handwriting" -> question = QuestionHandwriting(
                        it.word!!,
                        it.syllables!!,
                        it.hintImg!!,
                        it.handWriting!!.hintHangman!! as List<String>
                    )
                }
                questions.add(question!!)
            }
            _exercise.value = Exercise(questions, category)
            _isLoaded.value = true
        }
    }

    fun startExercise() {
        this._currentQuestion.value = this.exercise.value?.start()
        _currentAttempt.value = _exercise.value?.getCurentAttempt()
    }

    fun answer(ans: String): Boolean {
        return this.currentAttempt.value?.answer(this.currentQuestion.value!!, ans)!!
    }

    fun nextQuestion() {
        _isRetry.value = false
        val newQuestion = this.currentAttempt.value?.next()
        if (newQuestion == null) {
            val newAttempt = this.currentAttempt.value?.checkWrongAnswers()
            if (newAttempt == null || exercise.value?.category !== "assessment") {
                finish()
            } else {
                _isRetry.value = true
                this.exercise.value?.addAttempt(newAttempt)
                _currentAttempt.value = _exercise.value?.getCurentAttempt()
                this._currentQuestion.value = this.exercise.value?.retry()
            }
        } else {
            this._currentQuestion.value = newQuestion
        }
    }

    fun finish() {
        Log.d("finished", exercise.value.toString())
        _isFinished.value = true
    }

    fun getExerciseCategory(): String {
        return exercise.value!!.category
    }
}
