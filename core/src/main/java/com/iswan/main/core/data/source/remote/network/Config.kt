package com.iswan.main.core.data.source.remote.network

import com.iswan.main.core.BuildConfig


object Config {

    const val API_URL = "https://www.googleapis.com/youtube/v3/"
    const val API_KEY = BuildConfig.API_KEY
    const val playlistId = BuildConfig.playlistId

    const val STARTING_PAGE_INDEX = 1
    const val DEFAULT_PAGE_TOKEN = ""

}