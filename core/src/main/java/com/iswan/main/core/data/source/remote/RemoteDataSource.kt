package com.iswan.main.core.data.source.remote

import com.iswan.main.core.data.source.remote.network.ApiResponse
import com.iswan.main.core.data.source.remote.network.ApiService
import com.iswan.main.core.data.source.remote.response.ItemsVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPlaylistItems(): Flow<ApiResponse<List<ItemsVideo>>> {
        return flow {
            val snippets = ArrayList<ItemsVideo>()
            val arrayId = ArrayList<String>()
            try {
                val playlist = apiService.getPlaylistItems()
                val listItems = playlist.items
                if (listItems.isNotEmpty()) {
                    listItems.map {
                        arrayId.add(it.snippet.resourceId.videoId)
                    }
                    val concatId = arrayId.joinToString(",").toString()
                    println("concatId ---> $concatId")
                    val videos = apiService.getVideos(concatId)
                    println("videos ---> $videos")
                    if (videos.items.isNotEmpty()) {
                        videos.items.map {
                            snippets.add(it)
                        }
                    }
                    emit(ApiResponse.Success(snippets))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}