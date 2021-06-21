package com.iswan.main.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iswan.main.core.domain.usecase.MainUseCase
import com.iswan.main.core.domain.usecase.UserUseCase
import javax.inject.Inject

class SubscriptionViewModelFactory @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(SubscriptionViewModel::class.java) -> {
                SubscriptionViewModel(userUseCase) as T
            }
            else -> throw Throwable("unknown view model class : ${modelClass.name}")
        }

}