package com.iswan.main.thatchapterfan.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.iswan.main.core.BuildConfig
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.ui.Utils
import com.iswan.main.thatchapterfan.R
import com.iswan.main.thatchapterfan.databinding.ActivityDetailBinding
import com.iswan.main.thatchapterfan.detail.utils.MyPlaybackEventListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat


@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    private val TAG = "DETAIL-ACTIVITY"

    private lateinit var video: Video

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModels()

    private var _youtubeFragment: YouTubePlayerSupportFragmentX? = null
    private val youtubeFragment get() = _youtubeFragment!!

    companion object {
        const val EXTRA_VIDEO = "extra_video"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        video = intent.getParcelableExtra<Video>(EXTRA_VIDEO) as Video

        val mFragmentManager = supportFragmentManager

        _youtubeFragment = YouTubePlayerSupportFragmentX.newInstance()

        mFragmentManager.commit {
            add(R.id.youtube_fragment, youtubeFragment)
        }
        youtubeFragment.initialize(BuildConfig.API_KEY, this)
        updateView()
    }

    private fun updateView() {
        with(binding) {
            tvTitle.text = video.title
            val publishedText =
                getString(R.string.publishedOn) + " " + Utils.converISO8601toDate(video.publishedAt)
            tvPublished.text = publishedText
            val viewsText = NumberFormat.getInstance().format(video.viewCount) + " Views"
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
            backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.primaryDarkColor))
            setOnClickListener { }
            setOnCheckedChangeListener(favouriteListener())
        }

    private val ToggleButton.featuresLimit: Unit
        get() {
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.disabled))
            setOnClickListener { notifyPremium() }
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked != video.isFavourite) buttonView.isChecked = video.isFavourite
            }
        }

    private fun favouriteListener(): CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    this@DetailActivity,
                    getString(R.string.add_to_favourite), Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@DetailActivity,
                    getString(R.string.remove_from_favourite), Toast.LENGTH_LONG
                ).show()
            }
            video.isFavourite = isChecked
            viewModel.updateFavourite(video)
        }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        p1?.loadVideo(video.videoId)
        p1?.play()
        p1?.setPlaybackEventListener(object : MyPlaybackEventListener() {
            override fun onPlaying() {
                if (!isSubscribed()) {
                    p1.pause()
                    notifyPremium()
                }
            }

        })

        p1?.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
        p1?.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Log.d(TAG, "onInitializationFailure: ${p0.toString()}")
    }

    private fun notifyPremium() {
        Snackbar.make(binding.root, "Premium features", Snackbar.LENGTH_LONG)
            .setAction("ACTIVATE NOW") {
                val uri = Uri.parse("thatchapterfan://subscription")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            .show()
    }

    override fun onResume() {
        updateView()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _youtubeFragment = null
    }
}

