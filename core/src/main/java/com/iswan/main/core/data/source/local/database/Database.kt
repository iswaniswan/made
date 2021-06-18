package com.iswan.main.core.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iswan.main.core.data.source.local.entity.VideoEntity

@Database(
    entities = [VideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun videoDao(): VideoDao

}