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

package com.alurwa.moviecatalogue.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.data.source.remote.response.ListMovieResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvListResponse
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.Tv
import com.alurwa.moviecatalogue.main.MovieSortEnum

/**
 * Created by Purwa Shadr Al 'urwa on 28/04/2021.
 */

/*class TvPagingSource3(
        private val  apiService: ApiService,
                     private val sort: MovieSortEnum
                     ) : AppPagingSource<TvListResponse, Tv>() {

    override suspend fun getMovieApi(position: Int): TvListResponse =
            if (sort == MovieSortEnum.DISCOVER)  {
                apiService.getDiscoverTv(sort.code, position)
            } else {
                apiService.getTv(sort.code, position)
            }

    override suspend fun maxPage(): Int {

        getsResponse()?.totalPages
    }

    override suspend fun dataResult(): List<Tv> {
        TODO("Not yet implemented")
    }
}

 */