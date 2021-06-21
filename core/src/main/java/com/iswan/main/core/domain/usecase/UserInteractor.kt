package com.iswan.main.core.domain.usecase

import com.google.gson.JsonObject
import com.iswan.main.core.data.UserRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractor @Inject constructor(
    private val userRepository: UserRepository
): UserUseCase {

    override fun subscribeUser(obj: JsonObject) = userRepository.subscribeUser(obj)

    override fun getUser(): JsonObject = userRepository.getUserPreferences()
}