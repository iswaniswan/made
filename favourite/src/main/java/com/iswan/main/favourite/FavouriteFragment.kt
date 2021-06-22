package com.iswan.main.favourite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iswan.main.core.domain.adapters.VideosPagingDataAdapter
import com.iswan.main.core.domain.model.Video
import com.iswan.main.thatchapterfan.databinding.FragmentFavouriteBinding
import com.iswan.main.thatchapterfan.detail.DetailActivity
import com.iswan.main.thatchapterfan.di.FavouriteDependencies
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteFragment : Fragment() {

    @Inject
    lateinit var factory: FavouriteViewModelFactory
    private val viewModel: FavouriteViewModel by viewModels { factory }

    private lateinit var binding: FragmentFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInject()
        val videoAdapter = VideosPagingDataAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videos.collect {
                videoAdapter.submitData(it)
            }
        }

        with(binding.rvFavourite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = videoAdapter
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

}