package com.alurwa.moviecatalogue.main

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IMovieCatalogueRepository
) : ViewModel() {

    var currentSort: MovieSortEnum? = null

    var currentMovieResult: Flow<PagingData<Movie>>? = null

    private var currentTvResult: Flow<PagingData<Movie>>? = null

    private var currentTvSort: MovieSortEnum? = null

    var listState: Parcelable? = null

    var chipState: Int? = null

    fun getFilm(sortEnum: MovieSortEnum): Flow<PagingData<Movie>> {
        val movieResult = currentMovieResult

        if (sortEnum == currentSort && movieResult != null) {
            return movieResult
        }

        currentSort = sortEnum

        val newResult = repository.getDiscoveryMovies(sortEnum).cachedIn(viewModelScope)

        currentMovieResult = newResult

        return newResult
    }

    fun getTv(sortEnum: MovieSortEnum): Flow<PagingData<Movie>> {
        val tvResult = currentTvResult

        if (sortEnum == currentTvSort && tvResult != null) {
            return tvResult
        }

        currentTvSort = sortEnum

        val newResult = repository.getTvList(sortEnum).cachedIn(viewModelScope)

        currentTvResult = newResult

        return newResult
    }
}