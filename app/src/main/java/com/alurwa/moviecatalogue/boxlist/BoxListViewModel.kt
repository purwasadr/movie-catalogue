package com.alurwa.moviecatalogue.boxlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
    val movies = repository.getFilms(MovieSortEnum.POPULAR)

}