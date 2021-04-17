package com.alurwa.moviecatalogue.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.alurwa.moviecatalogue.core.common.MovieAdapter
import com.alurwa.moviecatalogue.core.common.MovieLoadStateAdapter
import com.alurwa.moviecatalogue.core.utils.SharedPreferencesUtil
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieFragment : Fragment() {
    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!

    private val mViewModel by activityViewModels<MainViewModel>()

    private val adapter by lazy {
        MovieAdapter(SharedPreferencesUtil.getIsShowPosterPreferences(requireContext())) {
            (activity as MainActivity).navigateToDetail(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        setupChips(savedInstanceState)

        setupRecyclerView()

        setupSwipeToRefresh()

    }

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

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == 100) 1 else 2
            }

        }

        binding.rcvMovie.layoutManager = gridLayoutManager
        //   binding.rcvMovie.layoutManager = AutoFitGridLayout(requireContext(), 500)
        binding.rcvMovie.setHasFixedSize(true)

        binding.rcvMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter(),
                footer = MovieLoadStateAdapter() {
                    adapter.retry()
                }
        )

        if (mViewModel.listState != null) {
            binding.rcvMovie.layoutManager?.onRestoreInstanceState(mViewModel.listState)
            mViewModel.listState = null
        }
    }

    private fun setupChips(savedInstanceState: Bundle?) {
        val chipState = mViewModel.chipState

        binding.chipGroupMovie.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.cpDiscover.id -> {
                    getMovies(MovieSortEnum.DISCOVER)
                }
                binding.cpPopular.id -> {
                    getMovies(MovieSortEnum.POPULAR)
                }
                binding.cpUpcoming.id -> {
                    getMovies(MovieSortEnum.UPCOMING)
                }
            }
        }

        if (chipState != null) {
            binding.chipGroupMovie.check(chipState)
            mViewModel.chipState = null
        } else {
            binding.chipGroupMovie.check(binding.cpDiscover.id)
        }
    }
    private fun getMovies(sortEnum: MovieSortEnum) {
        lifecycleScope.launch {
            mViewModel.getMovies(sortEnum).collectLatest {
               // Log.d(TAG, "Collect Lastest")
                adapter.submitData(it)
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.listState = binding.rcvMovie.layoutManager?.onSaveInstanceState()
        mViewModel.chipState = binding.chipGroupMovie.checkedChipId
        _binding = null
    }

    companion object {
        const val TAG = "MovieFragment"
    }
}