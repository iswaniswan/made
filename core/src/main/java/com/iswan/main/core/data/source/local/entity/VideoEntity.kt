package com.iswan.main.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "videoId") var videoId: String,

    @ColumnInfo(name = "title") var title: String,

    @ColumnInfo(name = "description") var description: String,

    @ColumnInfo(name = "thumbnails") var thumbnails: String,

    @ColumnInfo(name = "image") var image: String,

    @ColumnInfo(name = "publishedAt") var publishedAt: String,

    @ColumnInfo(name = "duration") var duration: String,

    @ColumnInfo(name = "viewCount") var viewCount: Int,

    @ColumnInfo(name = "likeCount") var likeCount: Int,

    @ColumnInfo(name = "dislikeCount") var dislikeCount: Int,

    @ColumnInfo(name = "isFavourite") var isFavourite: Boolean = false
): Parcelable
