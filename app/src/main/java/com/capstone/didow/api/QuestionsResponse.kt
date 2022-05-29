package com.capstone.didow.models

import com.google.gson.annotations.SerializedName

data class QuestionsResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("handWriting")
	val handWriting: HandWriting? = null,

	@field:SerializedName("hintImg")
	val hintImg: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("word")
	val word: String? = null,

	@field:SerializedName("syllables")
	val syllables: Int? = null,

	@field:SerializedName("scrambleWords")
	val scrambleWords: ScrambleWords? = null,

	@field:SerializedName("multipleChoice")
	val multipleChoice: MultipleChoice? = null
)

data class HandWriting(

	@field:SerializedName("hintHangman")
	val hintHangman: List<String?>? = null
)

data class MultipleChoice(

	@field:SerializedName("choices")
	val choices: List<String?>? = null
)

data class ScrambleWords(

	@field:SerializedName("hintHangman")
	val hintHangman: List<String?>? = null,

	@field:SerializedName("letters")
	val letters: List<String?>? = null
)
