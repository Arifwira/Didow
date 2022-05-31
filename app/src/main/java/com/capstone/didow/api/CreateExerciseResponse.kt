package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class CreateExerciseResponse(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("message")
	val message: String? = null
)
