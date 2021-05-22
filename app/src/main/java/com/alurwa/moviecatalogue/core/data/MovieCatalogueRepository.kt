package com.alurwa.moviecatalogue.core.data

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alurwa.moviecatalogue.core.data.source.local.LocalDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.RemoteDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.main.MovieSortEnum
import com.alurwa.moviecatalogue.utils.DataMapper
import com.alurwa.moviecatalogue.utils.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class MovieCatalogueRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @ApplicationContext private val context: Context
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

            override fun shouldFetch(): Boolean {
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

    override fun getFilmDetail(id: Int): Flow<Resource<FilmDetail?>> =
        object : NetworkBoundResource<FilmDetail?, FilmDetailResponse>() {
            override fun loadFromDB(): Flow<FilmDetail?> {
                Timber.d("loadFromDB")
                return localDataSource.getFilmDetail(id).map {
                    DataMapper.filmDetailEntityToDomain(it)
                }
            }

            override fun shouldFetch(): Boolean {
                return NetworkState.isNetworkAvailable(context)
            }

            override suspend fun createCall(): Flow<ApiResponse<FilmDetailResponse>> =
                remoteDataSource.getFilmDetail(id)

            override suspend fun saveCallResult(data: FilmDetailResponse) {
                val entity = DataMapper.filmDetailResponseToEntity(data)
                localDataSource.insertFilmDetail(entity)
                Timber.d("SaveCallResult")
            }

        }.asFlow()

    override fun getTvList(sort: MovieSortEnum): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,

                maxSize = 60
            ),
            pagingSourceFactory = {
                TvPagingSource(
                    apiService = remoteDataSource.apiService,
                    sort = sort
                )
            }
        ).flow

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
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,

                maxSize = 60
            ),
            pagingSourceFactory = { TvPagingSource(remoteDataSource.apiService, query = query) }
        ).flow
}