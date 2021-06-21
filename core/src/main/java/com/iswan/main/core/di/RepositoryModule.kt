package com.iswan.main.core.di

import androidx.paging.ExperimentalPagingApi
import com.iswan.main.core.data.Repository
import com.iswan.main.core.data.UserRepository
import com.iswan.main.core.data.security.SessionManager
import com.iswan.main.core.domain.repository.IRepository
import com.iswan.main.core.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module(includes = [NetworkModule::class, DatabaseModule::class, PreferencesModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @ExperimentalPagingApi
    @Binds
    abstract fun provideRepository(repository: Repository): IRepository

    @Binds
    abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository

}