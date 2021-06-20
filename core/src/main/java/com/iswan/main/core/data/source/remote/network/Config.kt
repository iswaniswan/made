package com.iswan.main.core.data.source.remote.network

import com.iswan.main.core.BuildConfig


object Config {

    const val API_URL = "https://www.googleapis.com/youtube/v3/"
    const val API_KEY = BuildConfig.API_KEY
    const val playlistId = BuildConfig.playlistId
}