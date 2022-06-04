package com.capstone.didow.entities

class Exercise(val questions: List<Question>, val category: String, val allowRetry: Boolean) {
    lateinit var currentAttempt: Attempt
    val attempts = mutableListOf<Attempt>()
    var currentAttemptIndex: Int = 0


    fun start(): Question {
        attempts.add(Attempt(currentAttemptIndex, questions))
        currentAttempt = attempts[currentAttemptIndex]
        return currentAttempt.questions[0]
    }

    fun getCurentAttempt(): Attempt {
        return attempts[currentAttemptIndex]
    }

    fun addAttempt(attempt: Attempt) {
        attempts.add(attempt)
        currentAttempt = attempt
        currentAttemptIndex++
    }

    fun retry(): Question {
        return currentAttempt.questions[0]
    }


}