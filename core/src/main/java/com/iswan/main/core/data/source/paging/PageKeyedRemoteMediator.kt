package com.iswan.main.core.data.source.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.iswan.main.core.data.source.local.database.VideoDao
import com.iswan.main.core.data.source.local.entity.RemoteKeys
import com.iswan.main.core.data.source.local.entity.VideoEntity
import com.iswan.main.core.data.source.remote.network.ApiService
import com.iswan.main.core.data.source.remote.response.ItemsVideo
import com.iswan.main.core.data.source.utils.Mapper
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.util.*


@ExperimentalPagingApi
class PageKeyedRemoteMediator(
    private val dao: VideoDao,
    private val service: ApiService,
    private val scope: CoroutineScope
) : RemoteMediator<Int, VideoEntity>() {

    private val PAGE_SIZE = 20
    private val TAG = "PageKeyedRemoteMediator"

    override suspend fun load(loadType: LoadType, state: PagingState<Int, VideoEntity>)
            : MediatorResult {
        
            val pageToken: String? = when (loadType) {
                LoadType.REFRESH -> ""
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    withContext(Dispatchers.IO) {
                        val remoteKey = async {
                            getRemoteKeyForLastItem(state)
                        }.await()
                        if (remoteKey == null) {
                            Log.d(TAG, "load: APPEND REMOTEKEY IS NULL")
                            MediatorResult.Success(endOfPaginationReached = true)
                        }
                        return@withContext remoteKey?.nextPageToken
                    }

                }
            }
        
        return try {
            val apiResponse = service.getPlaylistItems(pageToken.toString())
            if (apiResponse.items.isNotEmpty()) {

                val endOfPaginationReached = apiResponse.items.size < PAGE_SIZE
                Log.d(TAG, "load: end page ? --> $endOfPaginationReached")
                val prevPageToken = apiResponse.prevPageToken ?: ""
                val nextPageToken = apiResponse.nextPageToken ?: ""

                val snippets = ArrayList<ItemsVideo>()
                val arrayId = ArrayList<String>()
                val listItems = apiResponse.items
                val listKeys = ArrayList<RemoteKeys>()

                if (listItems.isNotEmpty()) {
                    listItems.map {
                        arrayId.add(it.snippet.resourceId.videoId)
                    }
                    val concatId = arrayId.joinToString(",")

                    val videos = service.getVideos(concatId)

                    if (videos.items.isNotEmpty()) {
                        videos.items.map {
                            snippets.add(it)

                            val rKeys = RemoteKeys(
                                it.id, prevPageToken, nextPageToken
                            )
                            listKeys.add(rKeys)
                        }

                        dao.insertAllKeys(listKeys)
                        dao.insertVideo(Mapper.mapResponseToEntities(snippets))
                    }
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                Log.d(TAG, "load: apiResponse unexecuted")
                MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (e: IOException) {
            Log.d(TAG, "exception: ${e.message}")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.d(TAG, "exception: ${e.message}")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, VideoEntity>): RemoteKeys? =
        state.pages.lastOrNull() { it.data.isNotEmpty() } ?.data?.lastOrNull()
            ?.let {
                dao.getRemoteKeys(it.videoId)
            }

}