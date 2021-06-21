package com.iswan.main.core.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.iswan.main.core.data.Repository
import com.iswan.main.core.data.UserRepository
import com.iswan.main.core.domain.model.Video
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class MainInteractor @Inject constructor(
    private val repository: Repository
) : MainUseCase {

    override fun getFavouriteVideos(): Flow<PagingData<Video>> = repository.getFavouriteVideos()

    override fun updateFavourite(video: Video) = repository.updateFavourite(video)

    override fun getPagedVideos(): Flow<PagingData<Video>> =
        repository.getPagedVideos()
}