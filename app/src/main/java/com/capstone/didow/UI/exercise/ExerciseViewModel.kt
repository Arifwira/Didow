package com.capstone.didow.UI.exercise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.didow.api.GetUserResponse
import com.capstone.didow.api.RetrofitInstance
import com.capstone.didow.entities.*
import com.capstone.didow.api.QuestionsResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ExerciseViewModel : ViewModel() {
    private val _exercise = MutableLiveData<Exercise>()
    private val _currentQuestion = MutableLiveData<Question>()
    private val _isLoaded = MutableLiveData<Boolean>(false)
    private val _currentAttempt = MutableLiveData<Attempt>()
    private val _isRetry = MutableLiveData(false)
    private val _isFinished = MutableLiveData(false)
    private val _userId = MutableLiveData<String>()
    private val _assessmentReport = MutableLiveData<AssessmentReport>()

    val exercise: LiveData<Exercise> = _exercise
    val currentQuestion: LiveData<Question> = _currentQuestion
    val isLoaded: LiveData<Boolean> = _isLoaded
    val currentAttempt: LiveData<Attempt> = _currentAttempt
    val isRetry: LiveData<Boolean> = _isRetry
    val isFinished: LiveData<Boolean> = _isFinished
    val assessmentReport: LiveData<AssessmentReport> = _assessmentReport


    fun init(category: String, userId: String?) {
        if (userId != null) {
            _userId.value = userId!!
        }
        val client = RetrofitInstance.getApiService()
        viewModelScope.launch {
            var userInfo: GetUserResponse? = null
            if (userId != null) {
                userInfo = client.getUser(userId, null)
            }
            var response: QuestionsResponse? = null

            when (category) {
                "auto" -> response = client.getQuestions(category, userInfo!!.data?.weightPoint)
                "assessment" -> response = client.getQuestions(category, null)
            }

            val data = response?.data
            val questions = mutableListOf<Question>()
            var questionNumber = 1
            data?.forEach {
                val type = it?.type
                var question: Question? = null
                when(type) {
                    "multipleChoice" -> question = QuestionMultipleChoice(
                        it.word!!,
                        it.syllables!!,
                        it.hintImg!!,
                        questionNumber,
                        it.multipleChoice!!.choices!! as List<String>
                    )
                    "scrambleWords" -> question = QuestionScrambleWords(
                        it.word!!,
                        it.syllables!!,
                        it.hintImg!!,
                        questionNumber,
                        it.scrambleWords!!.letters!! as List<String>,
                        it.scrambleWords.hintHangman!! as List<String>
                    )
                    "handwriting" -> question = QuestionHandwriting(
                        it.word!!,
                        it.syllables!!,
                        it.hintImg!!,
                        questionNumber,
                        it.handWriting!!.hintHangman!! as List<String>
                    )
                }
                questionNumber++
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
            if (newAttempt == null || exercise.value?.category == "assessment") {
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

    private suspend fun saveExercise() {
        val attemptsWithWrongAnswers = exercise.value?.attempts?.filter { attempt ->
            val wrongAnswers = attempt.answers.filter { answer -> !answer.isCorrect }
            wrongAnswers.isNotEmpty()
        }

        val attemptsJson = attemptsWithWrongAnswers?.map { attempt ->
            val wrongAnswers = attempt.answers.filter { answer -> !answer.isCorrect }
            val wrongAnswersJson = wrongAnswers.map { wrongAnswer ->
                val questionType = when (wrongAnswer.question) {
                    is QuestionMultipleChoice -> "multipleChoice"
                    is QuestionScrambleWords -> "scrambleWords"
                    is QuestionHandwriting -> "handwriting"
                    else -> null
                }
                val wrongAnswerJson = JSONObject()
                wrongAnswerJson.put("number", wrongAnswer.question.number)
                wrongAnswerJson.put("word", wrongAnswer.question.word)
                wrongAnswerJson.put("answer", wrongAnswer.answer)
                wrongAnswerJson.put("type", questionType)
                wrongAnswerJson
            }
            val attemptJson = JSONObject()
            attemptJson.put("attemptNumber", attempt.number)
            attemptJson.put("wrongAnswers", JSONArray(wrongAnswersJson.toString()))
            attemptJson
        }

        val jsonRequestBody = JSONObject()
        val questions = exercise.value?.questions
        val avgSyllables = questions!!
            .map { question -> question.syllables }
            .reduce { acc, i -> acc + i } / questions.size
        jsonRequestBody.put("userId", _userId.value)
        jsonRequestBody.put("endTime", System.currentTimeMillis())
        jsonRequestBody.put("avgSyllables", avgSyllables)
        jsonRequestBody.put("questionsQty", questions.size)
        jsonRequestBody.put("attempts", JSONArray(attemptsJson.toString()))
        val requestBody = jsonRequestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val client = RetrofitInstance.getApiService()
        client.createExercise(requestBody)
    }

    private suspend fun getAssessmentReport() {
        val jsonAnswers = exercise.value!!.attempts[0].answers.map { answer ->
            val jsonAnswer = JSONObject()
            val questionType = when (answer.question) {
                is QuestionMultipleChoice -> "multipleChoice"
                is QuestionScrambleWords -> "scrambleWords"
                is QuestionHandwriting -> "handwriting"
                else -> null
            }
            jsonAnswer.put("word", answer.question.word)
            jsonAnswer.put("syllables", answer.question.syllables)
            jsonAnswer.put("answer", answer.answer)
            jsonAnswer.put("isCorrect", answer.isCorrect)
            jsonAnswer.put("type", questionType)
            jsonAnswer
        }
        val jsonRequestBody = JSONObject()
        jsonRequestBody.put("answers", JSONArray(jsonAnswers.toString()))
        val requestBody = jsonRequestBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val client = RetrofitInstance.getApiService()
        val createAssessmentResponse = client.createAssessmentReport(requestBody)
        Log.d("createReport", createAssessmentResponse.toString())
        val assessmentId = createAssessmentResponse.data?.id
        val getAssessmentResponse = client.getAssessmentReport(assessmentId!!)
        Log.d("getReport", getAssessmentResponse.toString())
        _assessmentReport.value = AssessmentReport(
            getAssessmentResponse.data?.correctPercentage?.multipleChoice!!,
            getAssessmentResponse.data.correctPercentage.scrambleWords!!,
            getAssessmentResponse.data.correctPercentage.handwriting!!,
            getAssessmentResponse.data.score!!,
        )
    }

    private fun finish() {
        viewModelScope.launch {
            try {
                when (exercise.value!!.category) {
                    "auto" -> saveExercise()
                    "assessment" -> getAssessmentReport()
                    "custom" -> saveExercise()
                }
            } catch (exception: Exception) {
                Log.d("CREATE_EXERCISE_ERROR", exception.message.toString())
            }
            Log.d("finished", exercise.value.toString())
            _isFinished.value = true
        }
    }

    fun getExerciseCategory(): String {
        return exercise.value!!.category
    }
}
