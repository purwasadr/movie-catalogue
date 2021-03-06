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

package com.alurwa.moviecatalogue.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.R
import com.alurwa.moviecatalogue.core.adapter.BoxMovieAdapter
import com.alurwa.moviecatalogue.core.adapter.MovieLoadStateAdapter
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.common.SpacingDecoration
import com.alurwa.moviecatalogue.databinding.ActivitySearchBinding
import com.alurwa.moviecatalogue.filmdetail.FilmDetailActivity
import com.alurwa.moviecatalogue.tvdetail.TvDetailActivity
import com.alurwa.moviecatalogue.utils.CommonUtil
import com.alurwa.moviecatalogue.utils.Constants.EXTRA_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private var searchJob: Job? = null

    private val mViewModel by viewModels<SearchViewModel>()

    private val adapter by lazy {
        BoxMovieAdapter() { navigateToDetail(it) }
    }

    private val filmOrTv: Int by lazy {
        intent.getIntExtra(EXTRA_ID, -1)
    }

    private var currentQueryString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar(savedInstanceState)

        setupAdapter()

        setupRecyclerView(savedInstanceState)
    }

    private fun setupToolbar(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            if (savedInstanceState == null) {
                title = ""
            } else {

                // FIXME: Cannot save state
                // /  currentQueryString = savedInstanceState.getString(QUERY_STRING_STATE, "")
                // title = currentQueryString
                //  searchMovies(currentQueryString)
            }
        }
    }

    private fun setupAdapter() {
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Loading) {
                    binding.pb.isVisible = true
                    binding.rcvSearch.isVisible = false
                    binding.txtEmpty.isVisible = false
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading or Error.
                .filter { it.refresh is LoadState.NotLoading || it.refresh is LoadState.Error }
                .collect {
                    val state = it.refresh
                    if (state is LoadState.NotLoading) {
                        binding.rcvSearch.scrollToPosition(0)
                        binding.rcvSearch.isVisible = true
                        binding.txtEmpty.isVisible = (
                            currentQueryString.isNotEmpty() &&
                                adapter.itemCount == 0
                            )
                    } else if (state is LoadState.Error) {
                        Toast.makeText(
                            applicationContext,
                            "Error bro ${state.error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    binding.pb.isVisible = false
                }
        }
    }

    private fun setupRecyclerView(savedInstanceState: Bundle?) {

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
            rcvSearch.layoutManager = gridLayoutManager
            rcvSearch.addItemDecoration(
                SpacingDecoration(
                    16,
                    16,
                    RecyclerView.VERTICAL
                )
            )
            rcvSearch.setHasFixedSize(true)
            rcvSearch.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter(),
                footer = MovieLoadStateAdapter() {
                    adapter.retry()
                }
            )
            if (savedInstanceState != null) {

                // rcvSearch.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE))
            }
        }
    }

    private fun navigateToDetail(extraId: Int) {
        Intent().putExtra(EXTRA_ID, extraId)
            .also {
                if (filmOrTv == FilmOrTv.FILM.code) {
                    it.setClass(this, FilmDetailActivity::class.java)
                } else {
                    it.setClass(this, TvDetailActivity::class.java)
                }
                startActivity(it)
            }
    }

    private fun setupSearchInput(menu: Menu) {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchMenu = menu.findItem(R.id.search)
        val searchView = searchMenu?.actionView as SearchView

        if (currentQueryString.isEmpty()) {
            searchMenu.expandActionView()
        }

        with(searchView) {
            maxWidth = 10000
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = queryHint()
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    CommonUtil.hideKeyboard(applicationContext, currentFocus)
                    currentQueryString = query
                    searchMovies(query)
                    supportActionBar?.title = query
                    searchMenu.collapseActionView()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchView.setOnSearchClickListener {
            searchView.setQuery(currentQueryString, false)
        }

        searchMenu.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return if (currentQueryString == "") {
                    finish()
                    false
                } else {
                    true
                }
            }
        })
    }

    private fun searchMovies(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            if (query.isNotEmpty()) {
                mViewModel.searchMovie(filmOrTv, query).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun queryHint(): String =
        if (filmOrTv == FilmOrTv.FILM.code) {
            "Search Film"
        } else {
            "Search TV"
        }

    override fun onSaveInstanceState(outState: Bundle) {
        //   outState.putString(QUERY_STRING_STATE, currentQueryString)
        //   outState.putParcelable(LIST_STATE, binding.rcvSearch.layoutManager?.onSaveInstanceState())
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        setupSearchInput(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val QUERY_STRING_STATE = "query_string_state"
        const val LIST_STATE = "list_state"
    }
}
