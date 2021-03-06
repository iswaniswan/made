package com.iswan.main.subscription

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.iswan.main.core.domain.usecase.UserUseCase
import javax.inject.Inject

class SubscriptionViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    fun getUser(): JsonObject = userUseCase.getUser()

    fun subscribeUser(obj: JsonObject) = userUseCase.subscribeUser(obj)
}