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

package com.alurwa.moviecatalogue.core.model

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
) {

    val fullPosterPath: String?
        get() = posterPath?.let {
            "https://image.tmdb.org/t/p/w185$it"
        }

    val fullBackdropPath: String?
        get() = posterPath?.let {
            "https://image.tmdb.org/t/p/w500$it"
        }
}
