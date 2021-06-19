package com.iswan.main.core.data.source.local.database

import androidx.paging.PagingSource
import androidx.room.*
import com.iswan.main.core.data.source.local.entity.RemoteKeys
import com.iswan.main.core.data.source.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isFavourite=1")
    fun getFavouriteVideos(): Flow<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(videos: List<VideoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideoUnsuspend(videos: List<VideoEntity>)

    @Update
    fun setFavourite(videoEntity: VideoEntity)

    @Query("DELETE FROM videos")
    suspend fun clearVideos()


    /* paging */
    @Query("SELECT * FROM videos")
    fun pagedVideos(): PagingSource<Int, VideoEntity>

    /* remote keys query */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE videoId = :videoId")
    suspend fun getRemoteKeys(videoId: String): RemoteKeys

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

}