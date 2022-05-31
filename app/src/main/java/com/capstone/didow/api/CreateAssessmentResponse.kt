package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class CreateAssessmentResponse(

	@field:SerializedName("data")
	val data: CreateAssessmentData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CreateAssessmentData(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("id")
	val id: String? = null
)
