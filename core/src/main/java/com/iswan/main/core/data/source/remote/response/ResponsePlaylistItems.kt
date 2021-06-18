package com.iswan.main.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResponsePlaylistItems(

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("nextPageToken")
	val nextPageToken: String? = null,

	@field:SerializedName("pageInfo")
	val pageInfo: PageInfo? = null,

	@field:SerializedName("etag")
	val etag: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsPlaylist>
)

data class SnippetPlaylist(

	@field:SerializedName("resourceId")
	val resourceId: ResourceId,

	@field:SerializedName("publishedAt")
	val publishedAt: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("position")
	val position: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("thumbnails")
	val thumbnails: Thumbnails,
)

data class ItemsPlaylist(

	@field:SerializedName("snippet")
	val snippet: SnippetPlaylist,

	@field:SerializedName("id")
	val id: String
)
