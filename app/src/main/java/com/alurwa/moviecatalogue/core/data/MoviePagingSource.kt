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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.data.source.remote.response.ListMovieResponse
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.main.MovieSortEnum
import com.alurwa.moviecatalogue.utils.DataMapper
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviePagingSource(
    private val apiService: ApiService,
    private val sort: MovieSortEnum? = null,
    private val query: String? = null
) : PagingSource<Int, Movie>() {

    constructor(
        apiService: ApiService,
        sort: MovieSortEnum
    ) : this(apiService, sort, null)

    constructor(
        apiService: ApiService,
        query: String,
    ) : this(apiService, null, query)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGING_INDEX
        return try {
            val response = when {
                sort != null -> getMovieApi(sort, position)
                query != null -> {
                    apiService.searchMovies(query, position)
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }

            val maxPage = response.totalPages
            val repos = DataMapper.movieResponseToDomain(response.results)
            val nextKey = if (position == maxPage || maxPage == 0) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = repos,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (ex: IOException) {
            Timber.d(ex)
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            Timber.d(ex)
            return LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }

    private suspend fun getMovieApi(sort: MovieSortEnum, position: Int): ListMovieResponse {
        return if (sort == MovieSortEnum.DISCOVER) {
            apiService.getDiscoverMovies(sort.code, position)
        } else {
            apiService.getMovies(sort.code, position)
        }
    }

    companion object {
        const val STARTING_PAGING_INDEX = 1
        const val TAG = "MoviePagingSource"
    }
}
