package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("data")
	val data: DataGetUserResponse? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataGetUserResponse(

	@field:SerializedName("weightPoint")
	val weightPoint: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("nickname")
	val nickname: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
