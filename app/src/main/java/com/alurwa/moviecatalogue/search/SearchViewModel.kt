package com.alurwa.moviecatalogue.search

import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
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
class SearchViewModel @Inject constructor(
        private val movieCatalogueRepository: IMovieCatalogueRepository
) : ViewModel() {
    private var currentQuery: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null

    var listState: Parcelable? = null

    fun searchMovie(query: String): Flow<PagingData<Movie>> {
        val lastResult = currentSearchResult
        if (query == currentQuery && lastResult != null) {
            return lastResult
        }

        currentQuery = query

        val newResult = movieCatalogueRepository.getSearchMovies(query)
                .cachedIn(viewModelScope)

        currentSearchResult = newResult

        return newResult
    }
}