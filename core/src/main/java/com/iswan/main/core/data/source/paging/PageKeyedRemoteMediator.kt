package com.iswan.main.core.data.source.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.iswan.main.core.data.source.local.database.VideoDao
import com.iswan.main.core.data.source.local.entity.RemoteKeys
import com.iswan.main.core.data.source.local.entity.VideoEntity
import com.iswan.main.core.data.source.remote.network.ApiResponse
import com.iswan.main.core.data.source.remote.network.ApiService
import com.iswan.main.core.data.source.remote.network.Config
import com.iswan.main.core.data.source.remote.response.ItemsVideo
import com.iswan.main.core.data.source.utils.Mapper
import java.util.ArrayList

@ExperimentalPagingApi
class PageKeyedRemoteMediator(
    private val dao: VideoDao,
    private val service: ApiService
) : RemoteMediator<Int, VideoEntity>() {

    private val TAG = "PageKeyedRemoteMediator"
    private var pageToken: String = ""
    private var prevPageToken: String = ""
    private var nextPageToken: String = ""

    override suspend fun load(loadType: LoadType, state: PagingState<Int, VideoEntity>)
            : MediatorResult {

        return try {
            val page = when (loadType) {
                LoadType.APPEND -> {
                    val remoteKeys = getLastRemoteKey(state)
                    pageToken = remoteKeys?.nextPageToken ?: ""
                    remoteKeys?.nextKey ?: return MediatorResult.Success(true)
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getLastRemoteKey(state)
                    pageToken = remoteKeys?.prevPageToken ?: ""
                    return MediatorResult.Success(true)
                }
                LoadType.REFRESH -> {
                    val remoteKeys = getClosestRemoteKeys(state)
                    remoteKeys?.nextKey?.minus(1) ?: Config.STARTING_PAGE_INDEX
                }
            }

            Log.d(TAG, "load: nextPageToken --> $nextPageToken")
            Log.d(TAG, "load: prevPageToken --> $prevPageToken")
            Log.d(TAG, "load: pageToken --> $pageToken")

            /* make network request */
            Log.d(TAG, "load: request playlist -->")
            val apiResponse = service.getPlaylistItems(pageToken)
            if (apiResponse.items.isNotEmpty()) {
                Log.d(TAG, "load: apiResponse not empty")

                nextPageToken = apiResponse.nextPageToken ?: ""
                prevPageToken = apiResponse.prevPageToken ?: ""

                val endOfPaginationReached = apiResponse.items.isEmpty()
                if (loadType == LoadType.REFRESH) {
                    dao.clearRemoteKeys()
                    dao.clearVideos()
                }
                val prevKey = if (page <= 1) 1 else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val listKeys = apiResponse.items.map {
                    Log.d(TAG, "load: ${it.id}, $prevKey, $nextKey, $prevPageToken, $nextPageToken")
                    RemoteKeys(it.id, prevKey, nextKey, prevPageToken, nextPageToken)
                }
                dao.insertAll(listKeys)

                /* video section */

                val snippets = ArrayList<ItemsVideo>()
                val arrayId = ArrayList<String>()
                val listItems = apiResponse.items

                Log.d(TAG, "load: request playlist -->")
                if (listItems.isNotEmpty()) {
                    listItems.map {
                        arrayId.add(it.snippet.resourceId.videoId)
                    }
                    val concatId = arrayId.joinToString(",")
                    println("concatId ---> $concatId")
                    val videos = service.getVideos(concatId)
                    if (videos.items.isNotEmpty()) {
                        videos.items.map {
                            snippets.add(it)
                        }
                        dao.insertVideo(Mapper.mapResponseToEntities(snippets))
                    }
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                Log.d(TAG, "load: apiResponse unexecuted")
                MediatorResult.Success(endOfPaginationReached = true)
            }

        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getClosestRemoteKeys(state: PagingState<Int, VideoEntity>): RemoteKeys? =
        state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                dao.getRemoteKeys(it.videoId)
            }
        }

    private suspend fun getLastRemoteKey(state: PagingState<Int, VideoEntity>): RemoteKeys? =
        state.lastItemOrNull()?.let {
            dao.getRemoteKeys(it.videoId)
        }
}

//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, MovieEntity>
//    ): MediatorResult {
//        try {
//            Log.d(TAG, "load: excuted")
//            when (loadType) {
//                REFRESH -> REQUEST_PAGE
//                PREPEND ->  {
//                    REQUEST_PAGE = if (REQUEST_PAGE > 1) REQUEST_PAGE-- else Config.STARTING_PAGE_INDEX
//                    return MediatorResult.Success(true)
//                }
//                APPEND -> {
//                    REQUEST_PAGE = if (REQUEST_PAGE > 1) REQUEST_PAGE++ else Config.STARTING_PAGE_INDEX
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//            }
//
//            val response = apiService.getTrendingMovie(REQUEST_PAGE)
//            val moviesEntity = response.results.map {
//                Mapper.responseToEntity(it)
//            }
//
//            withContext(dispatcher) {
//                dao.insertMovies(moviesEntity)
//            }
//
//            Log.d(TAG, "REQUEST PAGE now ---> $REQUEST_PAGE")
//            Log.d(TAG, "CHECK -----> response page : ${response.page} ---> total pages : ${response.totalPages}")
//
//            return MediatorResult.Success(endOfPaginationReached = response.page == response.totalPages)
//        } catch (e: IOException) {
//            Log.d(TAG, "IOException: ${e.message}")
//            return MediatorResult.Error(e)
//        } catch (e: HttpException) {
//            Log.d(TAG, "HttpException: ${e.message}")
//            return MediatorResult.Error(e)
//        }
//    }
//}
