package com.alurwa.moviecatalogue.core.data

import com.alurwa.moviecatalogue.core.data.source.local.ILocalDataSource
import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.entity.TvDetailEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Created by Purwa Shadr Al 'urwa on 22/05/2021
 */

class FakeLocalDataSource: ILocalDataSource {
    private val filmDetail = mutableListOf<FilmDetailEntity>()

    override fun getFilmDetail(id: Int): Flow<FilmDetailEntity?> {
        return flowOf(filmDetail.get(0))
    }

    override suspend fun insertFilmDetail(filmDetailEntity: FilmDetailEntity) {
        filmDetail.add(filmDetailEntity)
    }

    override fun getTvDetail(id: Int): Flow<TvDetailEntity?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTvDetail(tvDetailEntity: TvDetailEntity) {
        TODO("Not yet implemented")
    }

}