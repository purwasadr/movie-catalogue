package com.alurwa.moviecatalogue.utils

import com.alurwa.moviecatalogue.core.data.source.local.entity.MovieEntity
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvResponse
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
            FilmDetail(
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
                    releaseDate = it.firstAirDate,
                    posterPath = it.posterPath,
                    voteAverage = it.voteAverage
            )
        }
    }

    fun tvDetailResponseToDomain(input: TvDetailResponse): TvDetail =
            TvDetail(
                    id = input.id,
                    name = input.name,
                    overview = input.overview,
                    posterPath = input.posterPath,
                    backdropPath = input.backdropPath,
                    firstAirDate = input.firstAirDate,
                    originalLanguage = input.originalLanguage,
                    originalName = input.originalName,
                    status = input.status,
                    voteAverage = input.voteAverage,
                    genres = input.genres.map {
                        Genre(it.id, it.name)
                    },
                    seasons = input.seasons.map {
                        Season(it.id, it.name,
                                it.overview, it.posterPath,
                                it.airDate, it.episodeCount,
                                it.seasonNumber)


                    },
                    cast = input.credits.cast.map {
                        Cast(it.name,it.profilePath, it.character)
                    }
            )
}