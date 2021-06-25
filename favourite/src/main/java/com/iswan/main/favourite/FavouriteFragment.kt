package com.iswan.main.favourite

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
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iswan.main.core.domain.adapters.VideosPagingDataAdapter
import com.iswan.main.core.domain.model.Video
import com.iswan.main.thatchapterfan.databinding.FragmentFavouriteBinding
import com.iswan.main.thatchapterfan.detail.DetailActivity
import com.iswan.main.thatchapterfan.di.FavouriteDependencies
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteFragment : Fragment() {

    @Inject
    lateinit var factory: FavouriteViewModelFactory
    private val viewModel: FavouriteViewModel by viewModels { factory }

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInject()

        val videoAdapter = VideosPagingDataAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videos.collectLatest {
                videoAdapter.submitData(it)
            }
        }

        with(binding.rvFavourite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = videoAdapter
        }

        videoAdapter.addLoadStateListener { loadState ->
            binding.apply {
                val refresh = loadState.source.refresh

                val empty = refresh is LoadState.NotLoading
                        && videoAdapter.itemCount == 0

                this.apply {
                    binding.viewEmpty.root.visibility =
                        if (empty) View.VISIBLE
                        else View.GONE
                }
            }
        }

        videoAdapter.setOnItemClickCallback (object : VideosPagingDataAdapter.IOnItemClickCallback {
            override fun onItemClick(video: Video) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_VIDEO, video)
                startActivity(intent)
            }
        })
    }

    private fun initInject() {
        DaggerFavouriteComponent.builder()
            .context(requireActivity())
            .favouriteDependencies(EntryPointAccessors.fromApplication(
                requireActivity().applicationContext, FavouriteDependencies::class.java
            ))
            .build()
            .inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}