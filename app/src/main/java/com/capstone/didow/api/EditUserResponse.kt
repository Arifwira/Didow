package com.capstone.didow.api

import com.google.gson.annotations.SerializedName

data class EditUserResponse(

	@field:SerializedName("data")
	val data: EditData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class EditData(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: Long? = null
)
