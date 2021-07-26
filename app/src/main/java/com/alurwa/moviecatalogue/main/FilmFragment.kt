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
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.moviecatalogue.boxlist.BoxListActivity
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import com.alurwa.moviecatalogue.databinding.ListNestedCarouselItemBinding
import com.alurwa.moviecatalogue.filmdetail.FilmDetailActivity
import com.alurwa.moviecatalogue.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class FilmFragment : MovieFragmentAbstract() {
    override fun navigateToDetail(extraId: Int) {
        Intent(requireContext(), FilmDetailActivity::class.java)
            .putExtra(Constants.EXTRA_ID, extraId)
            .also { requireContext().startActivity(it) }
    }

    override fun navigateToList(which: Int) {
        val movieSortEnum = when (which) {
            0 -> MovieSortEnum.DISCOVER.name
            1 -> MovieSortEnum.POPULAR.name
            2 -> MovieSortEnum.UPCOMING.name
            3 -> MovieSortEnum.TOP_RATING.name
            else -> throw IllegalArgumentException("no found in moviesortenum")
        }

        Intent(requireContext(), BoxListActivity::class.java)
            .putExtra(BoxListActivity.EXTRA_FILM_OR_MOVIE, FilmOrTv.FILM.code)
            .putExtra(BoxListActivity.EXTRA_MOVIE_SORT, movieSortEnum)
            .also { requireContext().startActivity(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupList(list: List<CarouselMenu>) {
        super.setupList(list)
    }

    override fun onNotLoading() {
        super.onNotLoading()
        Timber.d("OnNotLoading method")
        viewModel.stateNestedScrollViewFilm?.also {
            binding.nsvMovie.scrollY = it
        }
    }

    override fun setupLinearLayout(linearLayoutManager: LinearLayoutManager, index: Int) {
        viewModel.stateFilm?.let {
            linearLayoutManager.onRestoreInstanceState(it.get(index))
        }
    }

    override fun getCarousels() {
        lifecycleScope.launch {
            viewModel.filmMenu.collectLatest {
                submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        val list = mutableListOf<Parcelable?>()

        for (i in 0 until binding.llList.childCount) {

            val child = ListNestedCarouselItemBinding.bind(binding.llList.getChildAt(i))

            list.add(child.rcv.layoutManager?.onSaveInstanceState())
        }

        viewModel.stateFilm = list
        viewModel.stateNestedScrollViewFilm = binding.nsvMovie.scrollY

        super.onDestroyView()
    }
}
