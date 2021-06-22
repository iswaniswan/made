package com.iswan.main.thatchapterfan.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.iswan.main.core.domain.adapters.GeneralLoadStateAdapter
import com.iswan.main.core.domain.adapters.VideosPagingDataAdapter
import com.iswan.main.core.domain.model.Video
import com.iswan.main.thatchapterfan.databinding.FragmentHomeBinding
import com.iswan.main.thatchapterfan.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()

    private lateinit var videoAdapter: VideosPagingDataAdapter

    private val TAG = "HOMEFRAGMENT"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            videoAdapter = VideosPagingDataAdapter()

            with(binding.rvVideos) {
                setHasFixedSize(true)
                adapter = videoAdapter.withLoadStateHeaderAndFooter(
                    header =
                    GeneralLoadStateAdapter {
                        Log.d(TAG, "GeneralLoadStateAdapter --> Header RETRY ")
                        videoAdapter.retry()
                    },
                    footer = GeneralLoadStateAdapter {
                        Log.d(TAG, "GeneralLoadStateAdapter --> Footer RETRY ")
                        videoAdapter.retry()
                    }
                )
                binding.btnLoadRetry.setOnClickListener {
                    videoAdapter.retry()
                }
            }

            videoAdapter.addLoadStateListener { loadState ->
                binding.apply {
                    val refresh = loadState.mediator?.refresh

                    val empty = refresh is LoadState.NotLoading
                            && videoAdapter.itemCount == 0

                    val initialization = refresh is LoadState.Loading

                    this.apply {
                        rvVideos.isVisible = !empty && refresh is LoadState.NotLoading
                        progressBar.isVisible = initialization
                        btnLoadRetry.isVisible = refresh is LoadState.Error
                    }
                }
            }


            videoAdapter.setOnItemClickCallback(object : VideosPagingDataAdapter.IOnItemClickCallback {
                override fun onItemClick(video: Video) {
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_VIDEO, video)
                    startActivity(intent)
                }
            })

            loadData()
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagedVideos.collectLatest { paging ->
                videoAdapter.submitData(paging)
            }
        }
    }

}