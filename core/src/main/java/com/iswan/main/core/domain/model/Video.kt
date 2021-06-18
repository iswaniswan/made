package com.iswan.main.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val videoId: String,
    val title: String,
    val description: String,
    val thumbnails: String,
    val image: String,
    val publishedAt: String,
    val duration: String,
    val viewCount: Int,
    val likeCount: Int,
    val dislikeCount: Int,
    val isFavourite: Boolean
): Parcelable
