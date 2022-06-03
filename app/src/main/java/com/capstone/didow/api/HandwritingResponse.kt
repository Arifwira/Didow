package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class HandwritingResponse(

	@field:SerializedName("data")
	val data: HandwritingData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class HandwritingData(

	@field:SerializedName("expectedWord")
	val expectedWord: String? = null,

	@field:SerializedName("predictedWord")
	val predictedWord: String? = null,

	@field:SerializedName("dyslexia")
	val dyslexia: Boolean? = null,

	@field:SerializedName("isCorrect")
	val isCorrect: Boolean? = null
)
