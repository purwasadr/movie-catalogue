package com.alurwa.moviecatalogue.filmdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilmDetailViewModel @Inject constructor(
    private val repository: IMovieCatalogueRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val id = stateHandle.get<Int>(Constants.EXTRA_ID)!!

    val filmDetail: LiveData<Resource<FilmDetail?>> = repository.getFilmDetail(id).asLiveData()
}