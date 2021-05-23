package com.alurwa.moviecatalogue.utils

import com.alurwa.moviecatalogue.core.data.source.remote.response.CastResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.CreditResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.GenreResponse
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.Genre

/**
 * Created by Purwa Shadr Al 'urwa on 22/05/2021
 */

object DataDummy {
    fun getFilmDetailResponse() =
        FilmDetailResponse(
            id = 111,
            title = "Kimi no Nawa",
            overview = "Kimi no namaewa mitsuha-mitsuha",
            posterPath = "www.iniPathPoster.com",
            backdropPath = "www.iniBackDropPath.com",
            releaseDate = "19-04-2002",
            revenue = 1000000000,
            budget = 2000000000,
            runtime = 120,
            originalLanguage = "Japanese",
            status = "Released",
            voteAverage = 8.5,
            genres = listOf(
                GenreResponse(123, "Animation"),
                GenreResponse(122, "Romance")
            ),
            credits = CreditResponse(
                cast = listOf(
                    CastResponse(
                        "Nao Toyamaka",
                        "www.profilePath.com",
                        "Mitsuha Miyamizu"
                    ),
                    CastResponse(
                        "Bocah mboh",
                        "www.profilePath2.com",
                        "Taki Kun"
                    )
                )
            )
        )

    fun getFilmDetail() = FilmDetail(
        id = 111,
        title = "Kimi no Nawa",
        overview = "Kimi no namaewa mitsuha-mitsuha",
        posterPath = "www.iniPathPoster.com",
        backdropPath = "www.iniBackDropPath.com",
        releaseDate = "19-04-2002",
        revenue = 1000000000,
        budget = 2000000000,
        runtime = 120,
        originalLanguage = "Japanese",
        status = "Released",
        voteAverage = 8.5,
        genres = listOf(
            Genre(123, "Animation"),
            Genre(122, "Romance")
        ),
        cast = listOf(
            Cast(
                "Nao Toyamaka",
                "www.profilePath.com",
                "Mitsuha Miyamizu"
            ),
            Cast(
                "Bocah mboh",
                "www.profilePath2.com",
                "Taki Kun"
            )
        )
    )
}