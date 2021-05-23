package com.alurwa.moviecatalogue.core.data.source.local

import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.entity.TvDetailEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Purwa Shadr Al 'urwa on 22/05/2021
 */

interface ILocalDataSource {
    fun getFilmDetail(id: Int): Flow<FilmDetailEntity?>

    suspend fun insertFilmDetail(filmDetailEntity: FilmDetailEntity)

    fun getTvDetail(id: Int): Flow<TvDetailEntity?>

    suspend fun insertTvDetail(tvDetailEntity: TvDetailEntity)
}