package com.iswan.main.core.di

import android.content.Context
import androidx.room.Room
import com.iswan.main.core.BuildConfig
import com.iswan.main.core.data.source.local.database.Database
import com.iswan.main.core.data.source.local.database.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("thatchapterfan".toCharArray())
        val factory = SupportFactory(passphrase)
        val database = Room.databaseBuilder(
            context,
            Database::class.java, "videos_db"
        ).fallbackToDestructiveMigration()

        if (!BuildConfig.DEBUG) {
            database.openHelperFactory(factory)
        }
        return database.build()
    }

    @Singleton
    @Provides
    fun provideDao(database: Database): VideoDao =
        database.videoDao()

}