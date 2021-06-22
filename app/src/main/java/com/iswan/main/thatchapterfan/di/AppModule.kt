package com.iswan.main.thatchapterfan.di

import androidx.paging.ExperimentalPagingApi
import com.iswan.main.core.di.RepositoryModule
import com.iswan.main.core.domain.usecase.MainInteractor
import com.iswan.main.core.domain.usecase.MainUseCase
import com.iswan.main.core.domain.usecase.UserInteractor
import com.iswan.main.core.domain.usecase.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @ExperimentalPagingApi
    @Binds
    abstract fun provideMainUseCase(mainInteractor: MainInteractor): MainUseCase

    @Binds
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase

}