/*
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

package com.alurwa.moviecatalogue.boxlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.adapter.BoxMovieAdapter
import com.alurwa.moviecatalogue.core.adapter.MovieLoadStateAdapter
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.common.SpacingDecoration
import com.alurwa.moviecatalogue.databinding.ActivityBoxListBinding
import com.alurwa.moviecatalogue.filmdetail.FilmDetailActivity
import com.alurwa.moviecatalogue.tvdetail.TvDetailActivity
import com.alurwa.moviecatalogue.utils.CommonUtil
import com.alurwa.moviecatalogue.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

@AndroidEntryPoint
class BoxListActivity : AppCompatActivity() {
    private val binding: ActivityBoxListBinding by lazy {
        ActivityBoxListBinding.inflate(layoutInflater)
    }

    private val adapter: BoxMovieAdapter by lazy {
        BoxMovieAdapter() {
            navigateToDetail(it)
        }
    }

    private val viewModel: BoxListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()

        setupRecyclerView()

        setupAdapter()

        setupSwipeToRefresh()

        getMovies()
    }

    private fun setupRecyclerView() {
        // set Column width with total view with dp and margin which you want
        val noOfColumn = CommonUtil.calculateNoOfColumns(
            applicationContext,
            132F
        )

        val gridLayoutManager = GridLayoutManager(
            applicationContext,
            noOfColumn
        )

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == BoxMovieAdapter.VIEW_TYPE_MOVIE) 1 else noOfColumn
            }
        }

        with(binding) {
            list.setHasFixedSize(true)
            list.addItemDecoration(
                SpacingDecoration(
                    16,
                    16,
                    RecyclerView.VERTICAL
                )
            )
            list.layoutManager = gridLayoutManager
            list.adapter = adapter.withLoadStateHeaderAndFooter(
                MovieLoadStateAdapter(),
                MovieLoadStateAdapter() {
                    adapter.retry()
                }
            )
        }
    }

    private fun setupAdapter() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
                }
            }
        }

        lifecycleScope.launch {
          //  lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    // Only emit when REFRESH LoadState for RemoteMediator changes.
                    .distinctUntilChangedBy { it.refresh }
                    // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect {
                        binding.list.scrollToPosition(0)
                        Timber.d("binding.list.scrollToPosition(0)")
                    }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun setupToolbar() {
        supportActionBar?.apply {

            // Use
            title = viewModel.movieSort
                .lowercase()
                .replace('_', ' ', true)
                .replaceFirstChar { it.titlecase(Locale.getDefault()) }

            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun navigateToDetail(extraId: Int) {
        Intent().putExtra(Constants.EXTRA_ID, extraId)
            .also {
                if (viewModel.filmOrTv == FilmOrTv.FILM.code) {
                    it.setClass(this, FilmDetailActivity::class.java)
                } else {
                    it.setClass(this, TvDetailActivity::class.java)
                }
                startActivity(it)
            }
    }

    private fun getMovies() {
        lifecycleScope.launch {
            viewModel.movies.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        const val EXTRA_FILM_OR_MOVIE = "extra_film_or_movie"

        const val EXTRA_MOVIE_SORT = "extra_movie_sort"
    }
}
