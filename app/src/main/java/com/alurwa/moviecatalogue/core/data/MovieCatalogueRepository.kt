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

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.alurwa.moviecatalogue.core.data.source.local.ILocalDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.IRemoteDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.main.MovieSortEnum
import com.alurwa.moviecatalogue.utils.DataMapper
import com.alurwa.moviecatalogue.utils.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class MovieCatalogueRepository @Inject constructor(
    private val remoteDataSource: IRemoteDataSource,
    private val localDataSource: ILocalDataSource,
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
) : IMovieCatalogueRepository {

    override fun getFilms(sort: MovieSortEnum): Flow<PagingData<Movie>> =
        handlePager(MoviePagingSource(apiService, sort))

    override fun getSearchMovies(query: String): Flow<PagingData<Movie>> =
        handlePager(MoviePagingSource(apiService, query))

    override fun getFilmDetail(id: Int): Flow<Resource<FilmDetail?>> =
        object : NetworkBoundResource<FilmDetail?, FilmDetailResponse>() {
            override fun loadFromDB(): Flow<FilmDetail?> {
                Timber.d("loadFromDB")
                return localDataSource.getFilmDetail(id).map {
                    DataMapper.filmDetailEntityToDomain(it)
                }
            }

            override fun shouldFetch(): Boolean =
                NetworkState.isNetworkAvailable(context)

            override suspend fun createCall(): Flow<ApiResponse<FilmDetailResponse>> =
                remoteDataSource.getFilmDetail(id)

            override suspend fun saveCallResult(data: FilmDetailResponse) {
                val entity = DataMapper.filmDetailResponseToEntity(data)
                localDataSource.insertFilmDetail(entity)
                Timber.d("SaveCallResult")
            }
        }.asFlow()

    override fun getTvList(sort: MovieSortEnum): Flow<PagingData<Movie>> =
        handlePager(TvPagingSource(apiService, sort))

    override fun getTvDetail(id: Int): Flow<Resource<TvDetail?>> =
        object : NetworkBoundResource<TvDetail?, TvDetailResponse>() {
            override fun loadFromDB(): Flow<TvDetail?> =
                localDataSource.getTvDetail(id).map {
                    DataMapper.tvDetailEntityToDomain(it)
                }

            override fun shouldFetch(): Boolean =
                NetworkState.isNetworkAvailable(context)

            override suspend fun createCall(): Flow<ApiResponse<TvDetailResponse>> =
                remoteDataSource.getTvDetail(id)

            override suspend fun saveCallResult(data: TvDetailResponse) {
                localDataSource.insertTvDetail(
                    DataMapper.tvDetailResponseToEntity(data)
                )
            }
        }.asFlow()

    override fun getTvSearch(query: String): Flow<PagingData<Movie>> =
        handlePager(TvPagingSource(apiService, query))

    private fun <Key : Any, Value : Any> handlePager(
        pagingSource: PagingSource<Key, Value>
    ): Flow<PagingData<Value>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20,
            ),
            pagingSourceFactory = {
                pagingSource
            }
        ).flow
}
