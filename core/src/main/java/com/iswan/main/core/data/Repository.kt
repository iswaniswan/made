package com.iswan.main.core.data

import com.iswan.main.core.data.source.local.LocalDataSource
import com.iswan.main.core.data.source.remote.RemoteDataSource
import com.iswan.main.core.data.source.remote.network.ApiResponse
import com.iswan.main.core.data.source.remote.response.ItemsVideo
import com.iswan.main.core.data.source.utils.AppExecutors
import com.iswan.main.core.data.source.utils.Mapper
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: com.iswan.main.core.data.source.remote.RemoteDataSource,
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