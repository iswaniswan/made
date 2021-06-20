package com.iswan.main.core.domain.repository

import androidx.paging.PagingData
import com.iswan.main.core.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface IRepository {

    fun getFavouriteVideos(): Flow<PagingData<Video>>

    fun updateFavourite(video: Video)

    fun getPagedVideos(): Flow<PagingData<Video>>

}