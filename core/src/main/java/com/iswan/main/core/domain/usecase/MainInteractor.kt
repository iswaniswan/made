package com.iswan.main.core.domain.usecase

import com.iswan.main.core.data.Repository
import com.iswan.main.core.data.Resource
import com.iswan.main.core.domain.model.Video
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val repository: Repository
): MainUseCase {

    override fun getVideos(): Flow<Resource<List<Video>>> = repository.getVideos()

    override fun getFavouriteVideos(): Flow<List<Video>> = repository.getFavouriteVideos()

    override fun setFavourite(video: Video, state: Boolean) = repository.setFavourite(video, state)

}