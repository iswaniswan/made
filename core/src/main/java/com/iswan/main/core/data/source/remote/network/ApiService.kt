package com.iswan.main.core.data.source.remote.network

import com.iswan.main.core.data.source.remote.response.ResponsePlaylistItems
import com.iswan.main.core.data.source.remote.response.ResponseVideos
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("playlistItems")
    suspend fun getPlaylistItems(
        @Query("pageToken") pageToken: String,
        @Query("playlistId") playlistId: String = Config.playlistId,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") key: String = Config.API_KEY
    ): ResponsePlaylistItems

    @GET("videos")
    suspend fun getVideos(
        @Query("id") id: String,
        @Query("part") part: String = "snippet, contentDetails, statistics",
        @Query("key") key: String = Config.API_KEY
    ): ResponseVideos

}