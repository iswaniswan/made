package com.iswan.main.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iswan.main.core.databinding.ItemVideoBinding
import com.iswan.main.core.domain.model.Video

class VideosAdapter: RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    private var videos = ArrayList<Video>()

    fun setData(videos: List<Video>?) {
        if (videos == null) return
        this.videos.clear()
        this.videos.addAll(videos)
        this.notifyDataSetChanged()
    }

    private var iOnItemCallback: IOnItemCallback? = null

    interface IOnItemCallback {
        fun onItemClick(video: Video)
    }

    fun setOnItemCallback(callback: IOnItemCallback) {
        iOnItemCallback = callback
    }

    inner class VideoViewHolder(private val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
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
                    iOnItemCallback?.onItemClick(video)
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideosAdapter.VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideosAdapter.VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int = videos.size
}