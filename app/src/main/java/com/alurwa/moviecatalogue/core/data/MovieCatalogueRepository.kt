package com.alurwa.moviecatalogue.core.data

import androidx.paging.PagingData
import com.alurwa.moviecatalogue.core.data.source.local.LocalDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.RemoteDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.MovieDetail
import com.alurwa.moviecatalogue.core.utils.DataMapper
import com.alurwa.moviecatalogue.main.MovieSortEnum
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCatalogueRepository @Inject constructor(
        private val remoteDataSource: RemoteDataSource,
        private val localDataSource: LocalDataSource
) : IMovieCatalogueRepository {

    override fun getAllMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())
        val allMovies = remoteDataSource.getAllMovies()

        when (val apiResponse = allMovies.first()) {
            is ApiResponse.Success -> {
                localDataSource.deleteAllMovies()
                emitAll(allMovies.map {
                    Resource.Success(DataMapper.movieResponseToDomain(apiResponse.data))
                })
            }

            is ApiResponse.Error -> {
                emit(Resource.Error<List<Movie>>(apiResponse.errorMessage))
            }

            is ApiResponse.Empty -> {
                emit(Resource.Success(emptyList<Movie>()))
            }

        }
    }

    override fun getMovies(code: String): Flow<Resource<List<Movie>>> =
            object : NetworkBoundResource<List<Movie>, List<MovieResponse>>() {
                override fun loadFromDB(): Flow<List<Movie>> {
                  return localDataSource.getMovies().map {
                      DataMapper.movieEntityToDomain(it)
                  }
                }

                override fun shouldFetch(data: List<Movie>?): Boolean {
                    return true
                }

                override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                    return when (code) {
                        "1" -> remoteDataSource.getMovieDiscover()
                        else -> throw IllegalArgumentException("Ilegal")
                    }
                }

                override suspend fun saveCallResult(data: List<MovieResponse>) {

                }

            }.asFlow()

    override fun getDiscoveryMovies(sort: MovieSortEnum): Flow<PagingData<Movie>> {
        return remoteDataSource.getDiscoveryMovies(sort)
    }

    override fun getSearchMovies(query: String): Flow<PagingData<Movie>> {
        return remoteDataSource.getSearchMovies(query)
    }

    override fun getMovieDetail(id: Int) = flow<Resource<MovieDetail>> {
        emit(Resource.Loading())
        val response = remoteDataSource.getMovieDetail(id)

        when (val apiResponse = response.first()) {
            is ApiResponse.Success -> {
                emit(Resource.Success(DataMapper.movieDetailResponseToDomain(apiResponse.data)))
            }

            is ApiResponse.Error -> {
                emit(Resource.Error(apiResponse.errorMessage))
            }
        }
    }

}