package com.iswan.main.thatchapterfan.detail.utils

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.utils.Utils
import com.iswan.main.thatchapterfan.BuildConfig
import com.iswan.main.thatchapterfan.R
import com.iswan.main.thatchapterfan.databinding.ActivityDetailBinding
import com.iswan.main.thatchapterfan.detail.DetailViewModel
import java.lang.ref.WeakReference
import java.text.NumberFormat

class LeakyPlayer(
    private val video: Video,
    detailActivity: AppCompatActivity,
    private val viewModel: DetailViewModel,
    private val binding: ActivityDetailBinding
) {
    private val activity: WeakReference<AppCompatActivity> =
        WeakReference(detailActivity)

    private val youtubeFragment: WeakReference<YouTubePlayerSupportFragmentX> =
        WeakReference(YouTubePlayerSupportFragmentX.newInstance())

    private val TAG = "LeakyPlayer"

    fun playVideo() {
        val fragmentManager = activity.get()?.supportFragmentManager
        fragmentManager?.commit {
            youtubeFragment.get()?.let {
                add(R.id.youtube_fragment, it)
            }
        }

        youtubeFragment.get()
            ?.initialize(BuildConfig.API_KEY, object : YouTubePlayer.OnInitializedListener {
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
                    p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?
                ) = Unit
            })
        updateView()
    }

    fun updateView() {
        with(binding) {
            tvTitle.text = video.title

            val publishedAtValue = StringBuilder()
                .append(activity.get()?.getString(R.string.publishedOn))
                .append(" ")
                .append(Utils.convertISO8601toDate(video.publishedAt))
            tvPublished.text = publishedAtValue

            val viewsText = StringBuilder()
                .append(NumberFormat.getInstance().format(video.viewCount))
                .append(" Views")
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
                    activity.get(),
                    activity.get()?.getString(R.string.add_to_favourite), Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    activity.get(),
                    activity.get()?.getString(R.string.remove_from_favourite), Toast.LENGTH_LONG
                ).show()
            }
            video.isFavourite = isChecked
            viewModel.updateFavourite(video)
        }

    private fun notifyPremium() {
        Snackbar.make(
            binding.root,
            activity.get()?.getString(R.string.premium_features).toString(),
            Snackbar.LENGTH_LONG
        )
            .setAction(
                activity.get()?.getString(R.string.activate_now).toString()
            ) {
                val uri = Uri.parse("thatchapterfan://subscription")
                activity.get()?.startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            .show()
    }
}