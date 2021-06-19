package com.iswan.main.core.data

import android.util.Log
import androidx.paging.*
import com.iswan.main.core.data.source.local.LocalDataSource
import com.iswan.main.core.data.source.local.database.Database
import com.iswan.main.core.data.source.local.database.VideoDao
import com.iswan.main.core.data.source.local.entity.VideoEntity
import com.iswan.main.core.data.source.paging.PageKeyedRemoteMediator
import com.iswan.main.core.data.source.remote.RemoteDataSource
import com.iswan.main.core.data.source.remote.network.ApiResponse
import com.iswan.main.core.data.source.remote.network.ApiService
import com.iswan.main.core.data.source.remote.response.ItemsVideo
import com.iswan.main.core.data.source.utils.AppExecutors
import com.iswan.main.core.data.source.utils.Mapper
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.domain.repository.IRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalPagingApi
class Repository @Inject constructor(
    private val dao: VideoDao,
    private val service: ApiService,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val executors: AppExecutors
): IRepository {

    override fun getVideos(): Flow<Resource<List<Video>>> =
        object: NetworkBoundResource<List<Video>, List<ItemsVideo>>() {
            override fun shouldFetch(data: List<Video>?): Boolean =
                data == null || data.isEmpty()

            override fun loadFromDB(): Flow<List<Video>> {
                return localDataSource.getAllVideos().map { Mapper.mapEntitiesToModel(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ItemsVideo>>> =
                remoteDataSource.getPlaylistItems()

            override suspend fun saveCallResult(data: List<ItemsVideo>) {
                val videos = Mapper.mapResponseToEntities(data)
                localDataSource.insertVideos(videos)
            }

        }.asFlow()

    private val TAG = "REPOSITORY ---->"
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun flowPagedVideos(): Flow<PagingData<VideoEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = PageKeyedRemoteMediator(dao, service),
            pagingSourceFactory = {
                dao.pagedVideos()
            }
        ).flow

    override fun getPagedVideos(): Flow<PagingData<Video>> =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = PageKeyedRemoteMediator(dao, service),
            pagingSourceFactory = {
                dao.pagedVideos()
            }
        ).flow.map { paging ->
            paging.map {
                Mapper.mapEntityToModel(it)
            }
        }


    override fun setFavourite(video: Video, state: Boolean) {
        val videoEntity = Mapper.mapModelToEntity(video)
        executors.diskIO().execute {
            localDataSource.setFavourite(videoEntity, state)
        }
    }

    override fun getFavouriteVideos(): Flow<List<Video>> {
        return localDataSource.getFavouriteVideos().map {
            Mapper.mapEntitiesToModel(it)
        }
    }



}
