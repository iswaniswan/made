package com.iswan.main.core.domain.usecase

import androidx.paging.PagingData
import com.iswan.main.core.data.Resource
import com.iswan.main.core.data.source.local.entity.VideoEntity
import com.iswan.main.core.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    fun getVideos(): Flow<Resource<List<Video>>>

    fun getFavouriteVideos(): Flow<List<Video>>

    fun setFavourite(video: Video, state: Boolean)

    fun getPagedVideos(): Flow<PagingData<Video>>

    fun flowPagedVideos(): Flow<PagingData<VideoEntity>>

}