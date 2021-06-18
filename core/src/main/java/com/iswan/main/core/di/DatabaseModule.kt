package com.iswan.main.core.di

import android.content.Context
import androidx.room.Room
import com.iswan.main.core.data.source.local.database.Database
import com.iswan.main.core.data.source.local.database.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(
            context,
            Database::class.java, "videos_db"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(database: Database): VideoDao =
        database.videoDao()

}