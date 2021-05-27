package com.alurwa.moviecatalogue.core.data

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    override fun getFilms(sort: MovieSortEnum): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20,
                maxSize = 60,
            ),
            pagingSourceFactory = { MoviePagingSource(apiService, sort = sort) }
        ).flow
    }

    override fun getSearchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,

                maxSize = 60
            ),
            pagingSourceFactory = { MoviePagingSource(apiService, query = query) }
        ).flow
    }

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
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,

                maxSize = 60
            ),
            pagingSourceFactory = {
                TvPagingSource(
                    apiService = apiService,
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
            pagingSourceFactory = { TvPagingSource(apiService, query = query) }
        ).flow
}