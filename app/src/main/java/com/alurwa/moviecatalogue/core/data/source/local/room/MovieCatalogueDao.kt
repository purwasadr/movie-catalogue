package com.alurwa.moviecatalogue.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieCatalogueDao {

    @Query("SELECT * FROM film_detail")
    fun getAllMovies(): Flow<List<FilmDetailEntity>>

    @Query("SELECT * FROM film_detail WHERE id = :filmId")
    fun getFilmDetail(filmId: Int): Flow<FilmDetailEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmDetail(filmDetail: FilmDetailEntity)

    @Query("DELETE FROM film_detail")
    suspend fun deleteAllMovies()
}