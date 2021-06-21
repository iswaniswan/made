package com.iswan.main.thatchapterfan.detail

import androidx.lifecycle.ViewModel
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.domain.usecase.MainUseCase
import com.iswan.main.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    fun updateFavourite(video: Video) =
        mainUseCase.updateFavourite(video)

    fun isSubscribed(): Boolean = userUseCase.getUser().get("isSubscribed").asBoolean

}