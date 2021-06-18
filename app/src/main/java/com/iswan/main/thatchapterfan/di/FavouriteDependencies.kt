package com.iswan.main.thatchapterfan.di

import com.iswan.main.core.domain.usecase.MainUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavouriteDependencies {

    fun mainUseCase(): MainUseCase

}