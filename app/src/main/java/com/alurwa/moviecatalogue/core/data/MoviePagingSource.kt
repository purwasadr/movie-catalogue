package com.alurwa.moviecatalogue.core.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.utils.DataMapper
import com.alurwa.moviecatalogue.main.MovieSortEnum
import retrofit2.HttpException
import java.io.IOException
import kotlin.IllegalArgumentException

class MoviePagingSource(
    private val apiService: ApiService,
    private val sort: MovieSortEnum? = null,
    private val query: String? = null
) : PagingSource<Int, Movie>() {

    constructor(apiService: ApiService, sort: MovieSortEnum) : this(apiService, sort, null)

    constructor(apiService: ApiService, query: String): this(apiService, null, query)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGING_INDEX
        return try {
            val response = when {
                sort != null -> getMovieApi(sort, position)
                query != null -> {
                    apiService.searchMovies(query, position)
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }

            val maxPage = response.totalPages
           // Log.d(this.javaClass.simpleName, response.results.size.toString())
            val repos = DataMapper.movieResponseToDomain(response.results)
            val nextKey = if (position == maxPage || maxPage == 0) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = repos,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )

        } catch (ex: IOException) {
            Log.d(TAG, "IOException")
            return LoadResult.Error(ex)

        } catch (ex: HttpException) {
            Log.d(TAG, "HttpException")
            return LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }

    private suspend fun getMovieApi(sort: MovieSortEnum, position: Int) =
            when (sort) {
                MovieSortEnum.DISCOVER -> apiService.getDiscoverMovies(position)
                MovieSortEnum.POPULAR -> apiService.getMovies("popular", position)
                MovieSortEnum.UPCOMING -> apiService.getMovies("upcoming", position)
                else -> {
                    throw IllegalArgumentException("illegal")
                }
    }

    companion object {
        const val STARTING_PAGING_INDEX = 1
        const val TAG = "MoviePagingSource"
    }
}