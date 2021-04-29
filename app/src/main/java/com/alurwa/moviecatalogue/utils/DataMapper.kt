package com.alurwa.moviecatalogue.utils

import com.alurwa.moviecatalogue.core.data.source.local.entity.MovieEntity
import com.alurwa.moviecatalogue.core.data.source.remote.response.*
import com.alurwa.moviecatalogue.core.model.*

object DataMapper {
    fun movieResponseToDomain(input: List<MovieResponse>): List<Movie> {
        return input.map {
            Movie(
                    id = it.id,
                    title = it.title,
                    releaseDate = it.releaseDate,
                    posterPath = it.posterPath,
                    voteAverage = it.voteAverage
            )
        }
    }

    fun movieEntityToDomain(input: List<MovieEntity>): List<Movie> {
        return input.map {
            Movie(
                    id = it.id,
                    title = it.title,
                    releaseDate = it.releaseDate,
                    posterPath = it.posterPath,
                    voteAverage = it.voteAverage
            )
        }
    }

    fun movieDetailResponseToDomain(input: MovieDetailResponse) =
            MovieDetail(
                    id = input.id,
                    title = input.title,
                    overview = input.overview,
                    posterPath = input.posterPath,
                    backdropPath = input.backdropPath,
                    releaseDate = input.releaseDate,
                    revenue = input.revenue,
                    budget = input.budget,
                    originalLanguage = input.originalLanguage,
                    runtime = input.runtime,
                    status = input.status,
                    voteAverage = input.voteAverage,
                    genres = input.genres.map {
                        Genre(it.id, it.name)
                    },
                    cast = input.credits.cast.map {
                        Cast(it.name, it.profilePath, it.character)
                    }
            )

    //FIXME: this mapper not good cause use different name properties
    fun tvListResponseToDomain(input: List<TvResponse>): List<Movie> {
        return input.map {
            Movie(
                    id = it.id,
                    title = it.name,
                    releaseDate  = it.firstAirDate,
                    posterPath = it.posterPath,
                    voteAverage = it.voteAverage
            )
        }
    }

    fun tvDetailResponseToDomain(input: List<TvDetailResponse>): List<TvDetail> =
            TvDetail(

            )
}