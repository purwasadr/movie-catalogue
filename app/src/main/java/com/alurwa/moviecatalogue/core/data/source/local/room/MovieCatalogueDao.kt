package com.alurwa.moviecatalogue.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.alurwa.moviecatalogue.core.data.source.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieCatalogueDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}