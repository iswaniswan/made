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
import androidx.lifecycle.*
import androidx.paging.LoadState
import com.iswan.main.core.domain.adapters.GeneralLoadStateAdapter
import com.iswan.main.core.domain.adapters.VideosPagingDataAdapter
import com.iswan.main.core.domain.model.Video
import com.iswan.main.thatchapterfan.databinding.FragmentHomeBinding
import com.iswan.main.thatchapterfan.detail.DetailActivity
import com.iswan.main.thatchapterfan.home.extensions.viewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()

    private var binding: FragmentHomeBinding by viewLifecycle()

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
            val videoAdapter = VideosPagingDataAdapter()

            with(binding.rvVideos) {
                setHasFixedSize(true)
                adapter = videoAdapter.withLoadStateHeaderAndFooter(
                    header =
                    GeneralLoadStateAdapter {
                        videoAdapter.retry()
                    },
                    footer = GeneralLoadStateAdapter {
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

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.pagedVideos.collectLatest { paging ->
                    videoAdapter.submitData(paging)
                }
            }
        }
    }

}