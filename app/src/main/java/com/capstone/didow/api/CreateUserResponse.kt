package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class CreateUserResponse(

	@field:SerializedName("data")
	val data: DataCreateUserResponse? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataCreateUserResponse(

	@field:SerializedName("createdAt")
	val createdAt: Long? = null,

	@field:SerializedName("id")
	val id: String? = null
)
