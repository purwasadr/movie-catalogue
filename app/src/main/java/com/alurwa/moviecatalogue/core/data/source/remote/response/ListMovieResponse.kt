package com.alurwa.moviecatalogue.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListMovieResponse(

        @field:SerializedName("results")
        val results: List<MovieResponse>,

        @field:SerializedName("errors")
        val errors: List<String>,

        @field:SerializedName("total_pages")
        val totalPages: Int
)