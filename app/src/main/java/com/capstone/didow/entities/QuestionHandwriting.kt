package com.capstone.didow.entities

class QuestionHandwriting(
    word: String,
    syllables: Int,
    hintImg: String,
    number: Int,
    val hintHangman: List<String>,
): Question(word, syllables, hintImg, number)