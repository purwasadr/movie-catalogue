package com.alurwa.moviecatalogue.boxlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.main.MovieSortEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Purwa Shadr Al 'urwa on 27/05/2021
 */

@HiltViewModel
class BoxListViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: IMovieCatalogueRepository
) : ViewModel() {
    val movies = if (filmOrTv == FilmOrTv.FILM.code) {
        repository.getFilms(MovieSortEnum.valueOf(movieSort)).cachedIn(viewModelScope)
    } else if (filmOrTv == FilmOrTv.TV.code) {
        repository.getTvList(MovieSortEnum.valueOf(movieSort)).cachedIn(viewModelScope)
    } else {
        throw IllegalArgumentException("Intent not match any")
    }

    val filmOrTv
    get() = stateHandle.get<Int>(BoxListActivity.EXTRA_FILM_OR_MOVIE)!!

    val movieSort
    get() = stateHandle.get<String>(BoxListActivity.EXTRA_MOVIE_SORT)!!

}