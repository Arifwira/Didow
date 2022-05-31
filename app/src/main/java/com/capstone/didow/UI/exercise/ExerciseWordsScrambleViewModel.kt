package com.capstone.didow.UI.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseWordsScrambleViewModel : ViewModel() {
    private val _selectedLettersTemp = mutableListOf<String>()
    private val _selectedLetters = MutableLiveData<List<String>>()
    private val _availableLettersTemp = hashMapOf<String, Int>()
    private val _availableLetters = MutableLiveData<HashMap<String, Int>>()

    val selectedLetters: LiveData<List<String>> = _selectedLetters
    val availableLetters: LiveData<HashMap<String, Int>> = _availableLetters

    fun init(letters: List<String>) {
        _selectedLettersTemp.clear()
        _selectedLetters.value = _selectedLettersTemp
        letters.forEach { letter ->
            val qty: Int? = _availableLettersTemp[letter]
            if (qty != null) {
                _availableLettersTemp[letter] = qty + 1
            } else {
                _availableLettersTemp[letter] = 1
            }
        }
        _availableLetters.value = _availableLettersTemp
    }

    fun selectLetter(letter: String) {
        _selectedLettersTemp.add(letter)
        _selectedLetters.value = _selectedLettersTemp
        val currentCount = _availableLettersTemp[letter]
        _availableLettersTemp[letter] = currentCount!! - 1
        _availableLetters.value = _availableLettersTemp
    }

    fun undoLetter() {
        val lastLetter = _selectedLettersTemp.removeLast()
        _selectedLetters.value = _selectedLettersTemp
        val qty = _availableLettersTemp[lastLetter]
        _availableLettersTemp[lastLetter] = qty!! + 1
        _availableLetters.value = _availableLettersTemp
    }
}