package com.capstone.didow.entities

class QuestionHandwriting(
    word: String,
    syllables: Int,
    hintImg: String,
    val hintHangman: List<String>,
): Question(word, syllables, hintImg)