package com.iswan.main.core.data

import androidx.paging.*
import com.iswan.main.core.data.source.local.database.VideoDao
import com.iswan.main.core.data.source.paging.PageKeyedRemoteMediator
import com.iswan.main.core.data.source.remote.network.ApiService
import com.iswan.main.core.data.source.utils.Mapper
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.domain.repository.IRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalPagingApi
class Repository @Inject constructor(
    private val dao: VideoDao,
    private val service: ApiService,
    private val dispatcher: CoroutineDispatcher
): IRepository {

    private val scope = CoroutineScope(dispatcher)
    private val pagingConfig = PagingConfig(
        pageSize = 5,
        prefetchDistance = 10,
        enablePlaceholders = false
    )

    override fun getPagedVideos(): Flow<PagingData<Video>> =
        Pager(
            config = pagingConfig,
            remoteMediator = PageKeyedRemoteMediator(dao, service, dispatcher),
            pagingSourceFactory = {
                dao.pagedVideos()
            }
        ).flow
            .map { paging ->
                paging.map {
                    Mapper.mapEntityToModel(it)
                }
            }

    override fun getFavouriteVideos(): Flow<PagingData<Video>> =
        Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.getFavouriteVideos() }
        ).flow
            .map { paging ->
                paging.map {
                    Mapper.mapEntityToModel(it)
                }
            }

    override fun updateFavourite(video: Video) {
        scope.launch {
            withContext(dispatcher) {
                val videoEntity = Mapper.mapModelToEntity(video)
                dao.updateFavourite(videoEntity)
            }
        }
    }
}