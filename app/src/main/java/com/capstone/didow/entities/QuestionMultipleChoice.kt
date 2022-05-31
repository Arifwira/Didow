package com.capstone.didow.entities

class QuestionMultipleChoice(
    word: String,
    syllables: Int,
    hintImg: String,
    number: Int,
    val choices: List<String>,
): Question(word, syllables, hintImg, number)