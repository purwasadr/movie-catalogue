package com.alurwa.moviecatalogue.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(
        val id: Int,
        val title: String?,
        val releaseDate: String?,
        val posterPath: String?,
        val voteAverage: Number?
) : Parcelable
