package com.alurwa.moviecatalogue.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity

@Database(entities = [FilmDetailEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieCatalogueDatabase : RoomDatabase() {
    abstract fun movieCatalogueDao(): MovieCatalogueDao
}