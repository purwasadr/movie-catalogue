package com.alurwa.moviecatalogue.detail

import androidx.lifecycle.*
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.TvDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IMovieCatalogueRepository
) : ViewModel() {

    private var movieDetail: LiveData<Resource<FilmDetail>>? = null

    private var tvDetail: LiveData<Resource<TvDetail>>? = null

    fun getMovieDetail(id: Int): LiveData<Resource<FilmDetail>> {
        // Just check if not null because the id never changed
        return movieDetail ?: repository.getMovieDetail(id).asLiveData().also {
            movieDetail = it
        }
    }

    fun getTvDetail(id: Int): LiveData<Resource<TvDetail>> {
        // Just check if not null because the id never changed
        return tvDetail ?: repository.getTvDetail(id).asLiveData().also {
            tvDetail = it
        }
    }
}