package com.iswan.main.thatchapterfan.di

import com.iswan.main.core.domain.usecase.UserUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SubscriptionDependencies {

    fun userUseCase(): UserUseCase

}