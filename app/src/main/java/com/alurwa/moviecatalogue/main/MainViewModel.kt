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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IMovieCatalogueRepository
) : ViewModel() {
    val filmMenu = flow {
        val carouselList = listOf(
            CarouselMenu(
                "Discover",
                repository.getFilms(MovieSortEnum.DISCOVER)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Popular",
                repository.getFilms(MovieSortEnum.POPULAR)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Upcoming",
                repository.getFilms(MovieSortEnum.UPCOMING)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Top Rating",
                repository.getFilms(MovieSortEnum.TOP_RATING)
                    .cachedIn(viewModelScope).first()
            )
        )
        Timber.d("Masuk Flow")

        emit(carouselList)
    }.shareIn(viewModelScope, SharingStarted.Lazily, 1)

    val tvMenu = flow {
        val carouselList = listOf(
            CarouselMenu(
                "Discover",
                repository.getTvList(MovieSortEnum.DISCOVER)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Popular",
                repository.getTvList(MovieSortEnum.POPULAR)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Top Rating",
                repository.getTvList(MovieSortEnum.TOP_RATING)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Top Rating",
                repository.getTvList(MovieSortEnum.TOP_RATING)
                    .cachedIn(viewModelScope).first()
            )
        )
        Timber.d("Masuk Flow")

        emit(carouselList)
    }.shareIn(viewModelScope, SharingStarted.Lazily, 1)
}
