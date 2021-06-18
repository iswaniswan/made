package com.iswan.main.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iswan.main.core.domain.usecase.MainUseCase
import javax.inject.Inject

class FavouriteViewModelFactory @Inject constructor(
    private val mainUseCase: MainUseCase
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(FavouriteViewModel::class.java) -> {
                FavouriteViewModel(mainUseCase) as T
            }
            else -> throw Throwable("unknown view model class : ${modelClass.name}")
        }

}