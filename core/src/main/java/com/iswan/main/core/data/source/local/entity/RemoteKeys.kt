package com.iswan.main.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val videoId: String,
    val prevPageToken: String?,
    val nextPageToken: String?
)
