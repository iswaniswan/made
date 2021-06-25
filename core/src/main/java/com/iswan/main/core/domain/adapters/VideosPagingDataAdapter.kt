package com.iswan.main.core.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iswan.main.core.databinding.ItemVideoBinding
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.utils.Utils

class VideosPagingDataAdapter:
    PagingDataAdapter<Video, VideosPagingDataAdapter.MovieViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem.videoId == newItem.videoId

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem == newItem

        }
    }

    private var iOnItemClickCallback: IOnItemClickCallback? = null

    interface IOnItemClickCallback {
        fun onItemClick(video: Video)
    }

    fun setOnItemClickCallback(iOnItemClickCallback: IOnItemClickCallback) {
        this.iOnItemClickCallback = iOnItemClickCallback
    }

    inner class MovieViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            with(binding) {
                tvTitle.text = video.title
                tvDuration.text = Utils.extractISOTimeToString(video.duration)
                tvPublished.text = Utils.convertISO8601toTimeAgo(video.publishedAt)
                tvViews.text = Utils.simplifyOfNumber(video.viewCount, " Views")
                Glide.with(itemView.context)
                    .load(video.thumbnails)
                    .into(ivThumbnails)
                itemView.setOnClickListener {
                    iOnItemClickCallback?.onItemClick(video)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        /* map to model here */
        if (currentItem != null) holder.bind(
            currentItem
        )
    }

}