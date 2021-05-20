package com.alurwa.moviecatalogue.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.alurwa.moviecatalogue.core.common.AutoFitGridLayout
import com.alurwa.moviecatalogue.core.common.MovieAdapter
import com.alurwa.moviecatalogue.core.common.MovieLoadStateAdapter
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding
import com.alurwa.moviecatalogue.utils.SharedPreferencesUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

/**
 * Created by Purwa Shadr Al 'urwa on 20/05/2021
 */

abstract class MovieFragmentAbstract : Fragment() {
    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!

    val viewModel by activityViewModels<MainViewModel>()

    val adapter by lazy {
        MovieAdapter(SharedPreferencesUtil.getIsShowPosterPreferences(requireContext())) {
            navigateToDetail(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        setupChips()

        setupRecyclerView()

        setupSwipeToRefresh()
    }

    abstract fun navigateToDetail(extraId: Int)

    private fun setupAdapter() {
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    binding.rcvMovie.scrollToPosition(0)
                }
        }
    }

    private fun setupRecyclerView() {

        // Create span for centering Progress Bar
        /*  val gridLayoutManager = GridLayoutManager(requireContext(), 2)
          gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
              override fun getSpanSize(position: Int): Int {
                  val viewType = adapter.getItemViewType(position)
                  return if (viewType == MovieAdapter.VIEW_TYPE_MOVIE) 1 else 2
              }
          }

         */

        val autoFitGridLayout = AutoFitGridLayout(requireContext(), 150)

        autoFitGridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == MovieAdapter.VIEW_TYPE_MOVIE) 1 else autoFitGridLayout.spanCount
            }
        }

        binding.rcvMovie.layoutManager = autoFitGridLayout
        //   binding.rcvMovie.layoutManager = AutoFitGridLayout(requireContext(), 500)
        binding.rcvMovie.setHasFixedSize(true)

        binding.rcvMovie.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter(),
            footer = MovieLoadStateAdapter() {
                adapter.retry()
            }
        )

        if (viewModel.listState != null) {
            binding.rcvMovie.layoutManager?.onRestoreInstanceState(viewModel.listState)
            viewModel.listState = null
        }
    }

    private fun setupChips() {
        val chipState = viewModel.chipState

        binding.chipGroupMovie.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.cpDiscovery.id -> getMovies(MovieSortEnum.DISCOVER)
                binding.cpPopular.id -> getMovies(MovieSortEnum.POPULAR)
                binding.cpUpcoming.id -> getMovies(MovieSortEnum.UPCOMING)
                binding.cpTopRated.id -> getMovies(MovieSortEnum.TOP_RATING)
            }
        }

        if (chipState != null) {
            binding.chipGroupMovie.check(chipState)
            viewModel.chipState = null
        } else {
            binding.chipGroupMovie.check(binding.cpDiscovery.id)
        }
    }

    abstract fun getMovies(sortEnum: MovieSortEnum)

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // mViewModel.listState = binding.rcvMovie.layoutManager?.onSaveInstanceState()
        //mViewModel.chipState = binding.chipGroupMovie.checkedChipId
        _binding = null
    }
}