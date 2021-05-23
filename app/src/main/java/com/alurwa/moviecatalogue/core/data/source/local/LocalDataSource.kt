package com.alurwa.moviecatalogue.core.data.source.local

import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.entity.TvDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDao
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDatabase
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val movieCatalogueDao: MovieCatalogueDao,
    private val database: MovieCatalogueDatabase
) : ILocalDataSource {

   private val tvDetailDao = database.tvDetailDao()

    /*  fun getMovies() = movieCatalogueDao.getAllMovies()

     suspend fun deleteAllMovies() {
         movieCatalogueDao.deleteAllMovies()
     }

    */

    override fun getFilmDetail(id: Int) = movieCatalogueDao.getFilmDetail(id)

    override suspend fun insertFilmDetail(filmDetailEntity: FilmDetailEntity) {
        movieCatalogueDao.insertFilmDetail(filmDetailEntity)
    }

    override fun getTvDetail(id: Int) = tvDetailDao.getTvDetail(id)

    override suspend fun insertTvDetail(tvDetailEntity: TvDetailEntity) {
        tvDetailDao.insertTvDetail(tvDetailEntity)
    }
}