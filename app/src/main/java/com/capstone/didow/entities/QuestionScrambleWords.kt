package com.capstone.didow.entities

class QuestionScrambleWords(
    word: String,
    syllables: Int,
    hintImg: String,
    val letters: List<String>,
    val hintHangman: List<String>,
): Question(word, syllables, hintImg)
