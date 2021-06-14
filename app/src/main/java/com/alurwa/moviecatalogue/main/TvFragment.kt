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
                submitList(it)
            }
        }
    }
}
