package com.iswan.main.core.domain.repository

import com.google.gson.JsonObject

interface IUserRepository {

    fun subscribeUser(obj: JsonObject)

    fun getUserPreferences(): JsonObject
}