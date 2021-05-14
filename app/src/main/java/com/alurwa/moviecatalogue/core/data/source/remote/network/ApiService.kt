/**
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

package com.alurwa.moviecatalogue.core.data.source.remote.network

import com.alurwa.moviecatalogue.BuildConfig
import com.alurwa.moviecatalogue.core.data.source.remote.response.FilmDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.ListMovieResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.TvListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY)
    suspend fun getAllMovies(): ListMovieResponse

    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY)
    suspend fun getDiscoverMovies(@Query("sort_by")sortBy: String,
                                  @Query("page")pagePos: Int

    ): ListMovieResponse

    @GET("3/movie/{type}?api_key=" + BuildConfig.API_KEY)
    suspend fun getMovies(@Path("type")type: String, @Query("page")pagePos: Int): ListMovieResponse

    @GET("3/search/movie?api_key=${BuildConfig.API_KEY}")
    suspend fun searchMovies(@Query("query")query: String,  @Query("page")pagePos: Int): ListMovieResponse

    @GET("3/movie/{id}?api_key=" + BuildConfig.API_KEY + "&append_to_response=credits")
    suspend fun getFilmDetail(@Path("id")id: Int): FilmDetailResponse

    @GET("3/discover/tv?api_key=" + BuildConfig.API_KEY)
    suspend fun getDiscoverTv(@Query("sort_by")sortBy: String,
                              @Query("page")pagePos: Int

    ): TvListResponse

    @GET("3/tv/{type}?api_key=" + BuildConfig.API_KEY)
    suspend fun getTv(@Path("type")type: String, @Query("page")pagePos: Int): TvListResponse

    @GET("3/search/tv?api_key=${BuildConfig.API_KEY}")
    suspend fun searchTv(@Query("query")query: String,  @Query("page")pagePos: Int): TvListResponse

    @GET("3/tv/{id}?api_key=" + BuildConfig.API_KEY + "&append_to_response=credits")
    suspend fun getTvDetail(@Path("id")id: Int): TvDetailResponse
}