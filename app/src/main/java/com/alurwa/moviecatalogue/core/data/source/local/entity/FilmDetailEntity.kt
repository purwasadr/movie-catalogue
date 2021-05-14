package com.alurwa.moviecatalogue.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.core.model.Genre

@Entity(tableName = "film_detail")
data class FilmDetailEntity(

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        val id: Int,

        @ColumnInfo(name = "title")
        val title: String,

        val overview: String?,

        @ColumnInfo(name = "poster_path")
        val posterPath: String?,

        @ColumnInfo(name = "backdrop_path")
        val backdropPath: String?,

        @ColumnInfo(name = "release_date")
        val releaseDate: String,

        val revenue: Int,

        val budget: Int,

        @ColumnInfo(name = "original_language")
        val originalLanguage: String,

        val runtime: Int?,

        val status: String,

        @ColumnInfo(name = "vote_average")
        val voteAverage: Number,

        val genres: List<Genre>,

        val cast: List<Cast>
)
