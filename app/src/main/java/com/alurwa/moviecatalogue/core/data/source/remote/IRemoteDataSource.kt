package com.alurwa.moviecatalogue.core.data.source.remote

import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by Purwa Shadr Al 'urwa on 22/05/2021
 */

interface IRemoteDataSource {
    fun getFilmDetail(id: Int): Flow<ApiResponse<FilmDetailResponse>>

    fun getTvDetail(id: Int): Flow<ApiResponse<TvDetailResponse>>
}