package com.capstone.didow.entities

class QuestionMultipleChoice(
    word: String,
    syllables: Int,
    hintImg: String,
    val choices: List<String>,
): Question(word, syllables, hintImg)