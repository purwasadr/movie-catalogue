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

package com.alurwa.moviecatalogue.core.data

import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.data.source.remote.response.ListMovieResponse
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.main.MovieSortEnum
import com.alurwa.moviecatalogue.utils.DataMapper

class MoviePagingSource() : AppPagingSource<ListMovieResponse, Movie>() {

    private lateinit var apiServiceCallback: suspend (position: Int) -> ListMovieResponse

    constructor(
        apiService: ApiService,
        sort: MovieSortEnum,
    ) : this() {
        apiServiceCallback = {
            if (sort == MovieSortEnum.DISCOVER) {
                apiService.getDiscoverMovies(sort.code, it)
            } else {
                apiService.getMovies(sort.code, it)
            }
        }
    }

    constructor(
        apiService: ApiService,
        query: String,
    ) : this() {
        apiServiceCallback = { position ->
            apiService.searchMovies(query, position)
        }
    }

    override suspend fun response(position: Int): ListMovieResponse =
        apiServiceCallback.invoke(position)

    override fun getTotalPage(response: ListMovieResponse): Int = response.totalPages

    override fun responseToDomain(response: ListMovieResponse): List<Movie> =
        DataMapper.movieResponseToDomain(response.results)
}
