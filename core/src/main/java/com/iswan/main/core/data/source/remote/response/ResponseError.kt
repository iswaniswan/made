package com.iswan.main.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class Error(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: List<ErrorsItem?>? = null
)

data class ErrorsItem(

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("domain")
	val domain: String? = null,

	@field:SerializedName("locationType")
	val locationType: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
