package com.iswan.main.favourite

import androidx.lifecycle.ViewModel
import com.iswan.main.core.domain.usecase.MainUseCase
import javax.inject.Inject


class FavouriteViewModel @Inject constructor(
    mainUseCase: MainUseCase
) : ViewModel() {

    val videos = mainUseCase.getFavouriteVideos()

}