package com.iswan.main.thatchapterfan.di

import com.iswan.main.thatchapterfan.utils.Preferences
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SubscriptionDependencies {

    fun preferences(): Preferences

}