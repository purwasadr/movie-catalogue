package com.alurwa.moviecatalogue.core.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.data.MoviePagingSource
import com.alurwa.moviecatalogue.core.data.TvPagingSource
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.Tv
import com.alurwa.moviecatalogue.main.MovieSortEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(val apiService: ApiService) {
    suspend fun getAllMovies(): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val response = apiService.getAllMovies()
                val dataArray = response.results

                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }

            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMovieDiscover(): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val response = apiService.getAllMovies()
                val dataArray = response.results

                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }

            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }

        }.flowOn(Dispatchers.IO)
    }

    fun getDiscoveryMovies(sort: MovieSortEnum): Flow<PagingData<Movie>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = true,

                        maxSize = 60,
                ),
                pagingSourceFactory = { MoviePagingSource(apiService,sort = sort) }
        ).flow
    }

    fun getSearchMovies(query: String): Flow<PagingData<Movie>> =
            Pager(
                    config = PagingConfig(
                            pageSize = 20,
                            enablePlaceholders = true,

                            maxSize = 60
                    ),
                    pagingSourceFactory = { MoviePagingSource(apiService,query = query) }
            ).flow

    fun getMovieDetail(id: Int) = flow<ApiResponse<MovieDetailResponse>> {
        try {
            val results = apiService.getDetailMovie(id)
            emit(ApiResponse.Success(results))

        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getTvDetail(id: Int) = flow<ApiResponse<TvDetailResponse>> {
            try {
                val results = apiService.getTvDetail(id)
                emit(ApiResponse.Success(results))

            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
    }.flowOn(Dispatchers.IO)
}