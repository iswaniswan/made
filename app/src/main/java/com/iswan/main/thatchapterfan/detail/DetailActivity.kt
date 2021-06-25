package com.iswan.main.thatchapterfan.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.iswan.main.core.domain.model.Video
import com.iswan.main.thatchapterfan.databinding.ActivityDetailBinding
import com.iswan.main.thatchapterfan.detail.utils.LeakyPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var video: Video

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModels()

    private var leakyPlayer: LeakyPlayer? = null

    companion object {
        const val EXTRA_VIDEO = "extra_video"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        video = intent.getParcelableExtra<Video>(EXTRA_VIDEO) as Video

        leakyPlayer = LeakyPlayer(video, this, viewModel, binding)
        leakyPlayer?.playVideo()
    }

    override fun onResume() {
        super.onResume()
        leakyPlayer?.updateView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (leakyPlayer != null) {
            leakyPlayer = null
        }
    }
}

