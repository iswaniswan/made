package com.iswan.main.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PageInfo(

    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("resultsPerPage")
    val resultsPerPage: Int
)

data class ResourceId(

    @field:SerializedName("kind")
    val kind: String,

    @field:SerializedName("videoId")
    val videoId: String
)

data class Thumbnails(

    @field:SerializedName("medium")
    val medium: Medium,

    @field:SerializedName("standard")
    val standard: Standard,
)

data class Medium(

    @field:SerializedName("width")
    val width: Int,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("height")
    val height: Int
)

data class Standard(

    @field:SerializedName("width")
    val width: Int,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("height")
    val height: Int
)

data class Localized(

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("title")
    val title: String
)

data class Statistics(

    @field:SerializedName("dislikeCount")
    val dislikeCount: String,

    @field:SerializedName("likeCount")
    val likeCount: String,

    @field:SerializedName("viewCount")
    val viewCount: String
)

data class ContentDetails(

    @field:SerializedName("duration")
    val duration: String
)