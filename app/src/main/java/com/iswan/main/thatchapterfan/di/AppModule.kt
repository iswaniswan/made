package com.iswan.main.thatchapterfan.di

import com.iswan.main.core.domain.usecase.MainInteractor
import com.iswan.main.core.domain.usecase.MainUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [PreferencesModule::class])
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideMainUseCase(mainInteractor: MainInteractor): MainUseCase

}