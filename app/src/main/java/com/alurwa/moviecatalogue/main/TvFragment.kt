package com.alurwa.moviecatalogue.main

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.alurwa.moviecatalogue.boxlist.BoxListActivity
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.tvdetail.TvDetailActivity
import com.alurwa.moviecatalogue.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TvFragment : MovieFragmentAbstract() {
    override fun navigateToDetail(extraId: Int) {
        Intent(requireContext(), TvDetailActivity::class.java)
            .putExtra(Constants.EXTRA_ID, extraId)
            .also { requireContext().startActivity(it) }
    }

    override fun navigateToList(which: Int) {
        val movieSortEnum = when (which) {
            0 -> MovieSortEnum.DISCOVER.name
            1 -> MovieSortEnum.POPULAR.name
            2 -> MovieSortEnum.UPCOMING.name
            3 -> MovieSortEnum.TOP_RATING.name
            else -> throw IllegalArgumentException("Not match to enum")
        }

        Intent(requireContext(), BoxListActivity::class.java)
            .putExtra(BoxListActivity.EXTRA_FILM_OR_MOVIE, FilmOrTv.TV.code)
            .putExtra(BoxListActivity.EXTRA_MOVIE_SORT, movieSortEnum)
            .also { requireContext().startActivity(it) }
    }

    override fun getCarousels() {
        lifecycleScope.launch {
            viewModel.tvMenu.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}