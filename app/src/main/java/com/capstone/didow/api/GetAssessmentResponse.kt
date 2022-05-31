package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class GetAssessmentResponse(

	@field:SerializedName("data")
	val data: GetAssessmentData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GetAssessmentData(

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("correctPercentage")
	val correctPercentage: CorrectPercentage? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class CorrectPercentage(

	@field:SerializedName("scrambleWords")
	val scrambleWords: Float? = null,

	@field:SerializedName("handwriting")
	val handwriting: Float? = null,

	@field:SerializedName("multipleChoice")
	val multipleChoice: Float? = null
)
