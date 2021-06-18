package com.iswan.main.core.domain.usecase

import com.iswan.main.core.data.Resource
import com.iswan.main.core.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    fun getVideos(): Flow<Resource<List<Video>>>

    fun getFavouriteVideos(): Flow<List<Video>>

    fun setFavourite(video: Video, state: Boolean)

}