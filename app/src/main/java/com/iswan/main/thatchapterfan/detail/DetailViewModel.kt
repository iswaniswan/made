package com.iswan.main.thatchapterfan.detail

import androidx.lifecycle.ViewModel
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
): ViewModel() {

    fun updateFavourite(video: Video) =
        mainUseCase.updateFavourite(video)

}