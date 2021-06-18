package com.iswan.main.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iswan.main.core.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class FavouriteViewModel @Inject constructor(
    mainUseCase: MainUseCase
) : ViewModel() {

    val videos = mainUseCase.getFavouriteVideos().asLiveData()

}