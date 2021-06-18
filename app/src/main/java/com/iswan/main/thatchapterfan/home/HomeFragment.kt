package com.iswan.main.thatchapterfan.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iswan.main.thatchapterfan.R
import com.iswan.main.core.data.Resource
import com.iswan.main.core.domain.model.Video
import com.iswan.main.core.ui.VideosAdapter
import com.iswan.main.thatchapterfan.databinding.FragmentHomeBinding
import com.iswan.main.thatchapterfan.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()

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
            val videoAdapter = VideosAdapter()

            with(binding.rvVideos) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = videoAdapter
            }

            videoAdapter.setOnItemCallback(object : VideosAdapter.IOnItemCallback {
                override fun onItemClick(video: Video) {
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_VIDEO, video)
                    startActivity(intent)
                }
            })

            viewModel.videos.observe(viewLifecycleOwner, {
                if (it != null) {
                    when (it) {
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvVideos.visibility = View.VISIBLE
                            videoAdapter.setData(it.data)
                        }
                        is com.iswan.main.core.data.Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvVideos.visibility = View.GONE
                        }
                        is com.iswan.main.core.data.Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvVideos.visibility = View.GONE
                            Snackbar.make(binding.root, getString(R.string.view_error), Snackbar.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Snackbar.make(binding.root, getString(R.string.view_error), Snackbar.LENGTH_LONG).show()
                }
            })

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}