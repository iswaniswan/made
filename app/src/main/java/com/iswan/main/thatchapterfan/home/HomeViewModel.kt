package com.iswan.main.thatchapterfan.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.iswan.main.core.data.Resource
import com.iswan.main.core.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    mainUseCase: MainUseCase
): ViewModel() {

    val videos = mainUseCase.getVideos().asLiveData()

    val pagedVideos = mainUseCase.getPagedVideos()

    val flowPagedVideos = mainUseCase.flowPagedVideos()

}