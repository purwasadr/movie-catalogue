package com.alurwa.moviecatalogue.core.data.source.local

import com.alurwa.moviecatalogue.core.data.source.local.entity.MovieEntity
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val movieCatalogueDao: MovieCatalogueDao) {
    fun getMovies() = movieCatalogueDao.getAllMovies()

    suspend fun deleteAllMovies() {
        movieCatalogueDao.deleteAllMovies()
    }
}