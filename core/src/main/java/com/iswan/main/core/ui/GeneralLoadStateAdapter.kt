package com.iswan.main.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iswan.main.core.databinding.LoadStateBinding

class GeneralLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<GeneralLoadStateAdapter.MovieLoadStateViewHolder>() {

    inner class MovieLoadStateViewHolder(private val binding: LoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnLoadRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressBar.isVisible = loadState is LoadState.Loading
                btnLoadRetry.isVisible = loadState !is LoadState.Loading
                tvLoadError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MovieLoadStateViewHolder {
        val binding =
            LoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

}