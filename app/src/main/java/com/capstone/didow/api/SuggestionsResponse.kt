package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class SuggestionsResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
