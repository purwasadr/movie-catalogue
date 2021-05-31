/*
 * MIT License
 *
 * Copyright (c) 2021 Purwa Shadr Al 'urwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.alurwa.moviecatalogue.utils

import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.entity.TvDetailEntity
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvResponse
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.Genre
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.core.model.Season
import com.alurwa.moviecatalogue.core.model.TvDetail

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

    fun movieEntityToDomain(input: List<FilmDetailEntity>): List<Movie> {
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

    fun filmDetailEntityToDomain(input: FilmDetailEntity?): FilmDetail? {
        return if (input == null) {
            null
        } else {
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
                genres = input.genres,
                cast = input.cast
            )
        }
    }

    fun filmDetailResponseToEntity(input: FilmDetailResponse): FilmDetailEntity =
        FilmDetailEntity(
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

    fun movieDetailResponseToDomain(input: FilmDetailResponse) =
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

    // FIXME: this mapper not good cause use different name properties
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
            type = input.type,
            genres = input.genres.map {
                Genre(it.id, it.name)
            },
            seasons = input.seasons.map {
                Season(
                    it.id, it.name,
                    it.overview, it.posterPath,
                    it.airDate, it.episodeCount,
                    it.seasonNumber
                )
            },
            cast = input.credits.cast.map {
                Cast(it.name, it.profilePath, it.character)
            }
        )

    fun tvDetailResponseToEntity(input: TvDetailResponse): TvDetailEntity =
        TvDetailEntity(
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
            type = input.type,
            genres = input.genres.map {
                Genre(it.id, it.name)
            },
            seasons = input.seasons.map {
                Season(
                    it.id, it.name,
                    it.overview, it.posterPath,
                    it.airDate, it.episodeCount,
                    it.seasonNumber
                )
            },
            cast = input.credits.cast.map {
                Cast(it.name, it.profilePath, it.character)
            }
        )

    fun tvDetailEntityToDomain(input: TvDetailEntity?): TvDetail? =
        input?.run {
            TvDetail(
                id = id,
                name = name,
                overview = overview,
                posterPath = posterPath,
                backdropPath = backdropPath,
                firstAirDate = firstAirDate,
                originalLanguage = originalLanguage,
                originalName = originalName,
                status = status,
                voteAverage = voteAverage,
                type = type,
                genres = genres,
                seasons = seasons,
                cast = cast,
            )
        }
}
