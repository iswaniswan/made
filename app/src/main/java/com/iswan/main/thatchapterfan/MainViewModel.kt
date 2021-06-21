package com.iswan.main.thatchapterfan

import androidx.lifecycle.ViewModel
import com.iswan.main.core.domain.usecase.MainUseCase
import com.iswan.main.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    fun getUsername(): String = userUseCase.getUser().get("name").asString

    /*
    fun subscribeUser(name: String)

    fun unsubscribeUser()

    fun isUserSubscribed(): Boolean

    fun getUser(): String?
    * */

}