package com.alurwa.moviecatalogue.core.data.source.remote

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alurwa.moviecatalogue.core.data.MoviePagingSource
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.main.MovieSortEnum
import com.alurwa.moviecatalogue.utils.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    val apiService: ApiService,
    @ApplicationContext private val context: Context
    ) {
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

    fun getFilmDetail(id: Int) = flow<ApiResponse<FilmDetailResponse>> {
        try {
            if (NetworkState.isNetworkAvailable(context)) {
                val results = apiService.getFilmDetail(id)
                emit(ApiResponse.Success(results))
            } else {
                emit(ApiResponse.NoConnection)
            }
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