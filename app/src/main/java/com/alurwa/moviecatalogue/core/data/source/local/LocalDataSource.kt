package com.alurwa.moviecatalogue.core.data.source.local

import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val movieCatalogueDao: MovieCatalogueDao
) {

    fun getMovies() = movieCatalogueDao.getAllMovies()

    suspend fun deleteAllMovies() {
        movieCatalogueDao.deleteAllMovies()
    }

    fun getFilmDetail(id: Int) = movieCatalogueDao.getFilmDetail(id)

    suspend fun insertFilmDetail(filmDetailEntity: FilmDetailEntity) {
        movieCatalogueDao.insertFilmDetail(filmDetailEntity)
    }
}