package com.iswan.main.core.data.source.utils

import com.iswan.main.core.data.source.local.entity.VideoEntity
import com.iswan.main.core.data.source.remote.response.ItemsVideo
import com.iswan.main.core.domain.model.Video


object Mapper {

    fun mapResponseToEntities(input: List<ItemsVideo>): List<VideoEntity> {
        val videos = ArrayList<VideoEntity>()
        input.map {
            val video = VideoEntity(
                it.id,
                it.snippet.title,
                it.snippet.description,
                it.snippet.thumbnails?.medium?.url ?: "",
                it.snippet.thumbnails?.standard?.url ?: "",
                it.snippet.publishedAt,
                it.contentDetails.duration,
                it.statistics.viewCount.toInt(),
                it.statistics.likeCount.toInt(),
                it.statistics.dislikeCount.toInt(),
                false
            )
            videos.add(video)
        }
        return videos
    }

    fun mapEntitiesToModel(videos: List<VideoEntity>): List<Video> =
        videos.map {
            Video(
                it.videoId,
                it.title,
                it.description,
                it.thumbnails,
                it.image,
                it.publishedAt,
                it.duration,
                it.viewCount,
                it.likeCount,
                it.dislikeCount,
                it.isFavourite
            )
        }

    fun mapEntityToModel(it: VideoEntity): Video =
        Video(
            it.videoId,
            it.title,
            it.description,
            it.thumbnails,
            it.image,
            it.publishedAt,
            it.duration,
            it.viewCount,
            it.likeCount,
            it.dislikeCount,
            it.isFavourite
        )

    fun mapModelToEntity(video: Video): VideoEntity =
        VideoEntity(
            video.videoId,
            video.title,
            video.description,
            video.thumbnails,
            video.image,
            video.publishedAt,
            video.duration,
            video.viewCount,
            video.likeCount,
            video.dislikeCount,
            video.isFavourite
        )


}