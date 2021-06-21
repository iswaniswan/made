package com.iswan.main.core.domain.usecase

import com.google.gson.JsonObject
import dagger.hilt.EntryPoint

interface UserUseCase {

    fun subscribeUser(obj: JsonObject)

    fun getUser(): JsonObject
}