package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class GetExercisesResponse(

	@field:SerializedName("data")
	val data: List<GetExercisesDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class WrongAnswersItem(

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("word")
	val word: String? = null,

	@field:SerializedName("attempts")
	val attempts: List<AttemptsItem?>? = null
)

data class GetExercisesDataItem(

	@field:SerializedName("questionsQty")
	val questionsQty: Int? = null,

	@field:SerializedName("wrongAnswers")
	val wrongAnswers: List<WrongAnswersItem?>? = null,

	@field:SerializedName("avgSyllables")
	val avgSyllables: Int? = null,

	@field:SerializedName("endTime")
	val endTime: Long? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class AttemptsItem(

	@field:SerializedName("attemptNumber")
	val attemptNumber: Int? = null,

	@field:SerializedName("answer")
	val answer: String? = null
)
