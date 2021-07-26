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
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * Created by Purwa Shadr Al 'urwa on 11/07/2021
 */

abstract class AppPagingSource<ResponseType, DomainType : Any> : PagingSource<Int, DomainType>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DomainType> {
        val position = params.key ?: STARTING_PAGING_INDEX
        return try {
            val response = response(position)

            val maxPage = getTotalPage(response)
            val movieList = responseToDomain(response)
            val nextKey = if (position == maxPage || maxPage == 0) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = movieList,
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

    override fun getRefreshKey(state: PagingState<Int, DomainType>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }

    protected abstract suspend fun response(position: Int): ResponseType

    protected abstract fun getTotalPage(response: ResponseType): Int

    protected abstract fun responseToDomain(response: ResponseType): List<DomainType>

    companion object {
        const val STARTING_PAGING_INDEX = 1
        const val TAG = "AppPagingSource"
    }
}
