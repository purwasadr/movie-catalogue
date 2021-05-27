package com.alurwa.moviecatalogue.core.data.source.remote

import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
   private val apiService: ApiService,
   private val dispatchers: CoroutineDispatcher
): IRemoteDataSource {
   /* suspend fun getAllMovies(): Flow<ApiResponse<List<MovieResponse>>> {
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

    */

    override fun getFilmDetail(id: Int) = flow<ApiResponse<FilmDetailResponse>> {
        try {
            val results = apiService.getFilmDetail(id)
            emit(ApiResponse.Success(results))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(dispatchers)

    override fun getTvDetail(id: Int) = flow<ApiResponse<TvDetailResponse>> {
        try {
            val results = apiService.getTvDetail(id)
            emit(ApiResponse.Success(results))

        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(dispatchers)
}