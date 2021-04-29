package com.alurwa.moviecatalogue.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.alurwa.moviecatalogue.R
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.common.MovieAdapter
import com.alurwa.moviecatalogue.core.common.MovieLoadStateAdapter
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding
import com.alurwa.moviecatalogue.databinding.FragmentTvBinding
import com.alurwa.moviecatalogue.detail.DetailActivity
import com.alurwa.moviecatalogue.tvdetail.TvDetailActivity
import com.alurwa.moviecatalogue.utils.Constants
import com.alurwa.moviecatalogue.utils.SharedPreferencesUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class TvFragment : Fragment() {
    private var _binding: FragmentTvBinding? = null

    private val binding get() = _binding!!

    private val mViewModel by activityViewModels<MainViewModel>()

    private val mAdapter by lazy {
        MovieAdapter(SharedPreferencesUtil.getIsShowPosterPreferences(requireContext())) {
           navigateToDetail(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        setupChips()

        setupRecyclerView()

        setupSwipeToRefresh()
    }

    private fun navigateToDetail(extraId: Int) {
        Intent(requireContext(), TvDetailActivity::class.java)
                .putExtra(Constants.EXTRA_ID, extraId)
                .also { requireContext().startActivity(it) }
    }

    private fun setupAdapter() {
        lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow
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
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = mAdapter.getItemViewType(position)
                return if (viewType == MovieAdapter.VIEW_TYPE_MOVIE) 1 else 2
            }

        }

        binding.rcvMovie.layoutManager = gridLayoutManager
        //   binding.rcvMovie.layoutManager = AutoFitGridLayout(requireContext(), 500)
        binding.rcvMovie.setHasFixedSize(true)

        binding.rcvMovie.adapter = mAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter(),
                footer = MovieLoadStateAdapter() {
                    mAdapter.retry()
                }
        )

        if (mViewModel.listState != null) {
            binding.rcvMovie.layoutManager?.onRestoreInstanceState(mViewModel.listState)
            mViewModel.listState = null
        }
    }

    private fun setupChips() {
        val chipState = mViewModel.chipState

        binding.chipGroupMovie.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.cpDiscovery.id -> getTv(MovieSortEnum.DISCOVER)
                binding.cpPopular.id -> getTv(MovieSortEnum.POPULAR)
                binding.cpUpcoming.id -> getTv(MovieSortEnum.UPCOMING)
                binding.cpTopRated.id -> getTv(MovieSortEnum.TOP_RATING)
            }
        }

        if (chipState != null) {
            binding.chipGroupMovie.check(chipState)
            mViewModel.chipState = null
        } else {
            binding.chipGroupMovie.check(binding.cpDiscovery.id)
        }
    }
    private fun getTv(sortEnum: MovieSortEnum) {
        lifecycleScope.launch {
            mViewModel.getTv(sortEnum).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { mAdapter.refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    companion object {
        const val TAG = "TvFragment"
    }
}