package com.iswan.main.thatchapterfan.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iswan.main.core.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    mainUseCase: MainUseCase
): ViewModel() {

    val videos = mainUseCase.getVideos().asLiveData()

}