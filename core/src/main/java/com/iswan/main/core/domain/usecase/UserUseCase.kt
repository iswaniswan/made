package com.iswan.main.core.domain.usecase

import com.google.gson.JsonObject

interface UserUseCase {

    fun subscribeUser(obj: JsonObject)

    fun getUser(): JsonObject
}