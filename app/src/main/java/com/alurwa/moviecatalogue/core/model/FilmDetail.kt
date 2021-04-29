package com.alurwa.moviecatalogue.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilmDetail(
        val id: Int,

        val title: String,

        val overview: String?,

        val posterPath: String?,

        val backdropPath: String?,

        val releaseDate: String,

        val revenue: Int,

        val budget: Int,

        val originalLanguage: String,

        val runtime: Int?,

        val status: String,

        val voteAverage: Number,

        val genres: List<Genre>,

        val cast: List<Cast>
) : Parcelable

