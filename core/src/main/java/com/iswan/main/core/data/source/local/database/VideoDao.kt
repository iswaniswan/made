package com.iswan.main.core.data.source.local.database

import androidx.paging.PagingSource
import androidx.room.*
import com.iswan.main.core.data.source.local.entity.RemoteKeys
import com.iswan.main.core.data.source.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos WHERE isFavourite=0")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isFavourite=1")
    fun getFavouriteVideos(): PagingSource<Int, VideoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVideo(videos: List<VideoEntity>)

    @Update
    suspend fun updateFavourite(videoEntity: VideoEntity)

    @Query("DELETE FROM videos WHERE isFavourite=0")
    suspend fun clearVideos()

    @Query("SELECT * FROM videos")
    fun pagedVideos(): PagingSource<Int, VideoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE videoId = :videoId")
    suspend fun getRemoteKeys(videoId: String): RemoteKeys

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

}