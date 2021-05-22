package com.alurwa.moviecatalogue.tvdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.utils.Constants.EXTRA_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModel @Inject constructor(
    repository: IMovieCatalogueRepository,
    stateHandle: SavedStateHandle
) : ViewModel() {

    // Hilt secara default menyediakan SavedStateHandle yang didapat dari Intent
    // jika ViewModel dihubungkan dengan activity atau Argument jika
    // ViewModel dihubungkan dengan fragment
    private val id = stateHandle.get<Int>(EXTRA_ID)!!

    val tvDetail: LiveData<Resource<TvDetail?>> =
        repository.getTvDetail(id).asLiveData()
}