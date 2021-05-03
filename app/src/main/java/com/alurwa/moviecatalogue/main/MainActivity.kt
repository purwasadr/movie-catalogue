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

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.alurwa.moviecatalogue.R
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.common.MovieAdapter
import com.alurwa.moviecatalogue.core.common.MovieLoadStateAdapter
import com.alurwa.moviecatalogue.databinding.ActivityMainBinding
import com.alurwa.moviecatalogue.detail.DetailActivity
import com.alurwa.moviecatalogue.search.SearchActivity
import com.alurwa.moviecatalogue.tvdetail.TvDetailActivity
import com.alurwa.moviecatalogue.utils.Constants
import com.alurwa.moviecatalogue.utils.SharedPreferencesUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //private val fragmentManager: FragmentManager = supportFragmentManager

    private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
        // val fragment: Fragment

        when (it.itemId) {
            R.id.movie -> {
                submitMovieAdapter(getSortFromChip(binding.chipGroupMovie.checkedChipId))
                true
            }

            R.id.tv -> {
                /*     fragment = TvFragment()
                     fragmentManager.beginTransaction()
                             .replace(R.id.content_main, fragment, TvFragment.TAG)
                             .commit()

                 */
                submitTvAdapter(getSortFromChip(binding.chipGroupMovie.checkedChipId))
                true
            }

            else -> false
        }
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val filmOrTv
        get() = when (binding.bottomNavMain.selectedItemId) {
            R.id.movie -> {
                FilmOrTv.FILM
            }
            R.id.tv -> {
                FilmOrTv.TV
            }
            else -> {
                throw IllegalArgumentException("Out of range buttom nav")
            }
        }

    private val adapter by lazy {
        MovieAdapter(SharedPreferencesUtil.getIsShowPosterPreferences(this)) {
            navigateToDetail(it)
        }
    }

    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(
            savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigation(savedInstanceState)

        setupAdapter()

        setupChips()

        setupRecyclerView()

        setupSwipeToRefresh()

        initSubmitAdapter(savedInstanceState)
    }

    private fun initSubmitAdapter(savedInstanceState: Bundle?) {
        submitMovieAdapter(MovieSortEnum.DISCOVER)
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.bottomNavMain.selectedItemId = R.id.movie
        }
        binding.bottomNavMain.setOnNavigationItemSelectedListener(bottomNavigationListener)
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
        val gridLayoutManager = GridLayoutManager(applicationContext, 2)
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
    }

    private fun setupChips() {
        val chipState = mViewModel.chipState

        if (chipState == null) {
            binding.chipGroupMovie.check(R.id.cp_discovery)
        }

        binding.chipGroupMovie.setOnCheckedChangeListener { _, checkedId ->
            if (filmOrTv == FilmOrTv.FILM) {
                submitMovieAdapter(getSortFromChip(checkedId))
            } else {
                submitTvAdapter(getSortFromChip(checkedId))
            }

        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun getIsShowPosterPref(): Boolean {
        return SharedPreferencesUtil.getIsShowPosterPreferences(applicationContext)
    }

    private fun navigateToSearch() {
        Intent(this, SearchActivity::class.java)
                .putExtra(Constants.EXTRA_ID, filmOrTv.code)
                .also {
                    startActivity(it)
                }
    }

    private fun navigateToDetail(id: Int) {
        if (filmOrTv == FilmOrTv.FILM) {
            Intent(this, DetailActivity::class.java)
                    .putExtra(Constants.EXTRA_ID, id)
                    .also { startActivity(it) }
        } else {
            Intent(this, TvDetailActivity::class.java)
                    .putExtra(Constants.EXTRA_ID, id)
                    .also { startActivity(it) }
        }

    }

    private fun getSortFromChip(checkedId: Int): MovieSortEnum {
        return when (checkedId) {
            binding.cpDiscovery.id -> MovieSortEnum.DISCOVER
            binding.cpPopular.id -> MovieSortEnum.POPULAR
            binding.cpUpcoming.id -> MovieSortEnum.UPCOMING
            binding.cpTopRated.id -> MovieSortEnum.TOP_RATING
            else -> MovieSortEnum.DISCOVER
        }
    }

    private fun submitMovieAdapter(sortEnum: MovieSortEnum) {

        lifecycleScope.launch {
            mViewModel.getMovies(sortEnum).collectLatest {
                adapter.submitData(it)
            }

        }
    }

    private fun submitTvAdapter(sortEnum: MovieSortEnum) {
        lifecycleScope.launch {
            mViewModel.getTv(sortEnum).collectLatest {
                adapter.submitData(it)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(R.id.show_poster)?.isChecked = getIsShowPosterPref()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_main -> {
                navigateToSearch()
                true
            }

            R.id.show_poster -> {
                val pref = SharedPreferencesUtil.getSharedPreferences(applicationContext)

                item.isChecked = !item.isChecked

                if (item.isChecked) {
                    pref.edit {
                        putBoolean(Constants.SharedPreference.IS_SHOW_POSTER, true)
                        apply()
                    }

                } else {
                    pref.edit {
                        putBoolean(Constants.SharedPreference.IS_SHOW_POSTER, false)
                        apply()
                    }
                }
                true
            }

            else -> false
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}