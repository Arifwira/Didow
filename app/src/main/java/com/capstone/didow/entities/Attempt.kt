package com.capstone.didow.entities

import android.util.Log

class Attempt(val number: Int, val questions: List<Question>) {
    val answers = mutableListOf<Answer>()
    var currentQuestion: Question = questions[0]
    var currentQuestionIndex: Int = 0

    fun answer(question: Question, answer: String, isReversed: Boolean?): Boolean {
        val isCorrect = question.word == answer
        if (question is QuestionHandwriting) {
            Log.d("handwriting", isReversed.toString())
            answers.add(HandwritingAnswer(question, answer, isCorrect, isReversed!!))
        } else {
            answers.add(Answer(question, answer, isCorrect))
        }
        return isCorrect
    }

    fun next(): Question? {
        if (currentQuestionIndex == questions.size - 1) {
            return null
        }
        currentQuestionIndex++
        currentQuestion = questions[currentQuestionIndex]
        return currentQuestion
    }

    fun checkWrongAnswers(): Attempt? {
        // If it is the third attempt, don't retry (return null)
        if (number == 3) {
            return null
        }
        val wrongAnswers = answers.filter {
            !it.isCorrect
        }
        if (wrongAnswers.isEmpty()) {
            return null
        }
        val newQuestions = wrongAnswers.map {
            it.question
        }
        return Attempt(number + 1, newQuestions)
    }
}