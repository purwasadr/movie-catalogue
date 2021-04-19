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
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IMovieCatalogueRepository
) : ViewModel() {
    // val allMovie = movieCatalogueDatabase.getAllMovies().asLiveData()
    var currentSort: MovieSortEnum? = null

    var currentMovieResult: Flow<PagingData<Movie>>? = null

    var listState: Parcelable? = null

    var chipState: Int? = null

    fun getMovies(sortEnum: MovieSortEnum): Flow<PagingData<Movie>> {
        val movieResult = currentMovieResult

        if (sortEnum == currentSort && movieResult != null) {
            return movieResult
        }

        currentSort = sortEnum

        val newResult = repository.getDiscoveryMovies(sortEnum).cachedIn(viewModelScope)

        currentMovieResult = newResult

        return newResult
    }
}