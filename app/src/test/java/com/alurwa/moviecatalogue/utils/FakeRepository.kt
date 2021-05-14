package com.alurwa.moviecatalogue.utils

import androidx.paging.PagingData
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.main.MovieSortEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeRepository : IMovieCatalogueRepository {
    override fun getAllMovies(): Flow<Resource<List<Movie>>> {
        return emptyFlow()
    }

    override fun getMovies(code: String): Flow<Resource<List<Movie>>> {
        return emptyFlow()
    }

    override fun getDiscoveryMovies(sort: MovieSortEnum): Flow<PagingData<Movie>> {
        return emptyFlow()
    }

    override fun getSearchMovies(query: String): Flow<PagingData<Movie>> {
        return emptyFlow()
    }

    override fun getFilmDetail(id: Int): Flow<Resource<FilmDetail>> {
        return emptyFlow()
    }

    override fun getTvList(sort: MovieSortEnum): Flow<PagingData<Movie>> {
        return emptyFlow()
    }

    override fun getTvDetail(id: Int): Flow<Resource<TvDetail>> {
        return emptyFlow()
    }

    override fun getTvSearch(query: String): Flow<PagingData<Movie>> {
        return emptyFlow()
    }
}