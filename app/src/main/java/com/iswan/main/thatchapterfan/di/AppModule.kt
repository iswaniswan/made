package com.iswan.main.thatchapterfan.di

import androidx.paging.ExperimentalPagingApi
import com.iswan.main.core.domain.usecase.MainInteractor
import com.iswan.main.core.domain.usecase.MainUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@ExperimentalPagingApi
@Module(includes = [PreferencesModule::class])
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideMainUseCase(mainInteractor: MainInteractor): MainUseCase

}