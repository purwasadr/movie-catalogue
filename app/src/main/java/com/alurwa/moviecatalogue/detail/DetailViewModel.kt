package com.alurwa.moviecatalogue.detail

import androidx.lifecycle.*
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.MovieDetail
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IMovieCatalogueRepository
) : ViewModel() {

    var movieDetail: LiveData<Resource<MovieDetail>>? = null

    fun getMovieDetail(id: Int): LiveData<Resource<MovieDetail>> {
        // Just check if not null because the id never changed
        return movieDetail ?: repository.getMovieDetail(id).asLiveData().also {
            movieDetail = it
        }
    }
}