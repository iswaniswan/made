package com.iswan.main.core.data.source.local.database

import androidx.room.*
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

    @Update
    fun setFavourite(videoEntity: VideoEntity)

}