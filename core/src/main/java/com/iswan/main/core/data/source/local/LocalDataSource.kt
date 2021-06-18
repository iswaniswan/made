package com.iswan.main.core.data.source.local

import com.iswan.main.core.data.source.local.database.VideoDao
import com.iswan.main.core.data.source.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val videoDao: VideoDao
) {

    fun getAllVideos(): Flow<List<VideoEntity>> = videoDao.getAllVideos()

    fun getFavouriteVideos(): Flow<List<VideoEntity>> = videoDao.getFavouriteVideos()

    suspend fun insertVideos(videos: List<VideoEntity>) = videoDao.insertVideo(videos)

    fun setFavourite(videoEntity: VideoEntity, state: Boolean) {
        videoEntity.isFavourite = state
        videoDao.setFavourite(videoEntity)
    }

}