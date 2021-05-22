package com.alurwa.moviecatalogue.core.data.source.local

import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.entity.TvDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDao
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val movieCatalogueDao: MovieCatalogueDao,
    private val database: MovieCatalogueDatabase
) {

    private val tvDetailDao = database.tvDetailDao()

    fun getMovies() = movieCatalogueDao.getAllMovies()

    suspend fun deleteAllMovies() {
        movieCatalogueDao.deleteAllMovies()
    }

    fun getFilmDetail(id: Int) = movieCatalogueDao.getFilmDetail(id)

    suspend fun insertFilmDetail(filmDetailEntity: FilmDetailEntity) {
        movieCatalogueDao.insertFilmDetail(filmDetailEntity)
    }

    fun getTvDetail(id: Int) = tvDetailDao.getTvDetail(id)

    suspend fun insertTvDetail(tvDetailEntity: TvDetailEntity) {
        tvDetailDao.insertTvDetail(tvDetailEntity)
    }
}