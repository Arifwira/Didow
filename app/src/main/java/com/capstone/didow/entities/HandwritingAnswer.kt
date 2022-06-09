package com.capstone.didow.entities

class HandwritingAnswer(
    question: Question,
    answer: String,
    isCorrect: Boolean,
    val isReversed: Boolean
): Answer(question, answer, isCorrect)