package com.alurwa.moviecatalogue.boxlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

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

    private fun setupToolbar() {
        supportActionBar?.apply {
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