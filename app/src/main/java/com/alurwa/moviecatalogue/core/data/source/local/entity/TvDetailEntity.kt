package com.alurwa.moviecatalogue.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.core.model.Genre
import com.alurwa.moviecatalogue.core.model.Season

/**
 * Created by Purwa Shadr Al 'urwa on 22/05/2021
 */

@Entity(tableName = "tv_detail")
data class TvDetailEntity(

    @PrimaryKey
    val id: Int,

    val name: String,

    val overview: String?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name ="first_air_date")
    val firstAirDate: String?,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String,

    @ColumnInfo(name = "original_name")
    val originalName: String,

    val status: String,

    @ColumnInfo( name = "vote_average")
    val voteAverage: Number,

    val type: String,

    val genres: List<Genre>,

    val seasons: List<Season>,

    val cast: List<Cast>
)
