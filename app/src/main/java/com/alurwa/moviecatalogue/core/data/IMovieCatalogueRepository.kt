package com.alurwa.moviecatalogue.core.data

import androidx.paging.PagingData
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.MovieDetail
import com.alurwa.moviecatalogue.core.model.Tv
import com.alurwa.moviecatalogue.main.MovieSortEnum
import kotlinx.coroutines.flow.Flow

interface IMovieCatalogueRepository {
    fun getAllMovies(): Flow<Resource<List<Movie>>>

    fun getMovies(code: String): Flow<Resource<List<Movie>>>

    fun getDiscoveryMovies(sort: MovieSortEnum): Flow<PagingData<Movie>>

    fun getSearchMovies(query: String): Flow<PagingData<Movie>>

    fun getMovieDetail(id: Int): Flow<Resource<MovieDetail>>

    fun getTvList(sort: MovieSortEnum): Flow<PagingData<Movie>>

    fun getTvDetail(id: Int): Flow<Resource<MovieDetail>>
}