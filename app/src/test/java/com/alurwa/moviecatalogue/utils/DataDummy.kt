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
