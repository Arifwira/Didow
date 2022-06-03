package com.capstone.didow.entities

class Attempt(val number: Int, val questions: List<Question>) {
    val answers = mutableListOf<Answer>()
    var currentQuestion: Question = questions[0]
    var currentQuestionIndex: Int = 0

    fun answer(question: Question, answer: String): Boolean {
        val isCorrect = question.word == answer
        answers.add(Answer(question, answer, isCorrect))
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