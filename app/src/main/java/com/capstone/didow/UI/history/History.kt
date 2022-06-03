package com.capstone.didow.UI.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class History(
    val userId: String,
    val endTime: Long,
    val qty: Int,
    val avgSyllables: Int,
    val wrongAnswer: List<WrongAnswers>
):Parcelable

@Parcelize
open class WrongAnswers(
    val number: Int,
    val word: String,
    val type: String,
    val attempts: List<WrongAttempts>
):Parcelable

@Parcelize
open class WrongAttempts(
    val attemptNumber: Int,
    val answer: String
):Parcelable