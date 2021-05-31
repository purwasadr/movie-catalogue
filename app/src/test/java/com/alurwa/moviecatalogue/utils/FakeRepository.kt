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

package com.alurwa.moviecatalogue.utils

import androidx.paging.PagingData
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.main.MovieSortEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeRepository : IMovieCatalogueRepository {

    override fun getFilms(sort: MovieSortEnum): Flow<PagingData<Movie>> {
        return emptyFlow()
    }

    override fun getSearchMovies(query: String): Flow<PagingData<Movie>> {
        return emptyFlow()
    }

    override fun getFilmDetail(id: Int): Flow<Resource<FilmDetail?>> {
        return emptyFlow()
    }

    override fun getTvList(sort: MovieSortEnum): Flow<PagingData<Movie>> {
        return emptyFlow()
    }

    override fun getTvDetail(id: Int): Flow<Resource<TvDetail?>> {
        return emptyFlow()
    }

    override fun getTvSearch(query: String): Flow<PagingData<Movie>> {
        return emptyFlow()
    }
}
