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

package com.alurwa.moviecatalogue.core.data.source.remote

import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.utils.handleRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val dispatchers: CoroutineDispatcher
) : IRemoteDataSource {

    override fun getFilmDetail(id: Int) = flow {
        emit(handleRequest { apiService.getFilmDetail(id) })
//        try {
//            val results = apiService.getFilmDetail(id)
//            emit(ApiResponse.Success(results))
//        } catch (e: Exception) {
//            emit(ApiResponse.Error(e.toString()))
//        }
    }.flowOn(dispatchers)

    override fun getTvDetail(id: Int) = flow {
//        try {
//            val results = apiService.getTvDetail(id)
//            emit(ApiResponse.Success(results))
//        } catch (e: Exception) {
//            emit(ApiResponse.Error(e.toString()))
//        }
        emit(handleRequest { apiService.getTvDetail(id) })
    }.flowOn(dispatchers)
}
