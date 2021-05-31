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

package com.alurwa.moviecatalogue.boxlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.main.MovieSortEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Purwa Shadr Al 'urwa on 27/05/2021
 */

@HiltViewModel
class BoxListViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: IMovieCatalogueRepository
) : ViewModel() {
    val movies = if (filmOrTv == FilmOrTv.FILM.code) {
        repository.getFilms(MovieSortEnum.valueOf(movieSort)).cachedIn(viewModelScope)
    } else if (filmOrTv == FilmOrTv.TV.code) {
        repository.getTvList(MovieSortEnum.valueOf(movieSort)).cachedIn(viewModelScope)
    } else {
        throw IllegalArgumentException("Intent not match any")
    }

    val filmOrTv
        get() = stateHandle.get<Int>(BoxListActivity.EXTRA_FILM_OR_MOVIE)!!

    val movieSort
        get() = stateHandle.get<String>(BoxListActivity.EXTRA_MOVIE_SORT)!!
}
