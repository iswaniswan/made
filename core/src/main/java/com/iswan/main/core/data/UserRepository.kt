package com.iswan.main.core.data

import com.google.gson.JsonObject
import com.iswan.main.core.data.security.SessionManager
import com.iswan.main.core.domain.repository.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val sessionManager: SessionManager
) : IUserRepository {

    override fun subscribeUser(obj: JsonObject) = sessionManager.saveToPreferences(obj)

    override fun getUserPreferences() = sessionManager.getFromPreferences()

}