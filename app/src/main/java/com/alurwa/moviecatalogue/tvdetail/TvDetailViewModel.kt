package com.alurwa.moviecatalogue.tvdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alurwa.moviecatalogue.core.common.CastAdapter
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.TvDetail
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class TvDetailViewModel @javax.inject.Inject constructor(
    private val repository: IMovieCatalogueRepository
) : ViewModel(){


    private var tvDetail: LiveData<Resource<TvDetail>>? = null

    fun getTvDetail(id: Int): LiveData<Resource<TvDetail>> {
        // Just check if not null because the id never changed
        return tvDetail ?: repository.getTvDetail(id).asLiveData().also {
            tvDetail = it
        }
    }
}