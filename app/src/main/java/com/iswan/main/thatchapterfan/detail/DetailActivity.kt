package com.iswan.main.thatchapterfan.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.utils.Utils
import com.iswan.main.thatchapterfan.BuildConfig
import com.iswan.main.thatchapterfan.R
import com.iswan.main.thatchapterfan.databinding.ActivityDetailBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var video: Video
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    private var player: YouTubePlayer? =null

    companion object {
        const val EXTRA_VIDEO = "extra_video"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        video = intent.getParcelableExtra<Video>(EXTRA_VIDEO) as Video

        val playerview = binding.youtubePlayerView

        lifecycle.addObserver(playerview)
        playerview.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                player = youTubePlayer
                player?.loadVideo(video.videoId, 0f)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerState) {
                super.onStateChange(youTubePlayer, state)
                if (state == PLAYING && !isSubscribed()) {
                    notifyPremium()
                    youTubePlayer.pause()
                }
            }
        })

        @Suppress("DEPRECATION")
        playerview.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                supportActionBar?.hide()
                binding.youtubeContentData.root.visibility = View.GONE
                playerview.layoutParams.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                window.decorView.apply {
                    systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                }
            }

            override fun onYouTubePlayerExitFullScreen() {
                window.decorView.apply {
                    systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
                playerview.layoutParams.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                supportActionBar?.show()
                binding.youtubeContentData.root.visibility = View.VISIBLE
            }

        })
    }

    override fun onResume() {
        super.onResume()
        updateView()
        player?.play()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.youtubePlayerView.enterFullScreen()
        } else {
            binding.youtubePlayerView.exitFullScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player = null
        binding.youtubePlayerView.release()
    }

    private fun notifyPremium() {
        Snackbar.make(binding.root, getString(R.string.premium_features),
            Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.activate_now)) {
                val uri = Uri.parse(BuildConfig.uri_subscription)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            .show()
    }

    fun updateView() {
        with(binding.youtubeContentData) {
            tvTitle.text = video.title

            val publishedAtValue = StringBuilder()
                .append(getString(R.string.publishedOn))
                .append(" ")
                .append(Utils.convertISO8601toDate(video.publishedAt))
            tvPublished.text = publishedAtValue

            val viewsText = StringBuilder()
                .append(NumberFormat.getInstance().format(video.viewCount))
                .append(getString(R.string.views))
            tvViews.text = viewsText

            tvLikes.text = Utils.simplifyOfNumber(video.likeCount, "")
            tvDislikes.text = Utils.simplifyOfNumber(video.dislikeCount, "")
            tvDescription.text = video.description
            tbFavourite.isChecked = video.isFavourite
            tbFavourite.apply {
                if (isSubscribed()) featuresFull else featuresLimit
            }
        }
    }

    private fun isSubscribed(): Boolean = viewModel.isSubscribed()

    private val ToggleButton.featuresFull: Unit
        get() {
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this@DetailActivity, R.color.primaryDarkColor)
            )
            setOnClickListener { }
            setOnCheckedChangeListener(favouriteListener())
        }

    private val ToggleButton.featuresLimit: Unit
        get() {
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this@DetailActivity, R.color.disabled)
            )
            setOnClickListener { notifyPremium() }
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked != video.isFavourite) buttonView.isChecked = video.isFavourite
            }
        }

    private fun favouriteListener(): CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    this,
                    getString(R.string.add_to_favourite), Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                   getString(R.string.remove_from_favourite), Toast.LENGTH_LONG
                ).show()
            }
            video.isFavourite = isChecked
            viewModel.updateFavourite(video)
        }
}

