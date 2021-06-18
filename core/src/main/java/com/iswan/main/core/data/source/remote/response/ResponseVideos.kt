package com.iswan.main.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseVideos(

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsVideo>
)

data class SnippetVideo(

	@field:SerializedName("publishedAt")
	val publishedAt: String,

	@field:SerializedName("localized")
	val localized: Localized,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("thumbnails")
	val thumbnails: Thumbnails,
)

data class ItemsVideo(

	@field:SerializedName("snippet")
	val snippet: SnippetVideo,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("etag")
	val etag: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("contentDetails")
	val contentDetails: ContentDetails,

	@field:SerializedName("statistics")
	val statistics: Statistics
)