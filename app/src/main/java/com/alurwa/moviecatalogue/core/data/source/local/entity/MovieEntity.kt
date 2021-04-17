package com.alurwa.moviecatalogue.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "movieId")
        var id: Int,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "release_date")
        var releaseDate: String,

        @ColumnInfo(name = "poster_path")
        val posterPath: String,

        @ColumnInfo(name = "vote_average")
        val voteAverage: Double
)
