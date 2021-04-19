package com.alurwa.moviecatalogue.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("title")
        val title: String,

        @field:SerializedName("overview")
        val overview: String,

        @field:SerializedName("poster_path")
        val posterPath: String,

        @field:SerializedName("backdrop_path")
        val backdropPath: String,

        @field:SerializedName("release_date")
        val releaseDate: String,

        @field:SerializedName("revenue")
        val revenue: Int,

        @field:SerializedName("budget")
        val budget: Int,

        @field:SerializedName("runtime")
        val runtime: Int,

        @field:SerializedName("original_language")
        val originalLanguage: String,

        @field:SerializedName("status")
        val status: String,

        @field:SerializedName("vote_average")
        val voteAverage: Number,

        @field:SerializedName("genres")
        val genres: List<GenreResponse>
)
