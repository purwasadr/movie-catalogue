/**
 * MIT License
 *
 * Copyright (c) 2021 Purwa Shadr Al 'urwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.alurwa.moviecatalogue.main

import android.os.Bundle
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
import com.alurwa.moviecatalogue.utils.SharedPreferencesUtil
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

    private fun navigateToDetail() {

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

        // Create span for centering Progress Bar
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == MovieAdapter.VIEW_TYPE_MOVIE) 1 else 2
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

    private fun setupChips() {
        val chipState = mViewModel.chipState

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
            mViewModel.chipState = null
        } else {
            binding.chipGroupMovie.check(binding.cpDiscovery.id)
        }
    }
    private fun getMovies(sortEnum: MovieSortEnum) {
        lifecycleScope.launch {
            mViewModel.getMovies(sortEnum).collectLatest {
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