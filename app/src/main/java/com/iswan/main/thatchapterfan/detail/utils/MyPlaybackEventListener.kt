package com.iswan.main.thatchapterfan.detail.utils

import com.google.android.youtube.player.YouTubePlayer

open class MyPlaybackEventListener: YouTubePlayer.PlaybackEventListener {

        override fun onPlaying() = Unit

        override fun onPaused() = Unit

        override fun onStopped() = Unit

        override fun onBuffering(p0: Boolean) = Unit

        override fun onSeekTo(p0: Int) = Unit

}

