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
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iswan.main.thatchapterfan.R
import com.iswan.main.core.data.Resource
import com.iswan.main.core.data.source.utils.Mapper
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.ui.GeneralLoadStateAdapter
import com.iswan.main.core.ui.VideosAdapter
import com.iswan.main.core.ui.VideosPagingDataAdapter
import com.iswan.main.thatchapterfan.databinding.FragmentHomeBinding
import com.iswan.main.thatchapterfan.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
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
//            val videoAdapter = VideosAdapter()
            videoAdapter = VideosPagingDataAdapter()

            with(binding.rvVideos) {
                setHasFixedSize(true)
                adapter = videoAdapter.withLoadStateHeaderAndFooter(
                    header = GeneralLoadStateAdapter { loadData() },
                    footer = GeneralLoadStateAdapter { loadData() }
                )
                binding.btnLoadRetry.setOnClickListener {
                    videoAdapter.retry()
                }
            }

            videoAdapter.addLoadStateListener { loadState ->
                binding.apply {
                    val refresh = loadState.source.refresh

                    val empty = refresh is LoadState.NotLoading
                            && videoAdapter.itemCount == 0

                    val initialization = refresh is LoadState.Loading

                    binding.apply {
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


//            viewModel.pagedVideos.observe(viewLifecycleOwner, {
//                if (it != null) {
//                    when (it) {
//                        is Resource.Success -> {
//                            binding.progressBar.visibility = View.GONE
//                            binding.rvVideos.visibility = View.VISIBLE
//                            videoAdapter.submitData(it)
//                        }
//                        is Resource.Loading -> {
//                            binding.progressBar.visibility = View.VISIBLE
//                            binding.rvVideos.visibility = View.GONE
//                        }
//                        is Resource.Error -> {
//                            binding.progressBar.visibility = View.GONE
//                            binding.rvVideos.visibility = View.GONE
//                            Snackbar.make(binding.root, getString(R.string.view_error), Snackbar.LENGTH_LONG).show()
//                        }
//                    }
//                } else {
//                    Snackbar.make(binding.root, getString(R.string.view_error), Snackbar.LENGTH_LONG).show()
//                }
//            })



        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagedVideos.collectLatest {
                submitJob(it)
            }
        }
    }

    private var job: Job = Job()

    private fun submitJob(data: PagingData<Video>) {
        job.cancel()
        job = lifecycleScope.launch {
            videoAdapter.submitData(data)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}