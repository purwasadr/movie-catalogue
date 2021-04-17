package com.alurwa.moviecatalogue.core.di

import android.content.Context
import androidx.room.Room
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDao
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieCatalogueDatabase =
        Room.databaseBuilder(
            context,
            MovieCatalogueDatabase::class.java, "MovieCatalogue.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideTourismDao(database: MovieCatalogueDatabase): MovieCatalogueDao =
        database.movieCatalogueDao()
}